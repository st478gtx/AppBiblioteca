package colecciones;

import clases.Autor;
import clases.Libro;
import clases.LibroAutor;
import repository.Repositorio;
import util.Codigos;
import util.Rutas;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Locale;

/** Catálogo y relación; los libros históricos sin vínculo se enlazan solo en memoria. */
public final class DatosAutor {
    private final Repositorio<Autor> repositorio;
    private final Repositorio<LibroAutor> relacionesRepositorio;
    private final ArrayList<Autor> autores = new ArrayList<>();
    private final ArrayList<LibroAutor> relaciones = new ArrayList<>();

    public DatosAutor(Repositorio<Autor> repositorio, Repositorio<LibroAutor> relacionesRepositorio,
                     ArrayList<Libro> libros) {
        this.repositorio = repositorio;
        this.relacionesRepositorio = relacionesRepositorio;
        boolean hayAutores = Files.exists(Rutas.dato("autores.txt"));
        boolean hayRelaciones = Files.exists(Rutas.dato("libroAutor.txt"));
        if (hayAutores != hayRelaciones)
            throw new IllegalStateException("Falta autores.txt o libroAutor.txt; no se modificaron los datos");
        if (hayAutores) {
            autores.addAll(repositorio.cargar());
            relaciones.addAll(relacionesRepositorio.cargar());
        }
        validar(libros);
        for (Libro libro : libros) {
            if (codigoDeLibro(libro.getCodLibro()) != null) continue;
            Autor autor = buscarNombre(libro.getAutor());
            if (autor == null) {
                autor = new Autor(siguienteCodigo(), limpiarNombre(libro.getAutor()));
                autores.add(autor);
            }
            relaciones.add(new LibroAutor(libro.getCodLibro(), autor.getCodigo()));
        }
    }

    public static String limpiarNombre(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Ingrese el nombre del autor");
        String limpio = nombre.strip().replaceAll("\\s+", " ");
        if (limpio.isEmpty() || limpio.contains(";") || limpio.contains("\n") || limpio.contains("\r"))
            throw new IllegalArgumentException("Nombre de autor inválido");
        return limpio;
    }

    public static String clave(String nombre) { return limpiarNombre(nombre).toLowerCase(Locale.ROOT); }
    public ArrayList<Autor> listar() { return new ArrayList<>(autores); }
    public ArrayList<LibroAutor> relaciones() { return new ArrayList<>(relaciones); }
    public void restaurar(ArrayList<Autor> anteriores, ArrayList<LibroAutor> anterioresRelaciones) {
        autores.clear(); autores.addAll(anteriores);
        relaciones.clear(); relaciones.addAll(anterioresRelaciones);
    }
    public String siguienteCodigo() {
        return Codigos.siguiente("A", 9, 1, autores.stream().map(Autor::getCodigo).toList());
    }
    public Autor obtener(String codigo) {
        for (Autor autor : autores) if (autor.getCodigo().equals(codigo)) return autor;
        return null;
    }
    public Autor buscarNombre(String nombre) {
        String clave = clave(nombre);
        for (Autor autor : autores) if (clave(autor.getNombre()).equals(clave)) return autor;
        return null;
    }
    public String codigoDeLibro(String codLibro) {
        for (LibroAutor relacion : relaciones)
            if (relacion.getCodLibro().equals(codLibro)) return relacion.getCodAutor();
        return null;
    }
    public void agregar(Autor autor) { autores.add(autor); }
    public void reemplazar(Autor autor) {
        for (int i = 0; i < autores.size(); i++)
            if (autores.get(i).getCodigo().equals(autor.getCodigo())) { autores.set(i, autor); return; }
        throw new IllegalArgumentException("Autor inexistente");
    }
    public void eliminar(String codigo) { autores.removeIf(a -> a.getCodigo().equals(codigo)); }
    public void asociar(String codLibro, String codAutor) {
        relaciones.removeIf(r -> r.getCodLibro().equals(codLibro));
        relaciones.add(new LibroAutor(codLibro, codAutor));
    }
    public void desasociar(String codLibro) { relaciones.removeIf(r -> r.getCodLibro().equals(codLibro)); }
    public boolean tieneLibros(String codAutor) {
        return relaciones.stream().anyMatch(r -> r.getCodAutor().equals(codAutor));
    }
    public void guardar() {
        repositorio.guardar(new ArrayList<>(autores));
        relacionesRepositorio.guardar(new ArrayList<>(relaciones));
    }
    private void validar(ArrayList<Libro> libros) {
        ArrayList<String> codigos = new ArrayList<>();
        ArrayList<String> nombres = new ArrayList<>();
        for (Autor autor : autores) {
            if (autor.getCodigo().isBlank() || codigos.contains(autor.getCodigo()) || nombres.contains(clave(autor.getNombre())))
                throw new IllegalStateException("Autores duplicados o inválidos en autores.txt");
            codigos.add(autor.getCodigo()); nombres.add(clave(autor.getNombre()));
        }
        ArrayList<String> vinculados = new ArrayList<>();
        for (LibroAutor relacion : relaciones) {
            if (vinculados.contains(relacion.getCodLibro()) || obtener(relacion.getCodAutor()) == null
                    || libros.stream().noneMatch(l -> l.getCodLibro().equals(relacion.getCodLibro())))
                throw new IllegalStateException("Relación inválida en libroAutor.txt");
            vinculados.add(relacion.getCodLibro());
        }
    }
}

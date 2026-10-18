package service;

import clases.Autor;
import clases.Libro;
import clases.LibroAutor;
import colecciones.DatosAutor;
import colecciones.DatosLibro;
import repository.TransaccionArchivos;

import java.util.ArrayList;

public final class AutorService {
    private final DatosAutor datos;
    private final DatosLibro libros;

    public AutorService(DatosAutor datos, DatosLibro libros) {
        this.datos = datos;
        this.libros = libros;
    }

    public ArrayList<Autor> listar() { return datos.listar(); }
    public Autor obtener(String codigo) { return datos.obtener(codigo); }
    public Autor autorDeLibro(String codigoLibro) { return datos.obtener(datos.codigoDeLibro(codigoLibro)); }
    public ArrayList<Autor> buscar(String texto) {
        String filtro = texto == null ? "" : texto.strip().toLowerCase(java.util.Locale.ROOT);
        ArrayList<Autor> resultado = new ArrayList<>();
        for (Autor autor : datos.listar())
            if (autor.getNombre().toLowerCase(java.util.Locale.ROOT).contains(filtro)
                    || autor.getCodigo().toLowerCase(java.util.Locale.ROOT).contains(filtro)) resultado.add(autor);
        return resultado;
    }

    public Autor registrar(String nombre) {
        String limpio = DatosAutor.limpiarNombre(nombre);
        if (datos.buscarNombre(limpio) != null) throw new IllegalArgumentException("El autor ya existe");
        Autor autor = new Autor(datos.siguienteCodigo(), limpio);
        cambiar(() -> datos.agregar(autor));
        return autor;
    }

    public void modificar(String codigo, String nombre) {
        Autor anterior = exigir(codigo);
        String limpio = DatosAutor.limpiarNombre(nombre);
        Autor duplicado = datos.buscarNombre(limpio);
        if (duplicado != null && !duplicado.getCodigo().equals(codigo))
            throw new IllegalArgumentException("El autor ya existe");
        cambiar(() -> {
            datos.reemplazar(new Autor(codigo, limpio));
            for (Libro libro : libros.listar()) {
                if (!codigo.equals(datos.codigoDeLibro(libro.getCodLibro()))) continue;
                Libro copia = new Libro(libro.getCodLibro(), libro.getTitulo(), limpio, libro.getIsnb(),
                        libro.getCodCategoria(), libro.getAnioPublicacion(), libro.getCantidadDisponible(),
                        libro.getCantidadTotal(), libro.getRutaPortada());
                libros.actualizar(copia);
            }
        });
    }

    public void eliminar(String codigo) {
        exigir(codigo);
        if (datos.tieneLibros(codigo)) throw new IllegalArgumentException("El autor tiene libros asociados");
        cambiar(() -> datos.eliminar(codigo));
    }

    public void agregarLibro(Libro libro) {
        Autor autor = autorSeleccionado(libro);
        cambiar(() -> {
            libro.setAutor(autor.getNombre());
            libros.agregar(libro);
            datos.asociar(libro.getCodLibro(), autor.getCodigo());
        });
    }

    public void actualizarLibro(Libro libro) {
        if (libros.obtenerPorCodigo(libro.getCodLibro()) == null) throw new IllegalArgumentException("Libro inexistente");
        Autor autor = autorSeleccionado(libro);
        cambiar(() -> {
            libro.setAutor(autor.getNombre());
            libros.actualizar(libro);
            datos.asociar(libro.getCodLibro(), autor.getCodigo());
        });
    }

    public void eliminarLibro(String codigo) {
        if (libros.obtenerPorCodigo(codigo) == null) throw new IllegalArgumentException("Libro inexistente");
        cambiar(() -> { libros.eliminar(codigo); datos.desasociar(codigo); });
    }

    private Autor autorSeleccionado(Libro libro) {
        Autor autor = datos.buscarNombre(libro.getAutor());
        if (autor == null) throw new IllegalArgumentException("Seleccione un autor registrado");
        return autor;
    }
    private Autor exigir(String codigo) {
        Autor autor = datos.obtener(codigo);
        if (autor == null) throw new IllegalArgumentException("Autor inexistente");
        return autor;
    }
    private void cambiar(Runnable accion) {
        ArrayList<Autor> autoresAntes = datos.listar();
        ArrayList<LibroAutor> relacionesAntes = datos.relaciones();
        ArrayList<Libro> librosAntes = libros.listar();
        try {
            accion.run();
            TransaccionArchivos.ejecutarAutores(() -> { datos.guardar(); libros.guardar(); },
                    "autores.txt", "libroAutor.txt", "libros.txt");
        } catch (RuntimeException error) {
            datos.restaurar(autoresAntes, relacionesAntes);
            libros.restaurar(librosAntes);
            throw error;
        }
    }
}

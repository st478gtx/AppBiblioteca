import app.BibliotecaContexto;
import clases.Libro;
import clases.Prestamo;
import clases.Categoria;
import clases.Usuario;
import clases.Autor;
import clases.DetallePrestamo;
import colecciones.DatosLibro;
import colecciones.DatosPrestamo;
import colecciones.DatosDetallePrestamo;
import repository.ArchivoTextoRepository;
import repository.Formatos;
import service.PrestamoService;
import repository.TransaccionArchivos;
import util.Codigos;
import util.DatosLibroDetalle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/** Pruebas sin dependencias externas; todas las escrituras ocurren en carpetas temporales. */
public class PruebasBiblioteca {
    private static final String[] DATOS = {"usuarios.txt", "libros.txt", "categorias.txt",
            "prestamos.txt", "detallePrestamo.txt"};

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static Path copiarDatos(Path origen) throws IOException {
        Path copia = Files.createTempDirectory("biblioteca-pruebas-");
        Path destino = Files.createDirectory(copia.resolve("datos"));
        for (String nombre : DATOS) Files.copy(origen.resolve("datos").resolve(nombre), destino.resolve(nombre));
        return copia;
    }

    public static void main(String[] args) throws Exception {
        Path origen = Path.of(args[0]);
        probarAutores(origen);
        probarPrestamoMultiple(origen);
        Path copia = copiarDatos(origen);
        System.setProperty("biblioteca.home", copia.toString());
        BibliotecaContexto contexto = new BibliotecaContexto();
        comprobar(contexto.prestamos().obtener("P20") != null, "Se perdió P20");
        comprobar(contexto.prestamos().detallesDe("P20").isEmpty(), "P20 se alteró");

        comprobar("L1010".equals(Codigos.siguiente("L", 0, 10, List.of("L10", "L1000"))),
                "Código con huecos incorrecto");
        Libro libro = contexto.libros().obtener(0);
        Libro otroObjeto = new Libro(libro.getCodLibro(), libro.getTitulo(), libro.getAutor(), libro.getIsnb(),
                libro.getCodCategoria(), libro.getAnioPublicacion(), libro.getCantidadDisponible(),
                libro.getCantidadTotal(), libro.getRutaPortada());
        DatosLibroDetalle seleccion = new DatosLibroDetalle();
        seleccion.actualizarLista(libro);
        seleccion.actualizarLista(otroObjeto);
        comprobar(seleccion.longitud() == 1 && seleccion.obtener(0).getCantidad() == 2,
                "La selección no compara códigos por contenido");
        seleccion.eliminar(otroObjeto);
        comprobar(seleccion.obtener(0).getCantidad() == 1, "No se pudo quitar un libro equivalente");

        byte[] prestamosAntes = Files.readAllBytes(copia.resolve("datos/prestamos.txt"));
        int disponible = libro.getCantidadDisponible();
        for (int i = 0; i <= disponible; i++) contexto.prestamos().seleccionar(libro);
        try {
            contexto.prestamos().registrar(contexto.usuarios().obtener(0), LocalDate.now(), LocalDate.now().plusDays(5));
            throw new AssertionError("Se permitió un préstamo sin existencias");
        } catch (IllegalArgumentException esperado) { }
        comprobar(disponible == libro.getCantidadDisponible(), "El rechazo modificó el inventario");
        comprobar(java.util.Arrays.equals(prestamosAntes, Files.readAllBytes(copia.resolve("datos/prestamos.txt"))),
                "El rechazo modificó préstamos.txt");

        contexto.prestamos().limpiarSeleccion();
        contexto.prestamos().seleccionar(libro);
        Prestamo nuevo = contexto.prestamos().registrar(contexto.usuarios().obtener(0),
                LocalDate.now(), LocalDate.now().plusDays(5));
        comprobar(disponible - 1 == libro.getCantidadDisponible(), "No se descontó inventario");
        BibliotecaContexto recargado = new BibliotecaContexto();
        comprobar(recargado.prestamos().obtener(nuevo.getCodPrestamo()) != null, "No persistió el préstamo");
        comprobar(recargado.prestamos().detallesDe(nuevo.getCodPrestamo()).size() == 1, "No persistió el detalle");
        comprobar(recargado.libros().obtener(0).getCantidadDisponible() == disponible - 1, "No persistió el stock");
        comprobar(recargado.prestamos().obtener("P20") != null
                && recargado.prestamos().detallesDe("P20").isEmpty(), "Se alteró P20");

        Categoria nuevaCategoria = new Categoria("Categoría de prueba");
        recargado.categorias().agregar(nuevaCategoria);
        recargado.autores().registrar("Autor de prueba");
        Libro nuevoLibro = new Libro("Libro de prueba", "Autor de prueba", "ISBN-PRUEBA",
                nuevaCategoria.getCodCategoria(), 2026, 2, 2, "imagenes/portadas/sin_imagen.png");
        recargado.libros().agregar(nuevoLibro);
        Usuario nuevoUsuario = new Usuario("Ana Prueba", "12345678", "ana@ejemplo.com", "912345678", "Registrado");
        recargado.usuarios().agregar(nuevoUsuario);
        recargado.usuarios().guardar();
        BibliotecaContexto conAltas = new BibliotecaContexto();
        comprobar(conAltas.categorias().listar().stream().anyMatch(c -> c.getCodCategoria().equals(nuevaCategoria.getCodCategoria())),
                "No persistió categoría");
        comprobar(conAltas.libros().listar().stream().anyMatch(l -> l.getCodLibro().equals(nuevoLibro.getCodLibro())),
                "No persistió libro");
        comprobar(conAltas.usuarios().buscar(nuevoUsuario.getCodUser()) != null, "No persistió usuario");

        Categoria categoriaEditada = new Categoria(nuevaCategoria.getCodCategoria(), "Categoría editada");
        conAltas.categorias().actualizar(categoriaEditada);
        comprobar("Categoría editada".equals(conAltas.categorias().nombreDe(nuevaCategoria.getCodCategoria())),
                "No se actualizó categoría");
        Libro libroEditado = new Libro(nuevoLibro.getCodLibro(), "Libro editado", nuevoLibro.getAutor(),
                nuevoLibro.getIsnb(), nuevaCategoria.getCodCategoria(), 2026, 2, 2, nuevoLibro.getRutaPortada());
        conAltas.libros().actualizar(libroEditado);
        comprobar("Libro editado".equals(conAltas.libros().listar().stream()
                .filter(l -> l.getCodLibro().equals(nuevoLibro.getCodLibro())).findFirst().orElseThrow().getTitulo()),
                "No se actualizó libro");
        Usuario usuarioEditado = new Usuario(nuevoUsuario.getCodUser(), "Ana Editada", "12345678",
                "ana@ejemplo.com", "912345678", "Registrado");
        conAltas.usuarios().actualizar(usuarioEditado);
        conAltas.usuarios().guardar();
        comprobar("Ana Editada".equals(new BibliotecaContexto().usuarios().buscar(nuevoUsuario.getCodUser()).getNomUser()),
                "No se actualizó usuario");
        conAltas.usuarios().eliminar(nuevoUsuario.getCodUser());
        conAltas.usuarios().guardar();
        conAltas.libros().eliminar(nuevoLibro.getCodLibro());
        conAltas.categorias().eliminar(nuevaCategoria.getCodCategoria());
        BibliotecaContexto conBajas = new BibliotecaContexto();
        comprobar(conBajas.usuarios().buscar(nuevoUsuario.getCodUser()) == null, "No se eliminó usuario");
        comprobar(conBajas.libros().listar().stream().noneMatch(l -> l.getCodLibro().equals(nuevoLibro.getCodLibro())),
                "No se eliminó libro");
        comprobar(conBajas.categorias().listar().stream().noneMatch(c -> c.getCodCategoria().equals(nuevaCategoria.getCodCategoria())),
                "No se eliminó categoría");

        Path imagenOrigen = Files.writeString(copia.resolve("portada.png"), "imagen de prueba");
        String primeraRuta = conAltas.portadas().guardar(imagenOrigen);
        String segundaRuta = conAltas.portadas().guardar(imagenOrigen);
        comprobar(!primeraRuta.equals(segundaRuta), "Se sobrescribió una portada existente");
        comprobar(Files.exists(copia.resolve(primeraRuta)) && Files.exists(copia.resolve(segundaRuta)),
                "No se guardaron las portadas");

        byte[] detalleAntes = Files.readAllBytes(copia.resolve("datos/detallePrestamo.txt"));
        try {
            TransaccionArchivos.ejecutar(() -> {
                try { Files.writeString(copia.resolve("datos/detallePrestamo.txt"), "fallo"); }
                catch (IOException error) { throw new IllegalStateException(error); }
                throw new IllegalStateException("fallo simulado");
            }, "prestamos.txt", "detallePrestamo.txt", "libros.txt");
            throw new AssertionError("No se detectó el fallo simulado");
        } catch (IllegalStateException esperado) { }
        comprobar(java.util.Arrays.equals(detalleAntes, Files.readAllBytes(copia.resolve("datos/detallePrestamo.txt"))),
                "No se restauró el archivo tras el fallo");

        for (String nombre : new String[] {"prestamos.txt", "detallePrestamo.txt", "libros.txt"}) {
            Files.copy(copia.resolve("datos").resolve(nombre),
                    copia.resolve("datos").resolve(nombre + ".prestamo.bak"));
        }
        Files.writeString(copia.resolve("datos/.prestamo-pendiente"), "pendiente");
        Files.writeString(copia.resolve("datos/prestamos.txt"), "escritura interrumpida");
        BibliotecaContexto recuperado = new BibliotecaContexto();
        comprobar(recuperado.prestamos().obtener("P20") != null, "No se recuperó una escritura interrumpida");
        comprobar(!Files.exists(copia.resolve("datos/.prestamo-pendiente")), "Quedó el marcador pendiente");

        Path corrupta = copiarDatos(origen);
        Files.writeString(corrupta.resolve("datos/libros.txt"), "registro-inválido");
        System.setProperty("biblioteca.home", corrupta.toString());
        try { new BibliotecaContexto(); throw new AssertionError("Se aceptó un archivo corrupto"); }
        catch (IllegalStateException esperado) { }
        comprobar("registro-inválido".equals(Files.readString(corrupta.resolve("datos/libros.txt"))),
                "Se sobrescribió un archivo corrupto");
        System.out.println("OK: autores, asociaciones, prestamos multiples, formatos, CRUD, stock y rollback");
    }

    private static void probarAutores(Path origen) throws Exception {
        Path copia = copiarDatos(origen);
        System.setProperty("biblioteca.home", copia.toString());
        BibliotecaContexto contexto = new BibliotecaContexto();
        comprobar(!Files.exists(copia.resolve("datos/autores.txt")), "El arranque modificó los datos históricos");
        Autor gabriel = contexto.autores().autorDeLibro("L10");
        comprobar(gabriel != null && gabriel.getCodigo().equals(contexto.autores().autorDeLibro("L90").getCodigo()),
                "Los libros históricos del mismo autor quedaron duplicados");
        try { contexto.autores().registrar("  GABRIEL   GARCÍA MÁRQUEZ  ");
            throw new AssertionError("Se permitió un autor duplicado");
        } catch (IllegalArgumentException esperado) { }
        contexto.autores().modificar(gabriel.getCodigo(), "Gabriel G. Márquez");
        comprobar("Gabriel G. Márquez".equals(contexto.libros().buscar("Cien años").get(0).getAutor()),
                "El nombre no se propagó al libro");
        BibliotecaContexto recargado = new BibliotecaContexto();
        comprobar("Gabriel G. Márquez".equals(recargado.autores().autorDeLibro("L90").getNombre()),
                "No persistió la asociación");
        comprobar("Gabriel G. Márquez".equals(recargado.libros().buscar("Crónica").get(0).getAutor()),
                "No persistió la actualización del libro");
        try { recargado.autores().eliminar(gabriel.getCodigo());
            throw new AssertionError("Se eliminó un autor vinculado");
        } catch (IllegalArgumentException esperado) { }
        Autor libre = recargado.autores().registrar("  Autor   Nuevo ");
        comprobar("Autor Nuevo".equals(libre.getNombre()), "No se normalizaron espacios");
        try { recargado.autores().modificar(libre.getCodigo(), "  GABRIEL G. MÁRQUEZ ");
            throw new AssertionError("Se permitió renombrar a un autor duplicado");
        } catch (IllegalArgumentException esperado) { }
        Libro asociado = new Libro("Libro de autor", libre.getNombre(), "ISBN-AUTOR", "C10",
                2026, 2, 2, "imagenes/portadas/sin_imagen.png");
        recargado.libros().agregar(asociado);
        comprobar(libre.getCodigo().equals(new BibliotecaContexto().autores().autorDeLibro(asociado.getCodLibro()).getCodigo()),
                "No persistió el vínculo del libro nuevo");
        try { recargado.autores().eliminar(libre.getCodigo());
            throw new AssertionError("Se eliminó un autor con libro nuevo");
        } catch (IllegalArgumentException esperado) { }
        recargado.libros().eliminar(asociado.getCodLibro());
        recargado.autores().eliminar(libre.getCodigo());
        comprobar(new BibliotecaContexto().autores().obtener(libre.getCodigo()) == null,
                "No se eliminó el autor libre");
        byte[] autoresAntes = Files.readAllBytes(copia.resolve("datos/autores.txt"));
        byte[] relacionesAntes = Files.readAllBytes(copia.resolve("datos/libroAutor.txt"));
        byte[] catalogoAntes = Files.readAllBytes(copia.resolve("datos/libros.txt"));
        try {
            TransaccionArchivos.ejecutarAutores(() -> {
                try {
                    Files.writeString(copia.resolve("datos/autores.txt"), "A999;Parcial");
                    Files.writeString(copia.resolve("datos/libros.txt"), "parcial");
                } catch (IOException error) { throw new IllegalStateException(error); }
                throw new IllegalStateException("fallo tras dos escrituras");
            }, "autores.txt", "libroAutor.txt", "libros.txt");
            throw new AssertionError("Se aceptó escritura parcial con archivos existentes");
        } catch (IllegalStateException esperado) { }
        comprobar(java.util.Arrays.equals(autoresAntes, Files.readAllBytes(copia.resolve("datos/autores.txt")))
                && java.util.Arrays.equals(relacionesAntes, Files.readAllBytes(copia.resolve("datos/libroAutor.txt")))
                && java.util.Arrays.equals(catalogoAntes, Files.readAllBytes(copia.resolve("datos/libros.txt"))),
                "No se restauraron los tres archivos relacionados");

        Path fallo = copiarDatos(origen);
        System.setProperty("biblioteca.home", fallo.toString());
        try {
            TransaccionArchivos.ejecutarAutores(() -> {
                try { Files.writeString(fallo.resolve("datos/autores.txt"), "A10;Fallido"); }
                catch (IOException error) { throw new IllegalStateException(error); }
                throw new IllegalStateException("fallo simulado");
            }, "autores.txt", "libroAutor.txt", "libros.txt");
            throw new AssertionError("Se aceptó escritura parcial de autores");
        } catch (IllegalStateException esperado) { }
        comprobar(!Files.exists(fallo.resolve("datos/autores.txt"))
                && !Files.exists(fallo.resolve("datos/libroAutor.txt")),
                "No se revirtieron archivos nuevos");
        byte[] librosAntes = Files.readAllBytes(fallo.resolve("datos/libros.txt"));
        Files.copy(fallo.resolve("datos/libros.txt"), fallo.resolve("datos/libros.txt.autores.bak"));
        Files.writeString(fallo.resolve("datos/.autores-pendiente"), "libros.txt;true\nautores.txt;false\nlibroAutor.txt;false\n");
        Files.writeString(fallo.resolve("datos/libros.txt"), "fallo");
        new BibliotecaContexto();
        comprobar(java.util.Arrays.equals(librosAntes, Files.readAllBytes(fallo.resolve("datos/libros.txt"))),
                "No se recuperó una escritura interrumpida de autores");
    }

    private static void probarPrestamoMultiple(Path origen) throws Exception {
        Path copia = copiarDatos(origen);
        System.setProperty("biblioteca.home", copia.toString());
        BibliotecaContexto contexto = new BibliotecaContexto();
        Libro primero = contexto.libros().obtener(0);
        Libro segundo = contexto.libros().obtener(1);
        int antesPrimero = primero.getCantidadDisponible();
        int antesSegundo = segundo.getCantidadDisponible();
        contexto.prestamos().seleccionar(primero);
        contexto.prestamos().seleccionar(primero);
        contexto.prestamos().seleccionar(segundo);
        contexto.prestamos().seleccionar(segundo);
        contexto.prestamos().seleccionar(segundo);
        Prestamo registrado = contexto.prestamos().registrar(contexto.usuarios().obtener(0),
                LocalDate.now(), LocalDate.now().plusDays(5));
        BibliotecaContexto recargado = new BibliotecaContexto();
        comprobar(recargado.prestamos().detallesDe(registrado.getCodPrestamo()).size() == 2,
                "No se registraron ambos libros");
        comprobar(recargado.prestamos().detallesDe(registrado.getCodPrestamo()).get(0).getCantPrestamo() == 2
                && recargado.prestamos().detallesDe(registrado.getCodPrestamo()).get(1).getCantPrestamo() == 3,
                "Cantidades de detalle incorrectas");
        comprobar(recargado.libros().obtener(0).getCantidadDisponible() == antesPrimero - 2
                && recargado.libros().obtener(1).getCantidadDisponible() == antesSegundo - 3,
                "Inventario de varios libros incorrecto");
        comprobar(recargado.prestamos().detallesDe("P20").isEmpty(), "Se alteró P20");

        Path fallo = copiarDatos(origen);
        System.setProperty("biblioteca.home", fallo.toString());
        DatosLibro libros = new DatosLibro(new ArchivoTextoRepository<>(fallo.resolve("datos/libros.txt"),
                Formatos::leerLibro, Formatos::escribirLibro));
        DatosPrestamo prestamos = new DatosPrestamo(new ArchivoTextoRepository<>(fallo.resolve("datos/prestamos.txt"),
                Formatos::leerPrestamo, Formatos::escribirPrestamo));
        DatosDetallePrestamo detalles = new DatosDetallePrestamo(
                new ArchivoTextoRepository<>(fallo.resolve("datos/detallePrestamo.txt"),
                        Formatos::leerDetalle, Formatos::escribirDetalle), prestamos, libros) {
            private int llamadas;
            @Override public String agregar(String codLibro, String codPrestamo, int cantidad) {
                if (++llamadas == 2) throw new IllegalStateException("fallo en segundo libro");
                return super.agregar(codLibro, codPrestamo, cantidad);
            }
        };
        PrestamoService servicio = new PrestamoService(prestamos, detalles, libros);
        Libro uno = libros.obtener(0), dos = libros.obtener(1);
        int stockUno = uno.getCantidadDisponible(), stockDos = dos.getCantidadDisponible();
        int prestamosAntes = prestamos.longitud(), detallesAntes = detalles.longitud();
        servicio.seleccionar(uno); servicio.seleccionar(uno); servicio.seleccionar(dos);
        try { servicio.registrar(new BibliotecaContexto().usuarios().obtener(0),
                LocalDate.now(), LocalDate.now().plusDays(5));
            throw new AssertionError("No falló el segundo libro");
        } catch (IllegalStateException esperado) { }
        comprobar(uno.getCantidadDisponible() == stockUno && dos.getCantidadDisponible() == stockDos,
                "La recuperación parcial alteró las existencias");
        comprobar(prestamos.longitud() == prestamosAntes && detalles.longitud() == detallesAntes,
                "La recuperación parcial dejó registros en memoria");
        try { detalles.agregar("INEXISTENTE", "P10", 1); throw new AssertionError("Libro inválido"); }
        catch (IllegalArgumentException esperado) { }
        try { detalles.agregar("L10", "INEXISTENTE", 1); throw new AssertionError("Préstamo inválido"); }
        catch (IllegalArgumentException esperado) { }
        try { detalles.agregar("L10", "P10", 0); throw new AssertionError("Cantidad inválida"); }
        catch (IllegalArgumentException esperado) { }
        try { prestamos.agregar(prestamos.obtener(0)); throw new AssertionError("ID duplicado"); }
        catch (IllegalArgumentException esperado) { }
    }
}

package repository;

import util.Rutas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/** Restaura los archivos previos si falla el guardado coordinado. */
public final class TransaccionArchivos {
    private static final String MARCADOR = ".prestamo-pendiente";
    private static final String[] ARCHIVOS = {"prestamos.txt", "detallePrestamo.txt", "libros.txt"};
    private TransaccionArchivos() {}

    /** Ante un cierre inesperado, restaura el estado anterior al último préstamo. */
    public static void recuperarPendiente() {
        recuperarAutores();
        Path marcador = Rutas.dato(MARCADOR);
        if (!Files.exists(marcador)) return;
        try {
            for (String nombre : ARCHIVOS) {
                Path respaldo = Rutas.dato(nombre + ".prestamo.bak");
                if (!Files.exists(respaldo)) throw new IOException("Falta respaldo de " + nombre);
            }
            for (String nombre : ARCHIVOS) {
                Files.copy(Rutas.dato(nombre + ".prestamo.bak"), Rutas.dato(nombre),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            Files.delete(marcador);
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo recuperar un préstamo interrumpido; revise los respaldos", error);
        }
        try { limpiarRespaldos(); } catch (IOException ignored) { }
    }

    /** Operaciones de autores y libros; admite archivos nuevos sin tocar datos al arrancar. */
    public static void ejecutarAutores(Runnable operacion, String... nombres) {
        Path marcador = Rutas.dato(".autores-pendiente");
        if (Files.exists(marcador)) throw new IllegalStateException("Existe una operación de autores pendiente");
        ArrayList<String> estado = new ArrayList<>();
        try {
            for (String nombre : nombres) {
                Path archivo = Rutas.dato(nombre);
                boolean existe = Files.exists(archivo);
                estado.add(nombre + ";" + existe);
                if (existe) Files.copy(archivo, Rutas.dato(nombre + ".autores.bak"), StandardCopyOption.REPLACE_EXISTING);
                else Files.deleteIfExists(Rutas.dato(nombre + ".autores.bak"));
            }
            Files.write(marcador, estado, StandardCharsets.UTF_8);
            operacion.run();
            Files.delete(marcador);
        } catch (Exception error) {
            try { recuperarAutores(); }
            catch (RuntimeException recuperacion) { error.addSuppressed(recuperacion); }
            throw new IllegalStateException("No se pudo guardar autores y libros", error);
        }
        for (String nombre : nombres) {
            try { Files.deleteIfExists(Rutas.dato(nombre + ".autores.bak")); }
            catch (IOException ignored) { }
        }
    }

    private static void recuperarAutores() {
        Path marcador = Rutas.dato(".autores-pendiente");
        if (!Files.exists(marcador)) return;
        try {
            ArrayList<String> estado = new ArrayList<>(Files.readAllLines(marcador, StandardCharsets.UTF_8));
            for (String linea : estado) {
                String[] campos = linea.split(";", -1);
                if (campos.length != 2 || (!"true".equals(campos[1]) && !"false".equals(campos[1])))
                    throw new IOException("Marcador de autores inválido");
                if ("true".equals(campos[1]) && !Files.exists(Rutas.dato(campos[0] + ".autores.bak")))
                    throw new IOException("Falta respaldo de " + campos[0]);
            }
            for (String linea : estado) {
                String[] campos = linea.split(";", -1);
                Path archivo = Rutas.dato(campos[0]);
                if ("true".equals(campos[1]))
                    Files.copy(Rutas.dato(campos[0] + ".autores.bak"), archivo, StandardCopyOption.REPLACE_EXISTING);
                else Files.deleteIfExists(archivo);
            }
            Files.delete(marcador);
            for (String linea : estado) {
                String nombre = linea.split(";", -1)[0];
                Files.deleteIfExists(Rutas.dato(nombre + ".autores.bak"));
            }
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo recuperar autores; revise los respaldos", error);
        }
    }

    private static void limpiarRespaldos() throws IOException {
        for (String nombre : ARCHIVOS) Files.deleteIfExists(Rutas.dato(nombre + ".prestamo.bak"));
    }

    public static void ejecutar(Runnable operacion, String... nombres) {
        ArrayList<byte[]> originales = new ArrayList<>();
        ArrayList<Path> rutas = new ArrayList<>();
        Path marcador = Rutas.dato(MARCADOR);
        try {
            for (String nombre : nombres) {
                Path ruta = Rutas.dato(nombre);
                rutas.add(ruta);
                originales.add(Files.readAllBytes(ruta));
            }
            for (int i = 0; i < rutas.size(); i++) {
                Files.write(Rutas.dato(nombres[i] + ".prestamo.bak"), originales.get(i));
            }
            Files.writeString(marcador, "pendiente", StandardCharsets.UTF_8);
            operacion.run();
            Files.delete(marcador);
        } catch (Exception error) {
            for (int i = 0; i < originales.size(); i++) {
                try { Files.write(rutas.get(i), originales.get(i)); }
                catch (IOException recuperacion) { error.addSuppressed(recuperacion); }
            }
            try { Files.deleteIfExists(marcador); limpiarRespaldos(); }
            catch (IOException recuperacion) { error.addSuppressed(recuperacion); }
            throw new IllegalStateException("No se pudo guardar el préstamo completo", error);
        }
        try { limpiarRespaldos(); } catch (IOException ignored) { }
    }
}

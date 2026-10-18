package util;

import java.nio.file.Files;
import java.nio.file.Path;

/** Ubicación de los datos e imágenes, independiente del directorio de ejecución. */
public final class Rutas {
    private Rutas() {}

    public static Path raiz() {
        String configurada = System.getProperty("biblioteca.home");
        if (configurada != null && !configurada.isBlank()) {
            Path raiz = Path.of(configurada).toAbsolutePath().normalize();
            if (!Files.isDirectory(raiz.resolve("datos"))) {
                throw new IllegalStateException("No existe la carpeta datos en " + raiz);
            }
            return raiz;
        }
        Path actual = Path.of("").toAbsolutePath().normalize();
        for (Path carpeta = actual; carpeta != null; carpeta = carpeta.getParent()) {
            if (Files.isDirectory(carpeta.resolve("datos"))) return carpeta;
            Path biblioteca = carpeta.resolve("Biblioteca");
            if (Files.isDirectory(biblioteca.resolve("datos"))) return biblioteca;
        }
        throw new IllegalStateException("No se encontró Biblioteca/datos. Configure -Dbiblioteca.home=<ruta de Biblioteca>.");
    }

    public static Path dato(String nombre) {
        return raiz().resolve("datos").resolve(nombre);
    }

    public static Path imagen(String rutaGuardada) {
        Path ruta = Path.of(rutaGuardada.replace('\\', '/'));
        return ruta.isAbsolute() ? ruta : raiz().resolve(ruta).normalize();
    }
}

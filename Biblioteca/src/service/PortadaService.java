package service;

import util.Rutas;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Guarda portadas sin sobrescribir imágenes existentes de otros libros. */
public final class PortadaService {
    public String guardar(Path origen) throws IOException {
        Path carpeta = Rutas.raiz().resolve("imagenes").resolve("portadas");
        Files.createDirectories(carpeta);
        Path destino = carpeta.resolve(origen.getFileName());
        if (!origen.toAbsolutePath().normalize().equals(destino.toAbsolutePath().normalize())) {
            String nombre = origen.getFileName().toString();
            int punto = nombre.lastIndexOf('.');
            String base = punto < 0 ? nombre : nombre.substring(0, punto);
            String extension = punto < 0 ? "" : nombre.substring(punto);
            int indice = 1;
            while (Files.exists(destino)) destino = carpeta.resolve(base + "_" + indice++ + extension);
            Files.copy(origen, destino);
        }
        return "imagenes/portadas/" + destino.getFileName();
    }
}

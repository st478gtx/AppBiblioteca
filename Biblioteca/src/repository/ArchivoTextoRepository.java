package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.function.Function;

/** Persistencia de líneas con separador histórico y sustitución segura de un archivo. */
public final class ArchivoTextoRepository<T> implements Repositorio<T> {
    private final Path archivo;
    private final Function<String, T> leer;
    private final Function<T, String> escribir;

    public ArchivoTextoRepository(Path archivo, Function<String, T> leer, Function<T, String> escribir) {
        this.archivo = archivo;
        this.leer = leer;
        this.escribir = escribir;
    }

    @Override
    public ArrayList<T> cargar() {
        ArrayList<T> resultado = new ArrayList<>();
        try {
            int numero = 0;
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                numero++;
                try { resultado.add(leer.apply(linea)); }
                catch (RuntimeException error) {
                    throw new IllegalStateException("Registro inválido en " + archivo + ", línea " + numero, error);
                }
            }
            return resultado;
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo leer " + archivo, error);
        }
    }

    @Override
    public void guardar(ArrayList<T> registros) {
        Path temporal = null;
        try {
            ArrayList<String> lineas = new ArrayList<>();
            for (T registro : registros) lineas.add(escribir.apply(registro));
            temporal = Files.createTempFile(archivo.getParent(), archivo.getFileName().toString(), ".tmp");
            Files.write(temporal, lineas, StandardCharsets.UTF_8);
            try {
                Files.move(temporal, archivo, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException error) {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo guardar " + archivo, error);
        } finally {
            if (temporal != null) {
                try { Files.deleteIfExists(temporal); } catch (IOException ignored) { }
            }
        }
    }
}

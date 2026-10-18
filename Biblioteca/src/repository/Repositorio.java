package repository;

import java.util.ArrayList;

/** Contrato de persistencia de una colección. */
public interface Repositorio<T> {
    ArrayList<T> cargar();
    void guardar(ArrayList<T> registros);
}

package service;

import clases.Libro;
import colecciones.DatosLibro;
import java.util.ArrayList;

public final class LibroService {
    private final DatosLibro datos;
    private final AutorService autores;
    public LibroService(DatosLibro datos, AutorService autores) { this.datos = datos; this.autores = autores; }
    public ArrayList<Libro> listar() { return datos.listar(); }
    public ArrayList<Libro> buscar(String texto) { return new ArrayList<>(datos.buscar(texto)); }
    public int longitud() { return datos.longitud(); }
    public Libro obtener(int indice) { return datos.obtener(indice); }
    public String agregar(Libro libro) {
        autores.agregarLibro(libro); return "Libro agregado";
    }
    public String actualizar(Libro libro) {
        autores.actualizarLibro(libro); return "Libro actualizado.";
    }
    public String eliminar(String codigo) {
        autores.eliminarLibro(codigo); return "Registro de libro eliminado.";
    }
}

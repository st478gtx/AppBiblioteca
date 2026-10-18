package service;

import clases.Categoria;
import colecciones.DatosCategoria;
import java.util.ArrayList;

public final class CategoriaService {
    private final DatosCategoria datos;
    public CategoriaService(DatosCategoria datos) { this.datos = datos; }
    public ArrayList<Categoria> listar() { return datos.listar(); }
    public ArrayList<Categoria> buscar(String texto) { return new ArrayList<>(datos.buscar(texto)); }
    public int longitud() { return datos.longitud(); }
    public Categoria obtener(int indice) { return datos.obtener(indice); }
    public String nombreDe(String codigo) { return datos.obtenerNombreCategoria(codigo); }
    public String agregar(Categoria categoria) {
        ArrayList<Categoria> antes = datos.listar();
        try { String r = datos.agregar(categoria); datos.guardar(); return r; }
        catch (RuntimeException error) { datos.restaurar(antes); throw error; }
    }
    public String actualizar(Categoria categoria) {
        ArrayList<Categoria> antes = datos.listar();
        try { String r = datos.actualizar(categoria); datos.guardar(); return r; }
        catch (RuntimeException error) { datos.restaurar(antes); throw error; }
    }
    public String eliminar(String codigo) {
        ArrayList<Categoria> antes = datos.listar();
        try { String r = datos.remover(codigo); datos.guardar(); return r; }
        catch (RuntimeException error) { datos.restaurar(antes); throw error; }
    }
}

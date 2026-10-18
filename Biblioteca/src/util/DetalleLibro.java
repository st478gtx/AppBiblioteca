package util;

import clases.Libro;

public class DetalleLibro {

    private Libro libro;
    private int cantidad = 0;       

    public DetalleLibro(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }
    
    public DetalleLibro(Libro libro) {
    	this.libro = libro;
    	cantidad++;
    }
    
    public DetalleLibro() {}

    public Libro getLibro() {
        return libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

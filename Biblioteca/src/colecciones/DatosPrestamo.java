package colecciones;

import java.util.ArrayList;

import clases.Prestamo;

public class DatosPrestamo {
	public ArrayList<Prestamo> prestamos = new ArrayList<>();
	
	public DatosPrestamo() {
		
	}
	
	public int longitug() {
		return prestamos.size();
	}
	
	public Prestamo obtener(int i) {
		return prestamos.get(i);
	}
	
}

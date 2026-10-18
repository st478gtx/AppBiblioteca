package util;

import java.util.ArrayList;

import clases.Libro;

public class DatosLibroDetalle {

	private final ArrayList<DetalleLibro> detalleLibros = new ArrayList<>();

	public ArrayList<DetalleLibro> listar() { return new ArrayList<>(detalleLibros); }
	public void limpiar() { detalleLibros.clear(); }

	public int longitud() {
		return detalleLibros.size();
	}

	public DetalleLibro obtener(int i) {
		return detalleLibros.get(i);
	}

	public void agregar(Libro l) {

		DetalleLibro dl = new DetalleLibro(l, 1);

		detalleLibros.add(dl);
	}

	public void actualizarLista(Libro l) {

		for (int i = 0; i < longitud(); i++) {
			DetalleLibro dl = obtener(i);

			if (dl.getLibro().getCodLibro().equals(l.getCodLibro())) {
				dl.setCantidad(dl.getCantidad() + 1);
				return;
			}
		}

		agregar(l);
	}

	public void eliminar(Libro l) {
		for (int i = 0; i < longitud(); i++) {
			DetalleLibro dl = obtener(i);

			if (dl.getLibro().getCodLibro().equals(l.getCodLibro())) {
				if (dl.getCantidad() <= 1) {
					detalleLibros.remove(i);
				} else {
					dl.setCantidad(dl.getCantidad() - 1);
					return;
				}
			}
		}
	}
}

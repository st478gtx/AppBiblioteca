package colecciones;

import java.util.ArrayList;

import clases.DetalleLibro;
import clases.Libro;

public class DatosLibroDetalle {

	public ArrayList<DetalleLibro> detalleLibros = new ArrayList<>();

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

			if (dl.getLibro().getCodLibro() == l.getCodLibro()) {
				dl.setCantidad(dl.getCantidad() + 1);
				return;
			}
		}

		agregar(l);
	}

	public void eliminar(Libro l) {
		for (int i = 0; i < longitud(); i++) {
			DetalleLibro dl = obtener(i);

			if (dl.getLibro().getCodLibro() == l.getCodLibro()) {
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

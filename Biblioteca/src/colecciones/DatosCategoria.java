package colecciones;

import java.util.ArrayList;

import clases.Categoria;

public class DatosCategoria {
	public ArrayList<Categoria> categorias = new ArrayList<>();

	public DatosCategoria() {
		categorias.add(new Categoria("Ciencia ficción"));
		categorias.add(new Categoria("Fantasía"));
		categorias.add(new Categoria("Literatura"));
		categorias.add(new Categoria("Novela"));
		categorias.add(new Categoria("Romance"));
	}

	public int longitud() {
		return categorias.size();
	}

	public Categoria obtener(int i) {
		return categorias.get(i);
	}

	public String obtenerNombreCategoria(int codCategoria) {

		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodCategoria() == codCategoria) {
				return obtener(i).getNombre();
			}
		}

		return "Sin categoría";
	}

	public int obtenerCodCategoria(String nombre) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getNombre() == nombre) {
				return obtener(i).getCodCategoria();
			}
		}
		return -1;
	}
}

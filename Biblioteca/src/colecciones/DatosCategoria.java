package colecciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import clases.Categoria;

public class DatosCategoria {
	public ArrayList<Categoria> categorias = new ArrayList<>();
	public ArrayList<Categoria> categoriaAgregado = new ArrayList<>();

	public DatosCategoria() {

		recuperar();

		if (categorias.isEmpty()) {

			categorias.add(new Categoria("Ciencia ficción"));
			categorias.add(new Categoria("Fantasía"));
			categorias.add(new Categoria("Literatura"));
			categorias.add(new Categoria("Novela"));
			categorias.add(new Categoria("Romance"));

			guardar();
		}
	}

	public int longitud() {
		return categorias.size();
	}

	public Categoria obtener(int i) {
		return categorias.get(i);
	}

	public String agregar(Categoria cat) {
		categorias.add(cat);
		return "Registro de categoría agregado";
	}

	public String actualizar(Categoria reg) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodCategoria().equalsIgnoreCase(reg.getCodCategoria())) {
				categorias.set(i, reg);
				return "Registro de categoría actualizado";
			}
		}
		return "Categoria no encontrada";
	}

	public String remover(String codCategoria) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodCategoria().equalsIgnoreCase(codCategoria)) {
				categorias.remove(i);
				return "Registro de categoría eliminado";
			}
		}
		return "Categoria no encontrada";
	}

	public String obtenerNombreCategoria(String codCategoria) {

		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodCategoria().equals(codCategoria)) {
				return obtener(i).getNombre();
			}
		}

		return "Sin categoría";
	}

	public String obtenerCodCategoria(String nombre) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getNombre() == nombre) {
				return obtener(i).getCodCategoria();
			}
		}
		return "";
	}
	
	public ArrayList<Categoria> buscar(String busqueda) {

		categoriaAgregado.clear();

		Categoria l = new Categoria();

		busqueda = DatosLibro.quitarTildes(busqueda).toLowerCase();

		for (int i = 0; i < longitud(); i++) {
			l = obtener(i);

			if (DatosLibro.quitarTildes(l.getNombre().toLowerCase()).contains(busqueda)) {

				// Agrega categorias a la busqueda
				categoriaAgregado.add(l);
			}
		}
		return categoriaAgregado;
	}

	public String guardar() {

		String mensaje = "";

		if (longitud() == 0) {

			mensaje = "Registre una categoría para guardarlo";

		} else {
			try {

				PrintWriter categoria = new PrintWriter(new FileWriter("datos\\categorias.txt"));

				for (int i = 0; i < longitud(); i++) {

					Categoria c = obtener(i);

					categoria.println(c.getCodCategoria() + ";" + c.getNombre());
				}

				categoria.close();

				mensaje = "Se ha guardado:" + longitud() + " registros de categoria";

			} catch (Exception ex) {
				mensaje = ex.getMessage();
			}
		}
		return mensaje;
	}

	public void recuperar() {
		categorias.clear();
		try {

			BufferedReader categoria = new BufferedReader(new FileReader("datos\\categorias.txt"));
			String linea;

			while ((linea = categoria.readLine()) != null) {

				String[] arreglo = linea.split(";");

				Categoria c = new Categoria(arreglo[0], arreglo[1]);

				categorias.add(c);
			}

			categoria.close();
		} catch (Exception ex) {
			return;
		}
	}
}

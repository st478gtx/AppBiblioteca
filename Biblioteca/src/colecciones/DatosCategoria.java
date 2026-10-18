package colecciones;

import java.util.ArrayList;

import clases.Categoria;
import util.Codigos;
import repository.Repositorio;

public class DatosCategoria {
	private final Repositorio<Categoria> repositorio;
	private final ArrayList<Categoria> categorias = new ArrayList<>();
	public ArrayList<Categoria> listar() { return new ArrayList<>(categorias); }
	private final ArrayList<Categoria> categoriaAgregado = new ArrayList<>();
	public void restaurar(ArrayList<Categoria> anteriores) { categorias.clear(); categorias.addAll(anteriores); }

	public DatosCategoria(Repositorio<Categoria> repositorio) {
		this.repositorio = repositorio;

		recuperar();

		if (categorias.isEmpty()) {

			categorias.add(new Categoria("Ciencia ficción"));
			categorias.add(new Categoria("Fantasía"));
			categorias.add(new Categoria("Literatura"));
			categorias.add(new Categoria("Novela"));
			categorias.add(new Categoria("Romance"));

			for (Categoria categoria : categorias) asignarCodigo(categoria);
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
		asignarCodigo(cat);
		categorias.add(cat);
		return "Registro de categoría agregado";
	}

	private void asignarCodigo(Categoria categoria) {
		if (categoria.getCodCategoria() == null || categoria.getCodCategoria().isBlank()) {
			categoria.setCodCategoria(Codigos.siguiente("C", 0, 10,
					categorias.stream().map(Categoria::getCodCategoria).filter(java.util.Objects::nonNull).toList()));
		}
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
			if (obtener(i).getNombre().equals(nombre)) {
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
		repositorio.guardar(new ArrayList<>(categorias));
		return "Se han guardado " + categorias.size() + " registros de categorías";
	}

	public void recuperar() {
		ArrayList<Categoria> cargados = repositorio.cargar();
		categorias.clear();
		categorias.addAll(cargados);
	}
}

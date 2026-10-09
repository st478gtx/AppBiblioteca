package colecciones;

import java.text.Normalizer;
import java.util.ArrayList;

import clases.Libro;

public class DatosLibro {
	public ArrayList<Libro> libros = new ArrayList<>();

	public ArrayList<Libro> libroAgregado = new ArrayList<>();

	public DatosLibro() {
		libros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", "9780307474728", 4, 1967, 5, 5,
				"imagenes/portadas/cien_anos_de_soledad.jpg"));

		libros.add(new Libro("El principito", "Antoine de Saint-Exupéry", "9780156012195", 3, 1943, 8, 10,
				"imagenes/portadas/el_principito.jpg"));

		libros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "9788420412146", 4, 1605, 3, 5,
				"imagenes/portadas/don_quijote.jpg"));

		libros.add(new Libro("1984", "George Orwell", "9780451524935", 1, 1949, 6, 8, "imagenes/portadas/1984.jpg"));

		libros.add(new Libro("Orgullo y prejuicio", "Jane Austen", "9780141439518", 4, 1813, 4, 6,
				"imagenes/portadas/orgullo_y_prejuicio.jpg"));

		libros.add(new Libro("La ciudad y los perros", "Mario Vargas Llosa", "9788439720773", 4, 1963, 5, 7,
				"imagenes/portadas/la_ciudad_y_los_perros.jpg"));

		libros.add(new Libro("Harry Potter y la piedra filosofal", "J. K. Rowling", "9788478884452", 2, 1997, 10, 12,
				"imagenes/portadas/harry_potter_1.jpg"));

		libros.add(new Libro("El Hobbit", "J. R. R. Tolkien", "9780261102217", 2, 1937, 4, 5,
				"imagenes/portadas/el_hobbit.jpg"));

		libros.add(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez", "9781400034710", 4, 1981, 7,
				8, "imagenes/portadas/cronica_de_una_muerte.jpg"));

		libros.add(new Libro("Fahrenheit 451", "Ray Bradbury", "9781451678189", 1, 1953, 3, 5,
				"imagenes/portadas/fahrenheit_451.jpg"));
	}

	public Libro obtener(int i) {
		return libros.get(i);
	}

	public int longitud() {
		return libros.size();
	}

	public ArrayList<Libro> buscar(String busqueda) {

		libroAgregado.clear();

		Libro l = new Libro();

		busqueda = quitarTildes(busqueda).toLowerCase();

		for (int i = 0; i < longitud(); i++) {
			l = obtener(i);

			if (quitarTildes(l.getTitulo().toLowerCase()).contains(busqueda)
					|| quitarTildes(l.getAutor().toLowerCase()).contains(busqueda)) {

				// Agrega libros a la busqueda
				libroAgregado.add(l);
			}
		}
		return libroAgregado;
	}

	public static String quitarTildes(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
	}

	public String agregar(Libro reg) {
		if (reg != null) {
			libros.add(reg);
			return "Libro agregado";
		}
		return "Libro inválido";

	}

	public String actualizar(Libro libro) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getId() == libro.getId()) {
				libros.set(i, libro);
				return "Libro actualizado.";
			}
		}
		return "Libro no encontrado.";
	}
	
	public String eliminar(int codLibro) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getId() == codLibro) {
				libros.remove(i);
				return "Registro de libro eliminado.";
			}
		}
		return "Libro no encontrado.";
	}
}

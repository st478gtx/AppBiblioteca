package colecciones;

import java.util.ArrayList;

import clases.Libro;

public class DatosLibro {
	ArrayList<Libro> libros = new ArrayList<>();

	public DatosLibro() {
		libros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", "9780307474728", "Novela", 1967, 5, 5,
				"portadas/cien_anos_de_soledad.jpg"));

		libros.add(new Libro("El principito", "Antoine de Saint-Exupéry", "9780156012195", "Literatura", 1943, 8, 10,
				"portadas/el_principito.jpg"));

		libros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "9788420412146", "Novela", 1605, 3, 5,
				"portadas/don_quijote.jpg"));

		libros.add(new Libro("1984", "George Orwell", "9780451524935", "Ciencia ficción", 1949, 6, 8,
				"portadas/1984.jpg"));

		libros.add(new Libro("Orgullo y prejuicio", "Jane Austen", "9780141439518", "Romance", 1813, 4, 6,
				"portadas/orgullo_prejuicio.jpg"));

		libros.add(new Libro("La ciudad y los perros", "Mario Vargas Llosa", "9788439720773", "Novela", 1963, 5, 7,
				"portadas/ciudad_perros.jpg"));

		libros.add(new Libro("Harry Potter y la piedra filosofal", "J. K. Rowling", "9788478884452", "Fantasía", 1997,
				10, 12, "portadas/harry_potter_1.jpg"));

		libros.add(new Libro("El Hobbit", "J. R. R. Tolkien", "9780261102217", "Fantasía", 1937, 4, 5,
				"portadas/el_hobbit.jpg"));

		libros.add(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez", "9781400034710", "Novela",
				1981, 7, 8, "portadas/cronica_muerte.jpg"));

		libros.add(new Libro("Fahrenheit 451", "Ray Bradbury", "9781451678189", "Ciencia ficción", 1953, 3, 5,
				"portadas/fahrenheit_451.jpg"));
	}
}

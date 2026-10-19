package colecciones;

import clases.Autor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DatosAutor {
	private static final Path ARCHIVO = Files.isDirectory(Path.of("Biblioteca", "datos"))
			? Path.of("Biblioteca", "datos", "autores.txt") : Path.of("datos", "autores.txt");
	public ArrayList<Autor> autores = new ArrayList<>();

	public DatosAutor() {
		recuperar();
	}

	public int longitud() {
		return autores.size();
	}

	public Autor obtener(int i) {
		return autores.get(i);
	}

	public ArrayList<Autor> buscar(String texto) {
		ArrayList<Autor> encontrados = new ArrayList<>();
		String filtro = texto == null ? "" : texto.trim().toLowerCase();
		for (Autor autor : autores) {
			if (autor.getCodAutor().toLowerCase().contains(filtro)
					|| autor.getNombre().toLowerCase().contains(filtro)) {
				encontrados.add(autor);
			}
		}
		return encontrados;
	}

	public String agregar(String nombre) {
		String nombreValido = validarNombre(nombre);
		if (nombreRepetido(nombreValido, null)) {
			throw new IllegalArgumentException("El autor ya está registrado");
		}
		Autor autor = new Autor(siguienteCodigo(), nombreValido);
		autores.add(autor);
		return "Registro de autor agregado";
	}

	public String actualizar(Autor reg) {
		if (reg == null || reg.getCodAutor() == null) {
			throw new IllegalArgumentException("Seleccione un autor");
		}
		String nombreValido = validarNombre(reg.getNombre());
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodAutor().equalsIgnoreCase(reg.getCodAutor())) {
				if (nombreRepetido(nombreValido, reg.getCodAutor())) {
					throw new IllegalArgumentException("El autor ya está registrado");
				}
				autores.set(i, new Autor(obtener(i).getCodAutor(), nombreValido));
				return "Registro de autor actualizado";
			}
		}
		throw new IllegalArgumentException("Autor no encontrado");
	}

	public String remover(String codAutor) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodAutor().equalsIgnoreCase(codAutor)) {
				autores.remove(i);
				return "Registro de autor eliminado";
			}
		}
		throw new IllegalArgumentException("Seleccione un autor");
	}

	public void guardar() {
		ArrayList<String> lineas = new ArrayList<>();
		for (Autor autor : autores) {
			lineas.add(autor.getCodAutor() + ";" + autor.getNombre());
		}
		try {
			Files.write(ARCHIVO, lineas, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new IllegalStateException("No se pudo guardar autores.txt", ex);
		}
	}

	public void recuperar() {
		autores.clear();
		if (!Files.exists(ARCHIVO)) return;
		try {
			for (String linea : Files.readAllLines(ARCHIVO, StandardCharsets.UTF_8)) {
				String[] campos = linea.split(";", -1);
				if (campos.length != 2 || campos[0].isBlank() || campos[1].isBlank()) {
					throw new IllegalStateException("Registro inválido en autores.txt: " + linea);
				}
				autores.add(new Autor(campos[0], campos[1]));
			}
		} catch (IOException ex) {
			throw new IllegalStateException("No se pudo leer autores.txt", ex);
		}
	}

	private String siguienteCodigo() {
		int mayor = 0;
		for (Autor autor : autores) {
			String codigo = autor.getCodAutor();
			if (codigo.matches("A\\d+")) {
				mayor = Math.max(mayor, Integer.parseInt(codigo.substring(1)));
			}
		}
		return "A" + (mayor + 10);
	}

	private boolean nombreRepetido(String nombre, String codigoActual) {
		for (Autor autor : autores) {
			if (autor.getNombre().equalsIgnoreCase(nombre)
					&& (codigoActual == null || !autor.getCodAutor().equalsIgnoreCase(codigoActual))) {
				return true;
			}
		}
		return false;
	}

	private String validarNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty() || nombre.contains(";")
				|| nombre.contains("\n") || nombre.contains("\r")) {
			throw new IllegalArgumentException("Ingrese un nombre de autor válido");
		}
		return nombre.trim();
	}
}

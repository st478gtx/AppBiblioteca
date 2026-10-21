package clases;

public class Autor {
	private String codAutor;
	private String nombre;

	public Autor(String codAutor, String nombre) {
		this.codAutor = codAutor;
		this.nombre = nombre;
	}

	public String getCodAutor() {
		return codAutor;
	}

	public void setCodAutor(String codAutor) {
		this.codAutor = codAutor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}

package clases;

public class Categoria {
	private String codCategoria;
	public String nombre;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Categoria() {
	}

	public Categoria(String nombre) {
		correlativo += 10;
		this.codCategoria = "C" + correlativo;
		this.nombre = nombre;
	}

	public Categoria(String codCategoria, String nombre) {
		this.codCategoria = codCategoria;
		this.nombre = nombre;
		correlativo += 10;
	}

	public String getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
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

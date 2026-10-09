package clases;

public class Categoria {
	private int codCategoria;
	private String nombre;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Categoria() {
	}

	public Categoria(String nombre) {
		correlativo ++;
		this.codCategoria = correlativo;
		this.nombre = nombre;
	}

	public int getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(int codCategoria) {
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

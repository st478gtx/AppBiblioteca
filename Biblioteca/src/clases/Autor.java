package clases;

public class Autor {
	private String codAutor;
	private String nombre;
	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Autor(String nombre) {
		correlativo += 10;
		this.codAutor = "A" + correlativo;
		this.nombre = nombre;
	}

	public Autor(String codAutor, String nombre) {
		this.codAutor = codAutor;
		this.nombre = nombre;
		actualizarCorrelativo(codAutor);
	}

	private static void actualizarCorrelativo(String codigo) {
		if (codigo != null && codigo.matches("A\\d+")) {
			correlativo = Math.max(correlativo, Integer.parseInt(codigo.substring(1)));
		}
	}

	public String getCodAutor() {
		return codAutor;
	}

	public void setCodAutor(String codAutor) {
		this.codAutor = codAutor;
		actualizarCorrelativo(codAutor);
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

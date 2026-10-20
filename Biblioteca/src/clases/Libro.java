package clases;

public class Libro {
	private int anioPublicacion, cantidadTotal, cantidadDisponible;
	private String codLibro, codCategoria, codAutor, titulo, autor, isnb, rutaPortada;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Libro() {
	}

	public Libro(String titulo, String autor, String isnb, String codCategoria, int anioPublicacion,
			int cantidadDisponible, int cantidadTotal, String rutaPortada) {
		correlativo += 10;
		this.codLibro = "L" + correlativo;
		this.titulo = titulo;
		this.autor = autor;
		this.isnb = isnb;
		this.codCategoria = codCategoria;
		this.anioPublicacion = anioPublicacion;
		this.cantidadDisponible = cantidadDisponible;
		this.cantidadTotal = cantidadTotal;
		this.rutaPortada = rutaPortada;
	}

	public Libro(String codLibro, String titulo, String autor, String isnb, String codCategoria, int anioPublicacion,
			int cantidadDisponible, int cantidadTotal, String rutaPortada) {
		this.codLibro = codLibro;
		this.titulo = titulo;
		this.autor = autor;
		this.isnb = isnb;
		this.codCategoria = codCategoria;
		this.anioPublicacion = anioPublicacion;
		this.cantidadDisponible = cantidadDisponible;
		this.cantidadTotal = cantidadTotal;
		this.rutaPortada = rutaPortada;
		correlativo += 10;
	}

	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
	}

	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getCodAutor() {
		return codAutor;
	}

	public void setCodAutor(String codAutor) {
		this.codAutor = codAutor;
	}

	public String getIsnb() {
		return isnb;
	}

	public void setIsnb(String isnb) {
		this.isnb = isnb;
	}

	public String getRutaPortada() {
		return rutaPortada;
	}

	public void setRutaPortada(String rutaPortada) {
		this.rutaPortada = rutaPortada;
	}

	public String getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}

	public String getCodLibro() {
		return codLibro;
	}

	public void setCodLibro(String codLibro) {
		this.codLibro = codLibro;
	}

}

package clases;

import java.time.LocalDate;

public class Prestamo {
	String codPrestamo;
	Usuario usuario;
	Libro libro;
	LocalDate fechaPrestamo;
	LocalDate fechaDevolucion;
	String estado;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Prestamo(Usuario usuario, Libro libro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, String estado) {
		correlativo += 10;
		this.codPrestamo = "P" + correlativo;
		this.usuario = usuario;
		this.libro = libro;
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		this.estado = estado;
	}
	
	public Prestamo() {}

	public String getCodPrestamo() {
		return codPrestamo;
	}

	public void setCodPrestamo(String codPrestamo) {
		this.codPrestamo = codPrestamo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public LocalDate getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(LocalDate fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public LocalDate getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(LocalDate fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}

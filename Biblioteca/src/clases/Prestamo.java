package clases;

import java.time.LocalDate;

public class Prestamo {
	String codPrestamo;
	String codUsuario;
	LocalDate fechaPrestamo;
	LocalDate fechaDevolucion;
	String estado;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public Prestamo(String codUsuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
		correlativo += 10;
		this.codPrestamo = "P" + correlativo;
		this.codUsuario = codUsuario;
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
	}

	public Prestamo(String codPrestamo, String codUsuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
		this.codPrestamo = codPrestamo;
		this.codUsuario = codUsuario;
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		correlativo += 10;
	}

	public Prestamo() {
	}

	public String getCodPrestamo() {
		return codPrestamo;
	}

	public void setCodPrestamo(String codPrestamo) {
		this.codPrestamo = codPrestamo;
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(String usuario) {
		this.codUsuario = usuario;
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

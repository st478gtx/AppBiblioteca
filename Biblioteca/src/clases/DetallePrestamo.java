package clases;

public class DetallePrestamo {
	String codLibro, codPrestamo, codDetallePrestamo;
	int cantPrestamo;

	private static int correlativo;

	static {
		correlativo = 0;
	}

	public DetallePrestamo(String codLibro, String codPrestamo, int cantPrestamo) {
		correlativo += 10;
		this.codDetallePrestamo = "DP" + correlativo;
		this.codLibro = codLibro;
		this.codPrestamo = codPrestamo;
		this.cantPrestamo = cantPrestamo;
	}

	public DetallePrestamo(String codDetallePrestamo,  String codPrestamo, String codLibro, int cantPrestamo) {
		this.codDetallePrestamo = codDetallePrestamo;
		this.codLibro = codLibro;
		this.codPrestamo = codPrestamo;
		this.cantPrestamo = cantPrestamo;
		correlativo += 10;
	}

	public String getCodLibro() {
		return codLibro;
	}

	public void setCodLibro(String codLibro) {
		this.codLibro = codLibro;
	}

	public String getCodPrestamo() {
		return codPrestamo;
	}

	public void setCodPrestamo(String codPrestamo) {
		this.codPrestamo = codPrestamo;
	}

	public String getCodDetallePrestamo() {
		return codDetallePrestamo;
	}

	public void setCodDetallePrestamo(String codDetallePrestamo) {
		this.codDetallePrestamo = codDetallePrestamo;
	}

	public int getCantPrestamo() {
		return cantPrestamo;
	}

	public void setCantPrestamo(int cantPrestamo) {
		this.cantPrestamo = cantPrestamo;
	}

}

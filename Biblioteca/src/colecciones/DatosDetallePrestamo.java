package colecciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import clases.DetallePrestamo;

public class DatosDetallePrestamo {
	public ArrayList<DetallePrestamo> detallePrestamos = new ArrayList<DetallePrestamo>();

	public DatosDetallePrestamo() {
		recuperar();
	}

	public int longitud() {
		return detallePrestamos.size();
	}

	public DetallePrestamo obtener(int i) {
		return detallePrestamos.get(i);
	}

	public String agregar(String codLibro, String codPrestamo, int cantPrestamo) {

		DetallePrestamo dp = new DetallePrestamo(codLibro, codPrestamo, cantPrestamo);

		detallePrestamos.add(dp);
		return "Registro detalle prestamo agregado";
	}

	public String actualizar(DetallePrestamo reg) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodDetallePrestamo().equalsIgnoreCase(reg.getCodDetallePrestamo())) {
				detallePrestamos.set(i, reg);
				return "Registro detalle prestamo actualizado";
			}
		}
		return "Detalle de prestamo no encontrado";
	}

	public String remover(String codDetallePrestamo) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodDetallePrestamo().equalsIgnoreCase(codDetallePrestamo)) {
				detallePrestamos.remove(i);
				return "Registro detalle prestamo eliminado";
			}
		}
		return "Detalle de prestamo no encontrado";
	}

	public String guardar() {

		String mensaje = "";

		if (longitud() == 0) {

			mensaje = "Registre un detalle de prestamo para guardarlo";

		} else {
			try {

				PrintWriter detallePrestamo = new PrintWriter(new FileWriter("datos\\detallePrestamo.txt"));

				for (int i = 0; i < longitud(); i++) {

					DetallePrestamo c = obtener(i);

					detallePrestamo.println(c.getCodDetallePrestamo() + ";" + c.getCodPrestamo() + ";" + c.getCodLibro()
							+ ";" + c.getCantPrestamo());
				}

				detallePrestamo.close();

				mensaje = "Se ha guardado:" + longitud() + " registros detalle prestamo";

			} catch (Exception ex) {
				mensaje = ex.getMessage();
			}
		}
		return mensaje;
	}

	public void recuperar() {
		detallePrestamos.clear();
		try {

			BufferedReader detallePrestamo = new BufferedReader(new FileReader("datos\\detallePrestamo.txt"));
			String linea;

			while ((linea = detallePrestamo.readLine()) != null) {

				String[] arreglo = linea.split(";");

				DetallePrestamo c = new DetallePrestamo(arreglo[0], arreglo[1], arreglo[2],
						Integer.parseInt(arreglo[3]));

				detallePrestamos.add(c);
			}

			detallePrestamo.close();
		} catch (Exception ex) {
			return;
		}
	}
}

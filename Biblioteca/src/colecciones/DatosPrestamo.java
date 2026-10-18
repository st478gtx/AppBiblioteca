package colecciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import clases.Prestamo;

public class DatosPrestamo {
	public ArrayList<Prestamo> prestamos = new ArrayList<>();

	public int longitud() {
		return prestamos.size();
	}

	public Prestamo obtener(int i) {
		return prestamos.get(i);
	}

	public String agregar(Prestamo p) {
		prestamos.add(p);
		return String.valueOf(p.getCodPrestamo());
	}

	public String actualizar(Prestamo reg) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodPrestamo().equalsIgnoreCase(reg.getCodPrestamo())) {
				prestamos.set(i, reg);
				return "Registro de prestamo actualizado";
			}
		}
		return "Prestamo no encontrado";
	}

	public String remover(String codPrestamo) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodPrestamo().equalsIgnoreCase(codPrestamo)) {
				prestamos.remove(i);
				return "Registro detalle prestamo eliminado";
			}
		}
		return "Prestamo no encontrado";
	}

	public String guardar() {

		String mensaje = "";

		if (longitud() == 0) {

			mensaje = "Registre un detalle de prestamo para guardarlo";

		} else {
			try {

				PrintWriter detallePrestamo = new PrintWriter(new FileWriter("datos\\prestamos.txt"));

				for (int i = 0; i < longitud(); i++) {

					Prestamo c = obtener(i);

					detallePrestamo.println(c.getCodPrestamo() + ";" + c.getCodUsuario() + ";" + c.getFechaPrestamo()
							+ ";" + c.getFechaDevolucion());
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
		prestamos.clear();
		try {

			BufferedReader categoria = new BufferedReader(new FileReader("datos\\prestamos.txt"));
			String linea;

			while ((linea = categoria.readLine()) != null) {

				String[] arreglo = linea.split(";");

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

				LocalDate fechaPrestamo = LocalDate.parse(arreglo[3], formatter);

				LocalDate fechaDevolucion = LocalDate.parse(arreglo[4], formatter);

				Prestamo c = new Prestamo(arreglo[0], arreglo[1], fechaPrestamo, fechaDevolucion);

				prestamos.add(c);
			}

			categoria.close();
		} catch (Exception ex) {
			return;
		}
	}
}

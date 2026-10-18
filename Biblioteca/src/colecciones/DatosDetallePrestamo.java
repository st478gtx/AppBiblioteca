package colecciones;

import java.util.ArrayList;

import clases.DetallePrestamo;
import util.Codigos;
import repository.Repositorio;

public class DatosDetallePrestamo {
	private final Repositorio<DetallePrestamo> repositorio;
	private final DatosPrestamo prestamos;
	private final DatosLibro libros;
	private final ArrayList<DetallePrestamo> detallePrestamos = new ArrayList<>();
	public ArrayList<DetallePrestamo> listar() { return new ArrayList<>(detallePrestamos); }

	public DatosDetallePrestamo(Repositorio<DetallePrestamo> repositorio, DatosPrestamo prestamos, DatosLibro libros) {
		this.repositorio = repositorio;
		this.prestamos = prestamos;
		this.libros = libros;
		recuperar();
	}

	public int longitud() {
		return detallePrestamos.size();
	}

	public DetallePrestamo obtener(int i) {
		return detallePrestamos.get(i);
	}

	public String agregar(String codLibro, String codPrestamo, int cantPrestamo) {
		validar(codLibro, codPrestamo, cantPrestamo);

		DetallePrestamo dp = new DetallePrestamo(codLibro, codPrestamo, cantPrestamo);
		dp.setCodDetallePrestamo(Codigos.siguiente("DP", 0, 10,
				detallePrestamos.stream().map(DetallePrestamo::getCodDetallePrestamo).filter(java.util.Objects::nonNull).toList()));
		if (detallePrestamos.stream().anyMatch(d -> d.getCodDetallePrestamo().equals(dp.getCodDetallePrestamo())))
			throw new IllegalArgumentException("Código de detalle duplicado");

		detallePrestamos.add(dp);
		return "Registro detalle prestamo agregado";
	}

	public String actualizar(DetallePrestamo reg) {
		validar(reg.getCodLibro(), reg.getCodPrestamo(), reg.getCantPrestamo());
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodDetallePrestamo().equalsIgnoreCase(reg.getCodDetallePrestamo())) {
				detallePrestamos.set(i, reg);
				return "Registro detalle prestamo actualizado";
			}
		}
		return "Detalle de prestamo no encontrado";
	}

	private void validar(String codLibro, String codPrestamo, int cantidad) {
		if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva");
		if (libros.obtenerPorCodigo(codLibro) == null || prestamos.obtener(codPrestamo) == null)
			throw new IllegalArgumentException("Libro o préstamo inexistente");
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
		repositorio.guardar(new ArrayList<>(detallePrestamos));
		return "Se han guardado " + detallePrestamos.size() + " registros de detalles";
	}

	public void recuperar() {
		ArrayList<DetallePrestamo> cargados = repositorio.cargar();
		detallePrestamos.clear();
		detallePrestamos.addAll(cargados);
	}
}

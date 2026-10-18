package colecciones;

import java.util.ArrayList;

import clases.Prestamo;
import util.Codigos;
import repository.Repositorio;

public class DatosPrestamo {
	private final Repositorio<Prestamo> repositorio;
	private final ArrayList<Prestamo> prestamos = new ArrayList<>();
	public ArrayList<Prestamo> listar() { return new ArrayList<>(prestamos); }

	public DatosPrestamo(Repositorio<Prestamo> repositorio) {
		this.repositorio = repositorio;
		recuperar();
	}

	public int longitud() {
		return prestamos.size();
	}

	public Prestamo obtener(int i) {
		return prestamos.get(i);
	}

	public Prestamo obtener(String codPrestamo) {
		for (int i = 0; i < longitud(); i++) {
			if (obtener(i).getCodPrestamo().equalsIgnoreCase(codPrestamo)) {
				return prestamos.get(i);
			}
		}
		return null;
	}

	public String agregar(Prestamo p) {
		if (p.getCodPrestamo() == null || p.getCodPrestamo().isBlank()) {
			p.setCodPrestamo(Codigos.siguiente("P", 0, 10,
					prestamos.stream().map(Prestamo::getCodPrestamo).filter(java.util.Objects::nonNull).toList()));
		}
		if (obtener(p.getCodPrestamo()) != null) throw new IllegalArgumentException("Código de préstamo duplicado");
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
		repositorio.guardar(new ArrayList<>(prestamos));
		return "Se han guardado " + prestamos.size() + " registros de préstamos";
	}

	public void recuperar() {
		ArrayList<Prestamo> cargados = repositorio.cargar();
		prestamos.clear();
		prestamos.addAll(cargados);
	}
}

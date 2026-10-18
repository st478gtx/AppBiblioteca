package service;

import clases.DetallePrestamo;
import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import colecciones.DatosDetallePrestamo;
import colecciones.DatosLibro;
import colecciones.DatosPrestamo;
import util.DatosLibroDetalle;
import util.DetalleLibro;
import repository.TransaccionArchivos;

import java.time.LocalDate;
import java.util.ArrayList;

public final class PrestamoService {
    private final DatosPrestamo prestamos;
    private final DatosDetallePrestamo detalles;
    private final DatosLibro libros;
    private final DatosLibroDetalle seleccion = new DatosLibroDetalle();

    public PrestamoService(DatosPrestamo prestamos, DatosDetallePrestamo detalles, DatosLibro libros) {
        this.prestamos = prestamos;
        this.detalles = detalles;
        this.libros = libros;
    }

    public void seleccionar(Libro libro) { seleccion.actualizarLista(libro); }
    public void quitar(Libro libro) { seleccion.eliminar(libro); }
    public void limpiarSeleccion() { seleccion.limpiar(); }
    public ArrayList<DetalleLibro> seleccionados() { return seleccion.listar(); }
    public int cantidadSeleccionada() {
        int total = 0;
        for (DetalleLibro d : seleccion.listar()) total += d.getCantidad();
        return total;
    }
    public ArrayList<Prestamo> listar() { return prestamos.listar(); }
    public Prestamo obtener(String codigo) { return prestamos.obtener(codigo); }
    public ArrayList<DetallePrestamo> detallesDe(String codigo) {
        ArrayList<DetallePrestamo> resultado = new ArrayList<>();
        for (DetallePrestamo d : detalles.listar()) {
            if (d.getCodPrestamo().equals(codigo)) resultado.add(d);
        }
        return resultado;
    }

    public Prestamo registrar(Usuario usuario, LocalDate fecha, LocalDate devolucion) {
        if (usuario == null) throw new IllegalArgumentException("Seleccione un usuario");
        if (seleccion.longitud() == 0) throw new IllegalArgumentException("Seleccione al menos un libro");
        for (DetalleLibro elegido : seleccion.listar()) {
            Libro libro = libros.obtenerPorCodigo(elegido.getLibro().getCodLibro());
            if (libro == null) throw new IllegalArgumentException("El libro seleccionado ya no existe");
            if (elegido.getCantidad() <= 0 || elegido.getCantidad() > libro.getCantidadDisponible()) {
                throw new IllegalArgumentException("No hay ejemplares disponibles de " + libro.getTitulo());
            }
        }

        Prestamo prestamo = new Prestamo(usuario.getCodUser(), fecha, devolucion);
        prestamos.agregar(prestamo);
        ArrayList<DetallePrestamo> nuevosDetalles = new ArrayList<>();
        ArrayList<DetalleLibro> descontados = new ArrayList<>();
        try {
            for (DetalleLibro elegido : seleccion.listar()) {
                Libro libro = libros.obtenerPorCodigo(elegido.getLibro().getCodLibro());
                detalles.agregar(libro.getCodLibro(), prestamo.getCodPrestamo(), elegido.getCantidad());
                nuevosDetalles.add(detalles.obtener(detalles.longitud() - 1));
                libro.setCantidadDisponible(libro.getCantidadDisponible() - elegido.getCantidad());
                descontados.add(elegido);
            }
            TransaccionArchivos.ejecutar(() -> {
                prestamos.guardar();
                detalles.guardar();
                libros.guardar();
            }, "prestamos.txt", "detallePrestamo.txt", "libros.txt");
            limpiarSeleccion();
            return prestamo;
        } catch (RuntimeException error) {
            prestamos.remover(prestamo.getCodPrestamo());
            for (DetallePrestamo detalle : nuevosDetalles) detalles.remover(detalle.getCodDetallePrestamo());
            for (DetalleLibro elegido : descontados) {
                Libro libro = libros.obtenerPorCodigo(elegido.getLibro().getCodLibro());
                libro.setCantidadDisponible(libro.getCantidadDisponible() + elegido.getCantidad());
            }
            throw error;
        }
    }
}

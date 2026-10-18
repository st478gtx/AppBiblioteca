package repository;

import clases.Categoria;
import clases.DetallePrestamo;
import clases.Libro;
import clases.Prestamo;
import clases.Usuario;
import clases.Autor;
import clases.LibroAutor;
import java.time.LocalDate;

/** Conversión de las líneas .txt existentes, sin cambiar columnas ni fechas. */
public final class Formatos {
    private Formatos() {}

    private static String[] campos(String linea, int cantidad) {
        String[] partes = linea.split(";", -1);
        if (partes.length != cantidad) throw new IllegalArgumentException("Se esperaban " + cantidad + " campos");
        return partes;
    }

    public static Libro leerLibro(String linea) {
        String[] x = campos(linea, 9);
        return new Libro(x[0], x[1], x[2], x[3], x[4],
                Integer.parseInt(x[5]), Integer.parseInt(x[6]), Integer.parseInt(x[7]), x[8]);
    }
    public static String escribirLibro(Libro x) {
        return String.join(";", x.getCodLibro(), x.getTitulo(), x.getAutor(), x.getIsnb(),
                x.getCodCategoria(), String.valueOf(x.getAnioPublicacion()),
                String.valueOf(x.getCantidadDisponible()), String.valueOf(x.getCantidadTotal()), x.getRutaPortada());
    }
    public static Categoria leerCategoria(String linea) {
        String[] x = campos(linea, 2); return new Categoria(x[0], x[1]);
    }
    public static String escribirCategoria(Categoria x) { return x.getCodCategoria() + ";" + x.getNombre(); }
    public static Usuario leerUsuario(String linea) {
        String[] x = campos(linea, 6); return new Usuario(x[0], x[1], x[2], x[3], x[4], x[5]);
    }
    public static String escribirUsuario(Usuario x) {
        return String.join(";", x.getCodUser(), x.getNomUser(), x.getDniUser(), x.getEmailUser(), x.getFonoUser(), x.getEstadoUser());
    }
    public static Prestamo leerPrestamo(String linea) {
        String[] x = campos(linea, 4);
        return new Prestamo(x[0], x[1], LocalDate.parse(x[2]), LocalDate.parse(x[3]));
    }
    public static String escribirPrestamo(Prestamo x) {
        return String.join(";", x.getCodPrestamo(), x.getCodUsuario(),
                x.getFechaPrestamo().toString(), x.getFechaDevolucion().toString());
    }
    public static DetallePrestamo leerDetalle(String linea) {
        String[] x = campos(linea, 4);
        return new DetallePrestamo(x[0], x[1], x[2], Integer.parseInt(x[3]));
    }
    public static String escribirDetalle(DetallePrestamo x) {
        return String.join(";", x.getCodDetallePrestamo(), x.getCodPrestamo(),
                x.getCodLibro(), String.valueOf(x.getCantPrestamo()));
    }
    public static Autor leerAutor(String linea) {
        String[] x = campos(linea, 2); return new Autor(x[0], x[1]);
    }
    public static String escribirAutor(Autor x) { return x.getCodigo() + ";" + x.getNombre(); }
    public static LibroAutor leerLibroAutor(String linea) {
        String[] x = campos(linea, 2); return new LibroAutor(x[0], x[1]);
    }
    public static String escribirLibroAutor(LibroAutor x) { return x.getCodLibro() + ";" + x.getCodAutor(); }
}

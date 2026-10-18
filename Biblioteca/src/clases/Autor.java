package clases;

public final class Autor {
    private final String codigo;
    private final String nombre;

    public Autor(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    @Override public String toString() { return nombre; }
}

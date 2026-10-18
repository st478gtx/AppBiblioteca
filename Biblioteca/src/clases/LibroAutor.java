package clases;

public final class LibroAutor {
    private final String codLibro;
    private final String codAutor;

    public LibroAutor(String codLibro, String codAutor) {
        this.codLibro = codLibro;
        this.codAutor = codAutor;
    }

    public String getCodLibro() { return codLibro; }
    public String getCodAutor() { return codAutor; }
}

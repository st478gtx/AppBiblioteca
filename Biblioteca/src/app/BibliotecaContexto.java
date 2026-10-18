package app;

import colecciones.DatosCategoria;
import colecciones.DatosDetallePrestamo;
import colecciones.DatosLibro;
import colecciones.DatosPrestamo;
import colecciones.DatosUsuario;
import colecciones.DatosAutor;
import service.CategoriaService;
import service.LibroService;
import service.PrestamoService;
import service.UsuarioService;
import service.PortadaService;
import service.AutorService;
import repository.TransaccionArchivos;
import repository.ArchivoTextoRepository;
import repository.Formatos;
import util.Rutas;

/** Construye una sola vez el estado y los casos de uso compartidos por las ventanas. */
public final class BibliotecaContexto {
    private final DatosUsuario usuarios;
    private final DatosLibro libros;
    private final DatosCategoria categorias;
    private final DatosPrestamo prestamos;
    private final DatosDetallePrestamo detalles;
    private final DatosAutor autores;

    private final UsuarioService usuarioService;
    private final LibroService libroService;
    private final CategoriaService categoriaService;
    private final PrestamoService prestamoService;
    private final PortadaService portadaService = new PortadaService();
    private final AutorService autorService;

    public BibliotecaContexto() {
        TransaccionArchivos.recuperarPendiente();
        usuarios = new DatosUsuario(new ArchivoTextoRepository<>(Rutas.dato("usuarios.txt"),
                Formatos::leerUsuario, Formatos::escribirUsuario));
        libros = new DatosLibro(new ArchivoTextoRepository<>(Rutas.dato("libros.txt"),
                Formatos::leerLibro, Formatos::escribirLibro));
        categorias = new DatosCategoria(new ArchivoTextoRepository<>(Rutas.dato("categorias.txt"),
                Formatos::leerCategoria, Formatos::escribirCategoria));
        prestamos = new DatosPrestamo(new ArchivoTextoRepository<>(Rutas.dato("prestamos.txt"),
                Formatos::leerPrestamo, Formatos::escribirPrestamo));
        detalles = new DatosDetallePrestamo(new ArchivoTextoRepository<>(Rutas.dato("detallePrestamo.txt"),
                Formatos::leerDetalle, Formatos::escribirDetalle), prestamos, libros);
        autores = new DatosAutor(new ArchivoTextoRepository<>(Rutas.dato("autores.txt"),
                Formatos::leerAutor, Formatos::escribirAutor),
                new ArchivoTextoRepository<>(Rutas.dato("libroAutor.txt"),
                        Formatos::leerLibroAutor, Formatos::escribirLibroAutor), libros.listar());
        autorService = new AutorService(autores, libros);
        usuarioService = new UsuarioService(usuarios);
        libroService = new LibroService(libros, autorService);
        categoriaService = new CategoriaService(categorias);
        prestamoService = new PrestamoService(prestamos, detalles, libros);
    }

    public UsuarioService usuarios() { return usuarioService; }
    public LibroService libros() { return libroService; }
    public CategoriaService categorias() { return categoriaService; }
    public PrestamoService prestamos() { return prestamoService; }
    public PortadaService portadas() { return portadaService; }
    public AutorService autores() { return autorService; }
}

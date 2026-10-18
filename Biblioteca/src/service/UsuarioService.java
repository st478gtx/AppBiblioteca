package service;

import clases.Usuario;
import colecciones.DatosUsuario;
import java.util.ArrayList;

public final class UsuarioService {
    private final DatosUsuario datos;
    public UsuarioService(DatosUsuario datos) { this.datos = datos; }
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (int i = 0; i < datos.longitud(); i++) resultado.add(datos.obtener(i));
        return resultado;
    }
    public Usuario buscar(String codigoODni) { return datos.buscarPorCodigoODni(codigoODni); }
    public int longitud() { return datos.longitud(); }
    public Usuario obtener(int indice) { return datos.obtener(indice); }
    public String agregar(Usuario usuario) {
        validar(usuario, true);
        return datos.agregar(usuario);
    }
    public String actualizar(Usuario usuario) {
        validar(usuario, false);
        return datos.actualizar(usuario);
    }
    public void validarEliminacion(String codigo) {
        Usuario usuario = datos.buscarPorCodigoODni(codigo);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");
        if (!"Registrado".equalsIgnoreCase(usuario.getEstadoUser().trim()))
            throw new IllegalArgumentException("Solo se pueden eliminar los usuarios que se encuentren en estado Registrado.");
    }
    public String eliminar(String codigo) {
        validarEliminacion(codigo);
        return datos.eliminar(codigo);
    }
    public String guardar() { return datos.guardar(); }

    /** Conserva las validaciones existentes; el correo solo se validaba al agregar. */
    private void validar(Usuario usuario, boolean alta) {
        String nombre = usuario.getNomUser().trim();
        String dni = usuario.getDniUser().trim();
        String telefono = usuario.getFonoUser().trim();
        String correo = usuario.getEmailUser().trim();
        String estado = usuario.getEstadoUser();
        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty() || (alta && correo.isEmpty()))
            throw new IllegalArgumentException("Debe completar todos los campos.");
        if (!nombre.matches("^[\\p{L}\\s]+$"))
            throw new IllegalArgumentException("El nombre solo debe contener letras y espacios.");
        if (alta && !correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Ingrese un correo electrónico válido.");
        if (!dni.matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos.");
        if (!telefono.matches("9\\d{8}"))
            throw new IllegalArgumentException("El teléfono debe empezar con 9 y tener 9 dígitos.");
        if (estado == null || estado.isBlank() || "-- Seleccione --".equals(estado))
            throw new IllegalArgumentException("Seleccione un estado válido.");
    }
}

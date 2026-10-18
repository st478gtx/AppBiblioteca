package colecciones;
import java.util.ArrayList;
import java.io.*;

import clases.Usuario;
import util.Codigos;
import repository.Repositorio;


public class DatosUsuario {
	private final Repositorio<Usuario> repositorio;
	private ArrayList<Usuario> usuarios =new ArrayList<Usuario>();

	public DatosUsuario(Repositorio<Usuario> repositorio) {
		this.repositorio = repositorio;
		// intentamos recuperar los datos guardados en el archivo
		recuperar();
		
		//Si el archivo no existe o está vacío, recién agregamos los 10 por defecto
				if (usuarios.isEmpty()) {
		usuarios.add(new Usuario("Mario Quispe", "75896258", "mario@gmail.com","90098789","Con Prestamo"));
		usuarios.add(new Usuario("Carlos Mendoza",  "20587425", "cm@gmail.com","987789098","Penalizado"));
		usuarios.add(new Usuario("Gabriel Soto", "69325985",  "ggm@gmail.com","97867567","Con Prestamo"));
		usuarios.add(new Usuario("Cesar Altamirano", "66320120",  "caltamirano@gmail.com","987000988","Con Prestamo"));
		usuarios.add(new Usuario("Maria Garcia", "41250330",  "mariag@gmail.com","99088780","Penalizado"));
		usuarios.add(new Usuario("Carla Martinez",  "04852111", "carlam@gmail.com","900781018","Con Prestamo"));
		usuarios.add(new Usuario("Gabriela Gomez", "74660521",  "gabrielagm@gmail.com","908675557","Con Prestamo"));
		usuarios.add(new Usuario("Kenyi Farfan",  "12241500", "kfarfan@gmail.com","937303988","Registrado"));
		usuarios.add(new Usuario("Silvia Benavides",  "85655820", "sbenavides@gmail.com","963867947","Registrado"));
		usuarios.add(new Usuario("Sofia Rios",  "84553001", "sofir@gmail.com","981000248","Registrado"));
		// Guardamos estos datos iniciales en el archivo por primera vez
					for (Usuario usuario : usuarios) asignarCodigo(usuario);
					guardar();
				}
		}

	public int longitud() {
		return usuarios.size();
	}
	public Usuario obtener(int i) {
		return usuarios.get(i);
	}
	public String agregar(Usuario reg) {
		// Recorremos la lista para verificar si el DNI ya existe
	    for (Usuario u : usuarios) {
	        if (u.getDniUser().equals(reg.getDniUser())) {
	            return "El DNI " + reg.getDniUser() + " ya se encuentra registrado.";
	        }
	    }
	    //si no existe lo agregamos de manera normal
		asignarCodigo(reg);
		usuarios.add(reg);
		return "Registro del Usuario Agregado";
	}

	private void asignarCodigo(Usuario usuario) {
		if (usuario.getCodUser() == null || usuario.getCodUser().isBlank()) {
			usuario.setCodUser(Codigos.siguiente("U", 1000, 1,
					usuarios.stream().map(Usuario::getCodUser).filter(java.util.Objects::nonNull).toList()));
		}
	}
	public String actualizar(Usuario reg) {
		// 1. Verificar si el DNI ya le pertenece a OTRO usuario diferente
	    for (Usuario u : usuarios) {
	        if (u.getDniUser().equals(reg.getDniUser()) && !u.getCodUser().equals(reg.getCodUser())) {
	            return "El DNI " + reg.getDniUser() + " ya se encuentra registrado por otro usuario.";
	        }
	    }
		
		//buscar el Usuario por su codigo, si lo encuentra modificar sus datos
		for(int i = 0; i < usuarios.size(); i++) {
	        if(usuarios.get(i).getCodUser().equals(reg.getCodUser())) {
	        	usuarios.set(i, reg);
	            return "Registro del Usuario Actualizado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	public String eliminar(String codUser) {
		//buscar el Usuario por su codigo, si lo encuentra eliminar sus datos
		for(int i = 0; i < usuarios.size(); i++) {
	        if(usuarios.get(i).getCodUser().equals(codUser)) {
	        	usuarios.remove(i);
	            return "Registro del Usuario Eliminado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	
	public String guardar() {
		repositorio.guardar(new ArrayList<>(usuarios));
		return "Se han guardado " + usuarios.size() + " registros de usuarios";
	}
	
	public void recuperar() {
		ArrayList<Usuario> cargados = repositorio.cargar();
		usuarios.clear();
		usuarios.addAll(cargados);
	}

	public Usuario buscarPorCodigoODni(String texto) {
	    for (Usuario u : usuarios) {
	        // Compara si el texto coincide con el código o con el DNI
	        if (u.getCodUser().equalsIgnoreCase(texto) || u.getDniUser().equals(texto)) {
	            return u; // Retorna el usuario si lo encuentra
	        }
	    }
	    return null; // Retorna null si no existe
	}
	
	
	
	
}

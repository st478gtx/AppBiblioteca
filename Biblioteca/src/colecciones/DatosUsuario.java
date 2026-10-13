package colecciones;
import java.util.ArrayList;


import clases.Usuario;


public class DatosUsuario {
	private ArrayList<Usuario> usuario =new ArrayList<Usuario>();

	public DatosUsuario () {
			//agregar 10 objetos 
			usuario.add(new Usuario("Mario Quispe", "75896258", "mario@gmail.com","90098789","Con Prestamo"));
			usuario.add(new Usuario("Carlos Mendoza",  "20587425", "cm@gmail.com","987789098","Penalizado"));
			usuario.add(new Usuario("Gabriel Soto", "69325985",  "ggm@gmail.com","97867567","Con Prestamo"));
			usuario.add(new Usuario("Cesar Altamirano", "66320120",  "caltamirano@gmail.com","987000988","Con Prestamo"));
			usuario.add(new Usuario("Maria Garcia", "41250330",  "mariag@gmail.com","99088780","Penalizado"));
			usuario.add(new Usuario("Carla Martinez",  "04852111", "carlam@gmail.com","900781018","Con Prestamo"));
			usuario.add(new Usuario("Gabriela Gomez", "74660521",  "gabrielagm@gmail.com","908675557","Con Prestamo"));
			usuario.add(new Usuario("Kenyi Farfan",  "12241500", "kfarfan@gmail.com","937303988","Registrado"));
			usuario.add(new Usuario("Silvia Benavides",  "85655820", "sbenavides@gmail.com","963867947","Registrado"));
			usuario.add(new Usuario("Sofia Rios",  "84553001", "sofir@gmail.com","981000248","Registrado"));

		}

	public int longitud() {
		return usuario.size();
	}
	public Usuario obtener(int i) {
		return usuario.get(i);
	}
	public String agregar(Usuario reg) {
		usuario.add(reg);
		return "Registro del Usuario Agregado";
	}
	public String actualizar(Usuario reg) {
		//buscar el Usuario por su codigo, si lo encuentra modificar sus datos
		for(int i = 0; i < usuario.size(); i++) {
	        if(usuario.get(i).codUser.equals(reg.codUser)) {
	        	usuario.set(i, reg);
	            return "Registro del Usuario Actualizado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	public String eliminar(String codUser) {
		//buscar el Usuario por su codigo, si lo encuentra modificar sus datos
		for(int i = 0; i < usuario.size(); i++) {
	        if(usuario.get(i).codUser.equals(codUser)) {
	        	usuario.remove(i);
	            return "Registro del Usuario Eliminado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	
	
}

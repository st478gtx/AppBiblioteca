package colecciones;
import java.util.ArrayList;
import java.io.*;

import clases.Usuario;


public class DatosUsuario {
	private ArrayList<Usuario> usuarios =new ArrayList<Usuario>();

	public DatosUsuario () {
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
		usuarios.add(reg);
		return "Registro del Usuario Agregado";
	}
	public String actualizar(Usuario reg) {
		//buscar el Usuario por su codigo, si lo encuentra modificar sus datos
		for(int i = 0; i < usuarios.size(); i++) {
	        if(usuarios.get(i).codUser.equals(reg.codUser)) {
	        	usuarios.set(i, reg);
	            return "Registro del Usuario Actualizado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	public String eliminar(String codUser) {
		//buscar el Usuario por su codigo, si lo encuentra modificar sus datos
		for(int i = 0; i < usuarios.size(); i++) {
	        if(usuarios.get(i).codUser.equals(codUser)) {
	        	usuarios.remove(i);
	            return "Registro del Usuario Eliminado";
	        }
	    }
	    return "Usuario no encontrado";
	}
	
	public String guardar() {
		String mensaje="";
		if(longitud()==0) {
			mensaje= "Registre al usuario para guardarlos";
		}
		else {
			try {
				//crear el archivo usuarios.txt para escribir
				PrintWriter usuario=new PrintWriter(new FileWriter("C:\\PrestamosFile\\usuarios.txt"));
				for(int i=0; i<longitud(); i++) {
					//recupero el usuario a traves de su ubicacion en i
					Usuario x=obtener(i);
					//imprmir el contenido de x en usuarios
					usuario.println(x.codUser+";"+x.nomUser+";"+x.dniUser+";"+x.emailUser+";"+x.fonoUser+";"+x.estadoUser);
				}
				//despues del for, cerrar
				usuario.close();
				mensaje="Se ha guardado:"+longitud()+" registros de usuarios";
			}
			catch(Exception ex) {
				mensaje=ex.getMessage(); //en caso de error, almaceno
			}
		}
		return mensaje;
	}
	
	public void recuperar() {
		usuarios.clear(); // Limpiamos la lista antes de cargar para evitar duplicados
		try {
			//abrir el archivo usuarios.txt para leer
			BufferedReader usuario=new BufferedReader(new FileReader("C:\\PrestamosFile\\usuarios.txt"));
			String linea;
			//mientras que al leer la linea sea diferente a null
			while((linea=usuario.readLine())!=null)
			{
				//descomponer la linea en un array de cadena
				String[] arreglo=linea.split(";");
				//instanciar el Autor pasando todos los datos
				Usuario x=new Usuario(arreglo[0],arreglo[1],arreglo[2],arreglo[3],arreglo[4],arreglo[5]);
				//agregar a la lista
				usuarios.add(x);
			}
			//despues del for, cerrar
			usuario.close();
		}
		catch(Exception ex) {
			return;
		}
		
	}

	
	
	
	
	
}

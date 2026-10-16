package clases;

public class Usuario {

	public String codUser,nomUser,dniUser,emailUser,fonoUser, estadoUser;
	private static int correlativo;
	
	static {
		correlativo=1000;
	}
	
	public Usuario(String nomUser, String dniUser, String emailUser , String fonoUser, String estadoUser) {
		correlativo+=1;
		this.codUser="U"+correlativo;
		this.nomUser = nomUser;
		this.dniUser = dniUser;
		this.emailUser = emailUser;
		this.fonoUser = fonoUser;
		this.estadoUser = estadoUser;

	}
	
	public Usuario(String codUser,String nomUser, String dniUser, String emailUser , String fonoUser, String estadoUser) {
		
		this.codUser=codUser;
		this.nomUser = nomUser;
		this.dniUser = dniUser;
		this.emailUser = emailUser;
		this.fonoUser = fonoUser;
		this.estadoUser = estadoUser;
		
		// Actualizamos el correlativo basándonos en el código existente (ej. U1012 -> toma el 1012)
				try {
					int num = Integer.parseInt(codUser.substring(1));
					if (num > correlativo) {
						correlativo = num;
					}
				} catch (Exception e) {// Por si hay algún formato distinto
				}
			}
		
	
	public Usuario() {
	
	}

	@Override
	public String toString() {
		return nomUser;
	}

}

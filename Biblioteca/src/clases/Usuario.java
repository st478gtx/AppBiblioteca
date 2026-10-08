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

}

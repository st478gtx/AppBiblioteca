package clases;

public class Usuario {

	private String codUser, nomUser, dniUser, emailUser, fonoUser, estadoUser;
	
	public Usuario(String nomUser, String dniUser, String emailUser , String fonoUser, String estadoUser) {
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
		
			}
		
	
	public Usuario() {
	
	}

	public String getCodUser() { return codUser; }
	public void setCodUser(String codUser) { this.codUser = codUser; }
	public String getNomUser() { return nomUser; }
	public void setNomUser(String nomUser) { this.nomUser = nomUser; }
	public String getDniUser() { return dniUser; }
	public void setDniUser(String dniUser) { this.dniUser = dniUser; }
	public String getEmailUser() { return emailUser; }
	public void setEmailUser(String emailUser) { this.emailUser = emailUser; }
	public String getFonoUser() { return fonoUser; }
	public void setFonoUser(String fonoUser) { this.fonoUser = fonoUser; }
	public String getEstadoUser() { return estadoUser; }
	public void setEstadoUser(String estadoUser) { this.estadoUser = estadoUser; }

	@Override
	public String toString() {
		return nomUser;
	}

}

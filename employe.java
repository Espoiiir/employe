package employé;

public class employe {
 private static int dernierID = 0;

	private String nom;
	private String prenom;
	private String service;
	private int ID;
	private int joursConges;
	
	public employe (String nom, String prenom, String service )
	{
		this.nom = nom;
		this.prenom=prenom;
		this.service= service;
		this.ID= ++dernierID;
		this.joursConges=0;
	}
	//Setters & Getters
	public int getJoursConges() {
		return joursConges;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getService() {
		return service;
	}
	public int getID() {
		return ID;
	}
	
}

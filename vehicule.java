package employé;

import java.util.Date;

public class vehicule {
 private static int dernierID = 0;

	private String type;
	private Date date;
	private boolean etat;
	private int ID;
	
	public vehicule (String type, Date date, boolean etat)
	{
		this.type = type;
		this.date =date ;
		this.etat= etat;
		this.ID= ++dernierID;
	}
	//Setters & Getters
	public String getType() {
		return type;
	}
	public Date getDate() {
		return date;
	}
	public boolean getEtat() {
		return etat;
	}
	public int getID() {
		return ID;
	}
	
}

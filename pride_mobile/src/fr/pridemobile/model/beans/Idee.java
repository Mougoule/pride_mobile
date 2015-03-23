package fr.pridemobile.model.beans;

import java.util.Date;

public class Idee {

	private String idee;
	
	private Date dateCreation;
	
	private Date dateModification;

	/* Getters and Setters */
	
	public String getIdee() {
		return idee;
	}

	public void setIdee(String idee) {
		this.idee = idee;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}
}

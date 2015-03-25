package fr.pridemobile.model.beans;

import java.util.List;

public class Projet {

	private String nomProjet;
	
	private String description;
	
	private int noteProjet;
	
	private List<Idee> idees;
	
	public String getNomProjet() {
		return nomProjet;
	}

	public void setNomProjet(String nomProjet) {
		this.nomProjet = nomProjet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNoteProjet() {
		return noteProjet;
	}

	public void setNoteProjet(int noteProjet) {
		this.noteProjet = noteProjet;
	}

	public List<Idee> getIdees() {
		return idees;
	}

	public void setIdees(List<Idee> idees) {
		this.idees = idees;
	}	
}

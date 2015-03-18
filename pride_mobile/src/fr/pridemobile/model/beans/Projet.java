package fr.pridemobile.model.beans;

public class Projet {

	private String nomProjet;
	
	private String description;
	
	private int noteProjet;
	
	private byte[] image;

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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}	
}

package fr.pridemobile.model;

import fr.pridemobile.model.beans.Utilisateur;

public class Connexion {
	private String token;

	private Utilisateur utilisateur;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	@Override
	public String toString() {
		return "Connexion [token=" + token + ", utilisateur=" + utilisateur + "]";
	}
}

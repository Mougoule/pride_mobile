package fr.pridemobile.application;

public enum PrideConfiguration {
	
	WS_BASE("webapp.ws.base"),
	
	WS_UTILISATEURS("webapp.ws.utilisateurs"),
	WS_UTILISATEURS_CONNECT("webapp.ws.utilisateurs.connect"),
	WS_UTILISATEURS_PROJET("webapp.ws.utilisateurs.projet"),
	
	WS_PROJETS("webapp.ws.projets"),
	WS_PROJETS_COLLABORATEUR("webapp.ws.projets.collaborateur"),
	
	;
	
	/**
	 * Clé de la property
	 */
	private String key;
	
	private PrideConfiguration(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}

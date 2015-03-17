package fr.pridemobile.application;

public enum PrideConfiguration {
	
	WS_BASE("webapp.ws.base"),
	
	WS_UTILISATEURS("webapp.ws.utilisateurs"),
	WS_UTILISATEURS_CONNECT("webapp.ws.utilisateurs.connect"),
	
	;
	
	/**
	 * Cl� de la property
	 */
	private String key;
	
	private PrideConfiguration(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
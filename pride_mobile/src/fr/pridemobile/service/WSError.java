package fr.pridemobile.service;

import fr.pridemobile.R;


/**
 * Liste des codes erreurs WS
 * 
 */
public enum WSError {

	/** Erreur technique */
	TECH("TECH", R.string.ws_error_tech),
	
	/** Erreur de sécurité */
	SEC_00("SEC_00", R.string.ws_error_sec_token),
	
	;
	
	/** Code erreur businnes */
	private String code;
	
	/** Id message Ã  afficher par dÃ©faut */
	private int message;
	
	private WSError(String code, int message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}
	
	public boolean checkCode(String code) {
		return (this.code.equals(code));
	}

	public int getMessage() {
		return message;
	}
	
}

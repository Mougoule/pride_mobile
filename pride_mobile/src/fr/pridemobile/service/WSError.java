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
	
	/* Erreurs de l'utilisateur */
	UTI_01("UTI_01", R.string.ws_error_utilisateur_not_found),
	UTI_02("UTI_02", R.string.ws_error_utilisateur_already_exist),
	
	/* Erreurs des projets */
	PRO_01("PRO_01", R.string.ws_error_projet_not_found),
	PRO_02("PRO_02", R.string.ws_error_projet_already_exist),
	
	;
	
	/** Code erreur businnes */
	private String code;
	
	/** Id message à  afficher par défaut */
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

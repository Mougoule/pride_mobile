package fr.pridemobile.utils;



/**
 * Constantes de l'application
 */
public final class Constants {
	
	/** Durée maximale entre 1 clics pour quitter l'appli */
	public static final int BACK_QUIT_DELAY = 2000;
	
	/* Format par défaut des dates */
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
	
	/* Préférences */
	
	/** Clé de stockage des préférences */
	public static final String PREF_KEY = "prefs";
	
	/** Connexion automatique */
	public static final String PREF_AUTOLOGIN = "autolog";
	
	/** Token de session */
	public static final String PREF_TOKEN = "token";
	
	/** Code du livreur connecté */
	public static final String PREF_CODE_LIVREUR = "codeLivreur";
	
	/** Code du trasnporteur connecté */
	public static final String PREF_CODE_TRANSP = "codeTransp";
	
	/** Lanuge de l'applciation */
	public static final String PREF_LANGUAGE = "language";
	
	/** Séparateur  pour les différents éléments d'une liste de string à envoyer à la webapp par webservices*/
	public static final String STRING_SEPARATOR = ",";
	
	
	/* Media types */
	
	/** Image PNG */
	public static final String TYPE_IMAGE_PNG = "image/png";
}

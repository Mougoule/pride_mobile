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
	
	/** Login utilisateur connecté */
	public static final String PREF_LOGIN = "login";
	
	/** Mot de passe utilisateur connecté */
	public static final String PREF_MDP = "password";

	
	/** Séparateur  pour les différents éléments d'une liste de string à envoyer à la webapp par webservices */
	public static final String STRING_SEPARATOR = ",";
	
	/* Header des appels WS */
	/** Token */
	public static final String TOKEN_HEADER = "token";
	
	/* Media types */
	
	/** Image PNG */
	public static final String TYPE_IMAGE_PNG = "image/png";
	
	/** Image JPG */
	public static final String TYPE_IMAGE_JPEG = "image/jpeg";
}

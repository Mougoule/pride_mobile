package fr.pridemobile.utils;

/**
 * Constantes de l'application
 */
public final class Constants {
	
	/** Dur�e maximale entre 1 clics pour quitter l'appli */
	public static final int BACK_QUIT_DELAY = 2000;
	
	/* Format par d�faut des dates */
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
	
	/* Pr�f�rences */
	
	/** Cl� de stockage des pr�f�rences */
	public static final String PREF_KEY = "prefs";
	
	/** Connexion automatique */
	public static final String PREF_AUTOLOGIN = "autolog";
	
	/** Token de session */
	public static final String PREF_TOKEN = "token";
	
	/** Login utilisateur connect� */
	public static final String PREF_LOGIN = "login";
	
	/** Mot de passe utilisateur connect� */
	public static final String PREF_MDP = "password";

	
	/** S�parateur  pour les diff�rents �l�ments d'une liste de string � envoyer � la webapp par webservices */
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

package fr.pridemobile.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;
import fr.pridemobile.model.WebappResponse;
import fr.pridemobile.model.beans.Projet;

public class PrideApplication extends Application {

	/** Fichier properties */
	private static final String PROPERTIES_FILE_NAME = "pride.properties";
	
	/** Instance */
	public static PrideApplication INSTANCE;
	
	/** Properties du fichier pride.properties */
	private Properties properties;
	
	/** Le projet courament ouvert */
	private Projet currentProjet;
	
	/** Les projets de l'utilisateur*/
	private List<Projet> utilisateurProjets;
	
	public PrideApplication() {
		super();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Mise en place du singleton
		INSTANCE = this;
		
		// Charger le properties
		AssetManager assetManager = getAssets();
        InputStream inputStream;
		try {
			inputStream = assetManager.open(PROPERTIES_FILE_NAME);
			properties = new Properties();
	        properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("erreur conf", e);
		}
	}
	
	/**
	 * Log une erreur WS
	 * 
	 * @param tag
	 *            Tag
	 * @param response
	 *            Webapp response
	 */
	public void logError(String tag, WebappResponse<?> response) {
		StringBuilder sb = new StringBuilder();
		sb.append("Erreur : ").append(response.getCode()).append(" - ").append(response.getMessage()).append("\n");
		List<String> errors = response.getErrors();
		if (errors != null) {
			for (String error : errors) {
				sb.append(" - ").append(error);
			}
		}
		Log.e(tag, sb.toString());
	}
	
	/**
	 * Récupère une property dans le fichier de config
	 * 
	 * @param key La clé
	 * @return La property
	 */
	public String getProperty(PrideConfiguration key) {
		return (String) properties.get(key.getKey());
	}
	
	/**
	 * Retourne des properties concaténées
	 * 
	 * @param keys Les clés
	 * @return Les properties concaténées
	 */
	public String getProperties(PrideConfiguration... keys) {
		StringBuilder sb = new StringBuilder();
		for (PrideConfiguration key : keys) {
			sb.append((String) properties.get(key.getKey()));
		}
		return sb.toString();
	}
	
	/**
	 * Nettoie la "session" encours, utile lors de la déconnexion par exemple
	 */
	public void clear() {
		this.currentProjet =  null;
		this.utilisateurProjets = null;
		
	}
	
	public Projet getCurrentProjet() {
		return currentProjet;
	}

	public void setCurrentProjet(Projet currentChargement) {
		this.currentProjet = currentChargement;
	}
	
	public List<Projet> getUtilisateurProjets() {
		return utilisateurProjets;
	}

	public void setUtilisateurProjets(List<Projet> projets) {
		this.utilisateurProjets = projets;
	}
}

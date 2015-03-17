package fr.pridemobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * Lecteur de properties dans les assets
 */
public class AssetsPropertyReader {
	
	/** Contexte */
	private Context context;
	
	/** Properties */
	private Properties properties;

	public AssetsPropertyReader(Context context) {
		this.context = context;
		properties = new Properties();
	}

	/**
	 * Lit et initialise les properties
	 * 
	 * @param fileName Nom du fichier properties
	 * @return Les properties
	 */
	public Properties getProperties(String fileName) {

		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open(fileName);
			properties.load(inputStream);

		} catch (IOException e) {
			Log.e("AssetsPropertyReader", "Une erreur s'est produite lors de lecture des properties", e);
			throw new RuntimeException(e);
		}
		
		return properties;
	}
	
}

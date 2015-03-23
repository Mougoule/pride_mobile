package fr.pridemobile.utils;

import android.content.Context;
import fr.pridemobile.service.WSError;

public final class Messages {

	/**
	 * Trouve le message correspondant à un code d'erreur WS
	 * 
	 * @param code
	 *            Le code d'erreur
	 * @return Le message
	 */
	public static String findWSMessage(String code, Context context) {
		WSError wsError = null;
		try {
			wsError = WSError.valueOf(code);
		} catch (IllegalArgumentException e) {
			// Code inconnu
			wsError = null;
		}

		if (wsError == null) {
			//return context.getString(R.string.ws_error);
			return "erreur";
		} else {
			return context.getString(wsError.getMessage());
		}
	}

}

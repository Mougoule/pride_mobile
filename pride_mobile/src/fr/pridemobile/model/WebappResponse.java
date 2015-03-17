package fr.pridemobile.model;

import java.util.List;

/**
 * Classe abstraite d'une réponse WS
 * 
 * @author RM
 *
 * @param <T> Classe de l'objet attendu dans "data"
 */
public abstract class WebappResponse<T> {
	
	/** Données */
	private T data;
	
	/** Indique si l'appel s'est bien passé (hors explosion serveur) */
	private boolean success;
	
	/** Code d'erreur */
	private String code;
	
	/** Messages d'erreur */
	private List<String> errors;
	
	/** Message principal de l'erreur */
	private String message;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

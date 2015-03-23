package fr.pridemobile.service;

import java.util.concurrent.Callable;

import fr.pridemobile.model.WebappResponse;

/**
 * FOnction de retour appel WS
 * 
 * @param <T> Type attendu dans "data"
 */
public abstract class WSCallable<T extends WebappResponse<?>> implements Callable<Void> {
	
	/** L'objet r�ponse Http */
	protected T response;
	
	/**
	 * Appel de la m�thode
	 * @param response R�ponse Http
	 * @return Rien
	 * @throws Exception Peut-�tre
	 */
	public Void call(T response) throws Exception {
		this.response = response;
		return call();
	}
	
	@Override
	public abstract Void call() throws Exception;

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

}

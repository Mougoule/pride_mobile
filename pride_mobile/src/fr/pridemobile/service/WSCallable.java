package fr.pridemobile.service;

import java.util.concurrent.Callable;

import fr.pridemobile.model.WebappResponse;

/**
 * FOnction de retour appel WS
 * 
 * @author RM
 *
 * @param <T> Type attendu dans "data"
 */
public abstract class WSCallable<T extends WebappResponse<?>> implements Callable<Void> {
	
	/** L'objet réponse Http */
	protected T response;
	
	/**
	 * Appel de la méthode
	 * @param response R2ponse Http
	 * @return Rien
	 * @throws Exception PEut-être
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

package fr.pridemobile.service.common;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ServiceException(String detailMessage) {
		super(detailMessage);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}
}

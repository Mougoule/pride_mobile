package fr.pridemobile.service.common;

/**
 * Fichier à uploader par WS
 * 
 */
public class WSFile {
	
	/** Nom du fichier */
	private String fileName;
	
	/** Données */
	private byte[] fileData;
	
	/** Type de données */
	private String contentType;
	
	public WSFile(String fileName, byte[] fileData, String contentType) {
		super();
		this.fileName = fileName;
		this.fileData = fileData;
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}

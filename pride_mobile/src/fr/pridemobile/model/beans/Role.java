package fr.pridemobile.model.beans;

public enum Role {
	
	CHEF("Chef d'�quipe"),

	COLLABO("Membre d'�quipe");

	private String role;

	private Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}

package fr.pridemobile.model.beans;

public enum Role {
	
	CHEF("Chef d'équipe"),

	COLLABO("Membre d'équipe");

	private String role;

	private Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}

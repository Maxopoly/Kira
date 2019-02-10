package com.github.maxopoly.Kira.permission;

public class KiraPermission {

	private final int id;
	private final String name;

	public KiraPermission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof KiraPermission)) {
			return false;
		}
		return ((KiraPermission) o).id == id;
	}
}

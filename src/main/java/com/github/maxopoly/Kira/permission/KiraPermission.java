package com.github.maxopoly.Kira.permission;

public class KiraPermission {

	private final int id;
	private final String name;

	public KiraPermission(int id, String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name can not be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException(id  + " is not a legal id");
		}
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof KiraPermission)) {
			return false;
		}
		return ((KiraPermission) o).id == id;
	}
}

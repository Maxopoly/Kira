package com.github.maxopoly.kira.permission;

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

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KiraPermission)) {
			return false;
		}
		return ((KiraPermission) o).id == id;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

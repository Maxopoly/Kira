package com.github.maxopoly.Kira.permission;

import java.util.HashMap;
import java.util.Map;

public class KiraRole {
	
	private final String name;
	private final int id;
	private Map<String, KiraPermission> permissions;

	public KiraRole(String name, int id) {
		permissions = new HashMap<String, KiraPermission>();
		this.name = name;
		this.id = id;
	}

	public boolean hasPermission(String permission) {
		return permissions.containsKey(permission.toLowerCase());
	}
	
	public boolean hasPermission(KiraPermission permission) {
		return hasPermission(permission.getName());
	}
	
	public void addPermission(KiraPermission perm) {
		permissions.put(perm.getName(), perm);
	}

	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof KiraRole)) {
			return false;
		}
		return ((KiraRole) o).id == id;
	}
}

package com.github.maxopoly.Kira.permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PermissionGroup {

	private String name;
	private Set<String> permissions;

	public PermissionGroup(String name, Collection<String> perms) {
		permissions = new HashSet<>(perms);
		this.name = name;
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission.toLowerCase());
	}

	public String getName() {
		return name;
	}

}

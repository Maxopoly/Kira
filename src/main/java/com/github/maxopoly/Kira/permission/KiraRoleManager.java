package com.github.maxopoly.Kira.permission;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.maxopoly.Kira.user.User;

public class KiraRoleManager {
	
	private Map<Integer, KiraRole> roleById;
	private Map<Integer, Set<KiraRole>> userRoles;
	
	public KiraRoleManager() {
		roleById = new TreeMap<>();
		userRoles = new TreeMap<>();
	}
	
	public Set<KiraRole> getRoles(User user) {
		return userRoles.get(user.getID());
	}
	
	public void addRole(User user, KiraRole role) {
		addRole(user.getID(), role);
	}
	
	public void addRole(int userID, KiraRole role) {
		Set<KiraRole> existingRoles = userRoles.get(userID);
		if (existingRoles == null) {
			existingRoles = new TreeSet<>();
			userRoles.put(userID, existingRoles);
		}
		existingRoles.add(role);
	}
	
	public boolean hasPermission(User user, String perm) {
		Set<KiraRole> existingRoles = userRoles.get(user.getID());
		if (existingRoles == null) {
			return false;
		}
		for(KiraRole role : existingRoles) {
			if (role.hasPermission(perm)) {
				return true;
			}
		}
		return false;
	}
	
	public void registerRole(KiraRole role) {
		roleById.put(role.getID(), role);
	}
	
	public KiraRole getRole(int id) {
		return roleById.get(id);
	}
}

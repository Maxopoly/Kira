package com.github.maxopoly.Kira.permission;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.KiraUser;

public class KiraRoleManager {

	private Map<Integer, KiraRole> roleById;
	private Map<String, KiraRole> roleByName;
	private Map<Integer, Set<KiraRole>> userRoles;
	private Map<String, KiraPermission> permissionsByName;
	private Map<Integer, KiraPermission> permissionsById;

	public KiraRoleManager() {
		roleById = new TreeMap<>();
		userRoles = new TreeMap<>();
		roleByName = new HashMap<String, KiraRole>();
		permissionsByName = new HashMap<>();
		permissionsById = new TreeMap<>();
	}

	public void setupDefaultPermissions() {
		KiraRole defaultRole = getOrCreateRole("default");
		KiraPermission defaultPerm = getOrCreatePermission("default");
		KiraPermission canAuthPerm = getOrCreatePermission("canauth");
		addPermissionToRole(defaultRole, defaultPerm, true);
		addPermissionToRole(defaultRole, canAuthPerm, true);
		KiraRole adminRole = getOrCreateRole("admin");
		KiraPermission adminPerm = getOrCreatePermission("admin");
		addPermissionToRole(adminRole, adminPerm, true);
		KiraRole authRole = getOrCreateRole("auth");
		KiraPermission authPerm = getOrCreatePermission("isauth");
		addPermissionToRole(authRole, authPerm, true);
	}

	public KiraPermission getOrCreatePermission(String name) {
		// check cache first
		KiraPermission perm = permissionsByName.get(name);
		if (perm != null) {
			return perm;
		}
		perm = KiraMain.getInstance().getDAO().retrieveOrCreatePermission(name);
		if (perm != null) {
			registerPermission(perm);
		}
		return perm;
	}

	public KiraRole getOrCreateRole(String name) {
		// check cache first
		KiraRole role = roleByName.get(name);
		if (role != null) {
			return role;
		}
		role = KiraMain.getInstance().getDAO().retrieveOrCreateRole(name);
		if (role != null) {
			registerRole(role);
		}
		return role;
	}

	public void reload(KiraRoleManager newData) {
		this.roleById = newData.roleById;
		this.roleByName = newData.roleByName;
		this.userRoles = newData.userRoles;
	}

	public Collection<KiraRole> getRoles(KiraUser user) {
		Set<KiraRole> roleSet = userRoles.get(user.getID());
		if (roleSet == null) {
			roleSet = new HashSet<>();
		}
		return Collections.unmodifiableCollection(roleSet);
	}

	public void giveRoleToUser(KiraUser user, KiraRole role) {
		addRole(user.getID(), role, true);
	}

	public void takeRoleFromUser(KiraUser user, KiraRole role) {
		Set<KiraRole> existingRoles = userRoles.get(user.getID());
		if (existingRoles == null) {
			return;
		}
		existingRoles.remove(role);
		KiraMain.getInstance().getDAO().takeRoleFromUser(user, role);
	}

	public void addRole(int userID, KiraRole role, boolean saveToDb) {
		Set<KiraRole> existingRoles = userRoles.get(userID);
		if (existingRoles == null) {
			existingRoles = new HashSet<>();
			userRoles.put(userID, existingRoles);
		}
		if (existingRoles.contains(role)) {
			return;
		}
		existingRoles.add(role);
		if (saveToDb) {
			KiraMain.getInstance().getDAO().addUserToRole(KiraMain.getInstance().getUserManager().getUser(userID),
					role);
		}
	}

	public boolean hasPermission(KiraUser user, String perm) {
		Set<KiraRole> existingRoles = userRoles.get(user.getID());
		if (existingRoles == null) {
			return false;
		}
		for (KiraRole role : existingRoles) {
			if (role.hasPermission(perm)) {
				return true;
			}
		}
		return false;
	}

	public KiraRole getDefaultRole() {
		return getRole("default");
	}

	public void addPermissionToRole(KiraRole role, KiraPermission perm, boolean writeToDb) {
		if (role.hasPermission(perm)) {
			return;
		}
		role.addPermission(perm);
		if (writeToDb) {
			KiraMain.getInstance().getDAO().addPermissionToRole(perm, role);
		}
	}

	public void registerRole(KiraRole role) {
		roleById.put(role.getID(), role);
		roleByName.put(role.getName(), role);
	}

	public void deleteRole(KiraRole role, boolean writeToDb) {
		roleById.remove(role.getID());
		roleByName.remove(role.getName());
		for(Set<KiraRole> roles : userRoles.values()) {
			roles.remove(role);
		}
		if (writeToDb) {
			KiraMain.getInstance().getDAO().deleteRole(role);
		}
	}

	public void registerPermission(KiraPermission perm) {
		permissionsByName.put(perm.getName(), perm);
	}

	public KiraPermission getPermission(int id) {
		return permissionsById.get(id);
	}

	public KiraPermission getPermission(String name) {
		return permissionsByName.get(name);
	}

	public KiraRole getRole(int id) {
		return roleById.get(id);
	}

	public KiraRole getRole(String name) {
		return roleByName.get(name);
	}
}

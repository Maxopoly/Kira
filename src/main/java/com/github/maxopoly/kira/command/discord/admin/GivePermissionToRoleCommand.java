package com.github.maxopoly.kira.command.discord.admin;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.permission.KiraPermission;
import com.github.maxopoly.kira.permission.KiraRole;
import com.github.maxopoly.kira.permission.KiraRoleManager;

public class GivePermissionToRoleCommand extends ArgumentBasedCommand {

	public GivePermissionToRoleCommand() {
		super("givepermission", 2, "addpermission", "giveperm", "addperm");
	}

	@Override
	public String getFunctionality() {
		return "Gives a permission to a group";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "givepermission <group> <permission>";
	}

	@Override
	protected String handle(InputSupplier sender, String[] args) {
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		KiraRole role = roleMan.getRole(args[0]);
		if (role == null) {
			return "Role " + args[0] + " not found";
		}
		KiraPermission perm = roleMan.getPermission(args [1]);
		if (perm == null) {
			return "Permission " + args[1] + " not found";
		}
		roleMan.addPermissionToRole(role, perm, true);
		return "Giving permission " + perm.getName() + " to " + role.getName();
	}

	
	

}

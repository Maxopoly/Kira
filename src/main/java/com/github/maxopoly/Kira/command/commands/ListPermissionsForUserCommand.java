package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.permission.KiraPermission;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;
import com.github.maxopoly.Kira.user.KiraUser;

public class ListPermissionsForUserCommand extends Command {

	public ListPermissionsForUserCommand() {
		super("listperms", 1, 1, "getperms");
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		StringBuilder sb = new StringBuilder();
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		KiraUser user = KiraMain.getInstance().getUserManager().parseUser(args[0], sb);
		if (user == null) {
			sb.append("User not found");
			return sb.toString();
		}
		if (roleMan.getRoles(user).isEmpty()) {
			sb.append("This user has no rules\n");
		}
		for (KiraRole role : roleMan.getRoles(user)) {
			sb.append("From " + role.getName() + ":\n");
			for (KiraPermission perm : role.getAllPermissions()) {
				sb.append(" - "+perm.getName()+"\n");
			}
		}
		return sb.toString();
	}

	@Override
	public String getUsage() {
		return "listperms [role]";
	}

	@Override
	public String getFunctionality() {
		return "Lists all permissions a user has";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}

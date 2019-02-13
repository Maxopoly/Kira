package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;
import com.github.maxopoly.Kira.user.KiraUser;

public class GiveRoleCommand extends Command {

	public GiveRoleCommand() {
		super("giverole", 2, 2, "addrole");
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		StringBuilder sb = new StringBuilder();
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		KiraRole role = roleMan.getRole(args[0]);
		if (role == null) {
			sb.append("Role " + args[0] + " not found");
			return sb.toString();
		}
		KiraUser user = KiraMain.getInstance().getUserManager().parseUser(args[1], sb);
		if (user == null) {
			sb.append("User not found");
			return sb.toString();
		}
		if (roleMan.getRoles(user).contains(role)) {
			sb.append(user.toString() + " already has role " + role.getName());
		} else {
			roleMan.giveRoleToUser(user, role);
			sb.append("Giving role " + role.getName() + " to " + user.toString());
		}
		return sb.toString();
	}

	@Override
	public String getUsage() {
		return "giverole [role] [user]";
	}

	@Override
	public String getFunctionality() {
		return "Gives a role to a user";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}

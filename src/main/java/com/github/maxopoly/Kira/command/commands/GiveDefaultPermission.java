package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;

public class GiveDefaultPermission extends Command {

	public GiveDefaultPermission() {
		super("givedefaultperms", 0, 0);
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		KiraRole defaultRole = roleMan.getDefaultRole();
		StringBuilder sb = new StringBuilder();
		KiraMain.getInstance().getUserManager().getAllUsers().forEach(u -> {
			if (roleMan.getRoles(u).isEmpty()) {
				roleMan.giveRoleToUser(u, defaultRole);
				sb.append("Giving default role to " + u.toString() + "\n");
			}
			
		});
		KiraRole authRole = roleMan.getRole("auth");
		KiraMain.getInstance().getUserManager().getAllUsers().stream().filter(u -> u.hasIngameAccount()).forEach(u ->{
			if(!roleMan.getRoles(u).contains(authRole)) {
				roleMan.giveRoleToUser(u, authRole);
				sb.append("Giving auth role to " + u.toString() + "\n");
			}
			
		});
		return sb.toString();
	}

	@Override
	public String getUsage() {
		return "givedefaultperms";
	}

	@Override
	public String getFunctionality() {
		return "Gives the default permission to all accounts without permissions";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}
package com.github.maxopoly.Kira.command.discord;

import java.util.UUID;

import com.github.maxopoly.Kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;
import com.github.maxopoly.Kira.user.AuthManager;
import com.github.maxopoly.Kira.user.KiraUser;
import com.github.maxopoly.kira.KiraMain;

public class AuthCommand extends ArgumentBasedCommand {

	public AuthCommand() {
		super("auth", 1);
		setRequireUser();
	}

	@Override
	public String getFunctionality() {
		return "Allows linking your discord account to an ingame account. Run '/discordauth' ingame to get a code.";
	}

	@Override
	public String getRequiredPermission() {
		return "canauth";
	}

	@Override
	public String getUsage() {
		return "auth [code]";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		if (user.hasIngameAccount()) {
			return "You already have a linked ingame account";
		}
		String code = args[0];
		AuthManager authMan = KiraMain.getInstance().getAuthManager();
		UUID uuid = authMan.getUserForCode(code);
		if (uuid == null) {
			return "Invalid auth code";
		}
		String name = authMan.getName(uuid);
		logger.info("Adding " + name + ":" + uuid.toString() + " as ingame account for " + user.toString());
		user.updateIngame(uuid, name);
		KiraRoleManager kiraRoleMan = KiraMain.getInstance().getKiraRoleManager();
		KiraMain.getInstance().getUserManager().addUser(user);
		KiraMain.getInstance().getRoleManager().giveDiscordRole(user);
		KiraMain.getInstance().getDAO().updateUser(user);
		KiraRole authRole = kiraRoleMan.getRole("auth");
		if (authRole != null) {
			kiraRoleMan.giveRoleToUser(user, authRole);
		}
		authMan.removeCode(code);
		return "Successfully authenticated as " + name;
	}

}
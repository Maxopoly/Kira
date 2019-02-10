package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.user.DiscordRoleManager;
import com.github.maxopoly.Kira.user.User;
import com.github.maxopoly.Kira.user.UserManager;

public class DeauthDiscordCommand extends Command {

	public DeauthDiscordCommand() {
		super("deauth", 1, 1, "unrole");
	}

	@Override
	public String execute(InputSupplier supplier, String[] args) {
		StringBuilder reply = new StringBuilder();
		UserManager userManager = KiraMain.getInstance().getUserManager();
		DiscordRoleManager authManager = KiraMain.getInstance().getRoleManager();
		User user = userManager.parseUser(args[0], reply);
		if (user == null) {
			reply.append("User not found, no action was taken\n");
		} else {
			boolean worked = authManager.takeDiscordRole(user);
			reply.append("Unregistered user with given id found in discord, role removal "
					+ (worked ? "successfull" : "unsuccessfull") + "\n");
			if (worked) {
				user.updateIngame(null, null);
				KiraMain.getInstance().getDAO().updateUser(user);
			}
		}
		return reply.toString();
	}

	@Override
	public String getUsage() {
		return "deauth [user]";
	}

	@Override
	public String getFunctionality() {
		return "Removes a users auth connection";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}

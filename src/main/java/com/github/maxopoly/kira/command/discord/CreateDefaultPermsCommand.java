package com.github.maxopoly.Kira.command.discord;

import com.github.maxopoly.Kira.command.model.discord.Command;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.KiraMain;

public class CreateDefaultPermsCommand extends Command {

	public CreateDefaultPermsCommand() {
		super("createdefaultperms");
	}

	@Override
	public String getFunctionality() {
		return "Initializes basic permissions";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "createdefaultperms";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		KiraMain.getInstance().getKiraRoleManager().setupDefaultPermissions();
		return "Setup basic permissions";
	}
}
package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class CreateDefaultPermsCommand extends Command {

	public CreateDefaultPermsCommand() {
		super("createdefaultperms", 0, 0);
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraMain.getInstance().getKiraRoleManager().setupDefaultPermissions();
		return "Setup basic permissions";
	}

	@Override
	public String getUsage() {
		return "createdefaultperms";
	}

	@Override
	public String getFunctionality() {
		return "Initializes basic permissions";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}
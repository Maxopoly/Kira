package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class SelfInfoCommand extends Command {

	public SelfInfoCommand() {
		super("whoami", 0, 0);
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		return "You are " + sender.getIdentifier();
	}

	@Override
	public String getUsage() {
		return "whoami";
	}

	@Override
	public String getFunctionality() {
		return "Shows your linked accounts";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

}

package com.github.maxopoly.kira.command.discord;

import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class SelfInfoCommand extends Command {

	public SelfInfoCommand() {
		super("whoami");
	}

	@Override
	public String getFunctionality() {
		return "Shows your linked accounts";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

	@Override
	public String getUsage() {
		return "whoami";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		return "You are " + sender.getIdentifier();
	}

}

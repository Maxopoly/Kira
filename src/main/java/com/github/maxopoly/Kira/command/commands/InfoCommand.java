package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class InfoCommand extends Command {

	public InfoCommand() {
		super("info", 0, 1000, "identify");
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		return "Hello, I am Kira. I facilitate communication between discord and minecraft servers. "
				+ "I was created by Maxopoly#3569 and my source code can be found here: https://github.com/maxopoly/kira";
	}

	@Override
	public String getUsage() {
		return "info";
	}

	@Override
	public String getFunctionality() {
		return "Prints basic info on the bot";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

}

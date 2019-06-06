package com.github.maxopoly.kira.command.discord;

import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class InfoCommand extends Command {

	public InfoCommand() {
		super("info", "identify");
	}

	@Override
	public String getFunctionality() {
		return "Prints basic info on the bot";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

	@Override
	public String getUsage() {
		return "info";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		return "Hello, I am Kira. I facilitate communication between discord and minecraft servers. "
				+ "I was created by Maxopoly#3569 and my source code can be found here: https://github.com/maxopoly/kira";
	}

}

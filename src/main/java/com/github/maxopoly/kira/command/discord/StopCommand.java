package com.github.maxopoly.kira.command.discord;

import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.KiraMain;

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", "end", "quit", "exit");
	}

	@Override
	public String getFunctionality() {
		return "Stops the bot completly";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "stop";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		KiraMain.getInstance().stop();
		return "Thank you and good bye";
	}

}

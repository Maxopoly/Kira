package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", 0, 0, "end", "quit", "exit");
	}

	@Override
	public String getUsage() {
		return "stop";
	}

	@Override
	public String getFunctionality() {
		return "Stops the bot completly";
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraMain.getInstance().stop();
		return "Thank you and good bye";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}

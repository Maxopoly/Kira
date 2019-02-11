package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.CommandHandler;
import com.github.maxopoly.Kira.command.InputSupplier;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", 0, 0, "commands", "list");
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		CommandHandler cmdHandler = KiraMain.getInstance().getCommandHandler();
		StringBuilder sb = new StringBuilder();
		cmdHandler.getAllInputs().forEach(cmd -> {
			if (!sender.hasPermission(cmd.getRequiredPermission())) {
				return;
			}
			sb.append(" - " + cmd.getUsage() + "\n");
			sb.append("      " + cmd.getFunctionality() + "\n");
			
		});
		return sb.toString();
	}

	@Override
	public String getUsage() {
		return "help";
	}

	@Override
	public String getFunctionality() {
		return "Shows all available commands";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

}

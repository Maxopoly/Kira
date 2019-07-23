package com.github.maxopoly.kira.command.discord.user;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.discord.CommandHandler;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class HelpCommand extends ArgumentBasedCommand {

	public HelpCommand() {
		super("help", 0, 1, "commands", "list");
	}

	@Override
	public String getFunctionality() {
		return "Shows all available commands";
	}

	@Override
	public String getRequiredPermission() {
		return "default";
	}

	@Override
	public String getUsage() {
		return "help (command)";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
		CommandHandler cmdHandler = KiraMain.getInstance().getCommandHandler();
		StringBuilder sb = new StringBuilder();
		if (args.length == 0) {
			List<Command> commands = new LinkedList<>(cmdHandler.getAllInputs());
			Collections.sort(commands, new Comparator<Command>() {

				@Override
				public int compare(Command o1, Command o2) {
					return o1.getIdentifier().compareTo(o2.getIdentifier());
				}
			});
			for (Command cmd : commands) {
				if (!sender.hasPermission(cmd.getRequiredPermission())) {
					continue;
				}
				sb.append(" - " + cmd.getUsage().split("\n")[0] + "\n");
				sb.append("      " + cmd.getFunctionality() + "\n");
			}
		} else {
			Command cmd = cmdHandler.getHandler(args[0]);
			if (cmd == null) {
				return "The command " + args[0] + " is not known";
			}
			sb.append(cmd.getFunctionality() + "\n");
			sb.append(cmd.getUsage() + "\n");
		}
		return sb.toString();
	}

}

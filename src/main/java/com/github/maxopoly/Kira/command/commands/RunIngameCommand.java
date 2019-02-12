package com.github.maxopoly.Kira.command.commands;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class RunIngameCommand extends Command {

	private String commandRegex = "[a-zA-Z0-9_- !?\\.]+";

	public RunIngameCommand() {
		super("ingame", 1, 100, "mc");
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		if (sender.getUser() == null) {
			return "You are not allowed to do that";
		}
		UUID uuid = sender.getUser().getIngameUUID();
		if (uuid == null) {
			return "You are not tied to an ingame account from which this command could be run";
		}
		StringBuilder sb = new StringBuilder();
		Arrays.stream(args).forEach(s -> sb.append(s + " "));
		String commandString = sb.toString().trim();
		if (!Pattern.matches(commandRegex, commandString)) {
			return "Your command contained illegal characters";
		}
		KiraMain.getInstance().getMCRabbitGateway().runCommand(uuid, commandString);
		return "Ran command '" + commandString + "' as " + sender.getUser().getName();
	}

	@Override
	public String getUsage() {
		return "ingame [command]";
	}

	@Override
	public String getFunctionality() {
		return "Allows you to run ingame commands from discord";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}
}

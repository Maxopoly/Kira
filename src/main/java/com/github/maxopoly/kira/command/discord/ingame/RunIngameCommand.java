package com.github.maxopoly.kira.command.discord.ingame;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.rabbit.session.SendIngameCommandSession;

public class RunIngameCommand extends ArgumentBasedCommand {

	public static final Pattern commandPattern = Pattern.compile("[a-zA-Z0-9_\\- !?\\.]+");

	public RunIngameCommand() {
		super("ingame", 1, 100, "mc");
		doesRequireIngameAccount();
	}

	@Override
	public String getFunctionality() {
		return "Allows you to run ingame commands from discord";
	}

	@Override
	public String getRequiredPermission() {
		return "ingame_command";
	}

	@Override
	public String getUsage() {
		return "ingame [command]";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
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
		
		if (!commandPattern.matcher(commandString).matches()) {
			return "Your command contained illegal characters";
		}
		if (commandString.length() > 255) {
			return "Your command is too long";
		}
		KiraMain.getInstance().getRequestSessionManager()
		.request(new SendIngameCommandSession(sender, commandString));
		return "Running command '" + commandString + "' as " + sender.getUser().getName();
	}
}

package com.github.maxopoly.kira.command.discord.ingame;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.rabbit.session.RunConsoleCommandRequest;

public class ConsoleCommand extends Command {

	public ConsoleCommand() {
		super("console");
		doesRequireIngameAccount();
	}

	@Override
	public String getFunctionality() {
		return "Runs a command on Minecraft as console";
	}

	@Override
	public String getRequiredPermission() {
		return "consoleop";
	}

	@Override
	public String getUsage() {
		return "console <console command>";
	}

	@Override
	protected String handleInternal(String argument, InputSupplier sender) {
		KiraMain.getInstance().getRequestSessionManager()
				.request(new RunConsoleCommandRequest(argument, sender.getUser().getIngameUUID(), sender));
		return "Running command `" + argument + "` as console";
	}

}

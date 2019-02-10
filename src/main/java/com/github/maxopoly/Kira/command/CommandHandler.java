package com.github.maxopoly.Kira.command;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.command.commands.AuthCommand;
import com.github.maxopoly.Kira.command.commands.DeauthDiscordCommand;
import com.github.maxopoly.Kira.command.commands.SelfInfoCommand;
import com.github.maxopoly.Kira.command.commands.StopCommand;
import com.github.maxopoly.Kira.command.commands.SyncUsernameCommand;

public class CommandHandler extends TextInputHandler {

	public CommandHandler(Logger logger) {
		super(logger);
	}

	protected void registerCommands() {
		registerCommand(new StopCommand());
		// registerCommand(new RunIngameCommand());
		registerCommand(new AuthCommand());
		registerCommand(new DeauthDiscordCommand());
		registerCommand(new SelfInfoCommand());
		registerCommand(new SyncUsernameCommand());
		logger.info("Loaded total of " + commands.values().size() + " commands");
	}

	@Override
	protected String getHandlerName() {
		return "Commandhandler";
	}

	@Override
	protected void handleInput(TextInput textInput, InputSupplier runner, String arguments) {
		Command comm = (Command) textInput;
		if (!runner.hasPermission(comm.getRequiredPermission())) {
			runner.reportBack("You don't have the required permission to do this");
			logger.info(runner.getIdentifier() + " attempted to run forbidden command: " + textInput.getIdentifier());
			return;
		}
		String[] args;
		if (arguments.equals("")) {
			args = new String[0];
		} else {
			args = arguments.split(" ");
		}
		if (args.length < comm.minimumArgs()) {
			runner.reportBack(comm.getIdentifier() + " requires at least " + comm.minimumArgs() + " parameter\nUsage: "
					+ comm.getUsage());
			return;
		}
		if (args.length > comm.maximumArgs()) {
			runner.reportBack(comm.getIdentifier() + " accepts at maximum " + comm.maximumArgs() + " parameter\nUsage: "
					+ comm.getUsage());
			return;
		}
		runner.reportBack(comm.execute(runner, args));

	}

	@Override
	protected void handleError(InputSupplier supplier, String input) {
		supplier.reportBack("Invalid command");
	}

}

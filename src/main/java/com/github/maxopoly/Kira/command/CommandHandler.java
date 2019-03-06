package com.github.maxopoly.Kira.command;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.commands.AuthCommand;
import com.github.maxopoly.Kira.command.commands.ChannelInfoCommand;
import com.github.maxopoly.Kira.command.commands.ConfigureRelayConfigCommand;
import com.github.maxopoly.Kira.command.commands.CreateDefaultPermsCommand;
import com.github.maxopoly.Kira.command.commands.CreateRelayChannelHereCommand;
import com.github.maxopoly.Kira.command.commands.CreateRelayConfig;
import com.github.maxopoly.Kira.command.commands.DeauthDiscordCommand;
import com.github.maxopoly.Kira.command.commands.DeleteRelayCommand;
import com.github.maxopoly.Kira.command.commands.GetWeightCommand;
import com.github.maxopoly.Kira.command.commands.GiveDefaultPermission;
import com.github.maxopoly.Kira.command.commands.GiveRoleCommand;
import com.github.maxopoly.Kira.command.commands.HelpCommand;
import com.github.maxopoly.Kira.command.commands.InfoCommand;
import com.github.maxopoly.Kira.command.commands.JoinDiscordCommand;
import com.github.maxopoly.Kira.command.commands.ListPermissionsForUserCommand;
import com.github.maxopoly.Kira.command.commands.QuoteCommand;
import com.github.maxopoly.Kira.command.commands.ReloadPermissionCommand;
import com.github.maxopoly.Kira.command.commands.RunIngameCommand;
import com.github.maxopoly.Kira.command.commands.SelfInfoCommand;
import com.github.maxopoly.Kira.command.commands.StopCommand;
import com.github.maxopoly.Kira.command.commands.SyncUsernameCommand;
import com.github.maxopoly.Kira.command.commands.TieRelayConfigCommand;

public class CommandHandler extends TextInputHandler<Command> {

	public CommandHandler(Logger logger) {
		super(logger);
	}

	protected void registerCommands() {
		registerCommand(new StopCommand());
		registerCommand(new RunIngameCommand());
		registerCommand(new AuthCommand());
		registerCommand(new DeauthDiscordCommand());
		registerCommand(new SelfInfoCommand());
		registerCommand(new SyncUsernameCommand());
		registerCommand(new ReloadPermissionCommand());
		registerCommand(new CreateDefaultPermsCommand());
		registerCommand(new HelpCommand());
		registerCommand(new GiveDefaultPermission());
		registerCommand(new ListPermissionsForUserCommand());
		registerCommand(new GiveRoleCommand());
		registerCommand(new JoinDiscordCommand());
		registerCommand(new CreateRelayChannelHereCommand());
		registerCommand(new GetWeightCommand());
		registerCommand(new QuoteCommand());
		registerCommand(new ConfigureRelayConfigCommand());
		registerCommand(new CreateRelayConfig());
		registerCommand(new TieRelayConfigCommand());
		registerCommand(new ChannelInfoCommand());
		registerCommand(new DeleteRelayCommand());
		registerCommand(new InfoCommand());
		logger.info("Loaded total of " + commands.values().size() + " commands");
	}

	public void registerCommand(Command command) {
		if (command.getRequiredPermission() != null) {
			KiraMain.getInstance().getKiraRoleManager().getOrCreatePermission(command.getRequiredPermission());
		}
		super.registerCommand(command);
	}

	@Override
	protected String getHandlerName() {
		return "Commandhandler";
	}

	@Override
	protected void handleInput(Command comm, InputSupplier runner, String arguments) {
		if (comm.doesRequireUser() && runner.getUser() == null) {
			runner.reportBack("You have to be a user to run this command");
			return;
		}
		if (comm.doesRequireIngameAccount() && !runner.getUser().hasIngameAccount()) {
			runner.reportBack("You need to have an ingame account linked to use this command");
			return;
		}
		if (!runner.hasPermission(comm.getRequiredPermission())) {
			runner.reportBack("You don't have the required permission to do this");
			logger.info(runner.getIdentifier() + " attempted to run forbidden command: " + comm.getIdentifier());
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
		try {
			runner.reportBack(comm.execute(runner, args));
		} catch (Exception e) {
			logger.error(
					"Exception occured when executing command " + comm.getIdentifier() + " with arguments " + arguments,
					e);
			runner.reportBack(
					"oopsie whoopsie! uwu we made a fucky wucky!!1 a wittle fucko boingo! the code monkies at our headquarters "
					+ "are working VEWY HAWD to fix this!");
		}

	}

	@Override
	protected void handleError(InputSupplier supplier, String input) {
		supplier.reportBack("Invalid command");
	}

}

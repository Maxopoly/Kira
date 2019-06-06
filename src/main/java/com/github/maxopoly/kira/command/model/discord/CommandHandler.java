package com.github.maxopoly.kira.command.model.discord;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.command.discord.AuthCommand;
import com.github.maxopoly.kira.command.discord.ChannelInfoCommand;
import com.github.maxopoly.kira.command.discord.ConfigureRelayConfigCommand;
import com.github.maxopoly.kira.command.discord.ConsoleCommand;
import com.github.maxopoly.kira.command.discord.CreateDefaultPermsCommand;
import com.github.maxopoly.kira.command.discord.CreateRelayChannelHereCommand;
import com.github.maxopoly.kira.command.discord.CreateRelayConfig;
import com.github.maxopoly.kira.command.discord.DeauthDiscordCommand;
import com.github.maxopoly.kira.command.discord.DeleteRelayCommand;
import com.github.maxopoly.kira.command.discord.GenerateAPIToken;
import com.github.maxopoly.kira.command.discord.GetWeightCommand;
import com.github.maxopoly.kira.command.discord.GiveDefaultPermission;
import com.github.maxopoly.kira.command.discord.GiveRoleCommand;
import com.github.maxopoly.kira.command.discord.HelpCommand;
import com.github.maxopoly.kira.command.discord.InfoCommand;
import com.github.maxopoly.kira.command.discord.JoinDiscordCommand;
import com.github.maxopoly.kira.command.discord.ListPermissionsForUserCommand;
import com.github.maxopoly.kira.command.discord.QuoteCommand;
import com.github.maxopoly.kira.command.discord.ReloadPermissionCommand;
import com.github.maxopoly.kira.command.discord.RunIngameCommand;
import com.github.maxopoly.kira.command.discord.SelfInfoCommand;
import com.github.maxopoly.kira.command.discord.StopCommand;
import com.github.maxopoly.kira.command.discord.SyncUsernameCommand;
import com.github.maxopoly.kira.command.discord.TieRelayConfigCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.command.model.top.TextInputHandler;
import com.github.maxopoly.kira.KiraMain;

public class CommandHandler extends TextInputHandler<Command, String, InputSupplier> {

	public CommandHandler(Logger logger) {
		super(logger);
	}

	@Override
	protected String convertIntoArgument(String raw) {
		return raw;
	}

	@Override
	protected String getCommandArguments(String fullArgument) {
		int index =  fullArgument.indexOf(' ');
		if (index == -1) {
			return "";
		}
		return fullArgument.substring(index + 1, fullArgument.length());
	}

	@Override
	protected String getCommandIdentifier(String argument) {
		int index =  argument.indexOf(' ');
		if (index == -1) {
			return argument;
		}
		return argument.substring(0, index);
	}

	@Override
	protected String getHandlerName() {
		return "Discord Command Handler";
	}

	@Override
	protected void handleError(InputSupplier supplier, String input) {
		supplier.reportBack("Invalid command");
	}

	@Override
	public void registerCommand(Command command) {
		if (command.getRequiredPermission() != null) {
			KiraMain.getInstance().getKiraRoleManager().getOrCreatePermission(command.getRequiredPermission());
		}
		super.registerCommand(command);
	}

	@Override
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
		registerCommand(new GenerateAPIToken());
		registerCommand(new ConsoleCommand());
		logger.info("Loaded total of " + commands.values().size() + " commands");
	}

}

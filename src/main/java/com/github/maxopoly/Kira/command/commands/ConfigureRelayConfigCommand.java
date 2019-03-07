package com.github.maxopoly.Kira.command.commands;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.relay.RelayConfig;
import com.github.maxopoly.Kira.relay.RelayConfigManager;
import com.github.maxopoly.Kira.relay.SkynetType;
import com.github.maxopoly.Kira.relay.SnitchHitType;
import com.github.maxopoly.Kira.user.KiraUser;

public class ConfigureRelayConfigCommand extends Command {

	public ConfigureRelayConfigCommand() {
		super("relayconfig", 1, 100);
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		if (sender.getUser() == null) {
			return "You can't do this";
		}
		StringBuilder reply = new StringBuilder();
		KiraUser user = sender.getUser();
		if (!user.hasIngameAccount()) {
			return "You need to link an ingame account first";
		}
		RelayConfigManager relayMan = KiraMain.getInstance().getRelayConfigManager();
		RelayConfig relay = relayMan.getByName(args[0]);
		if (relay == null) {
			return "No relay config with this name is known";
		}
		KiraUser owner = KiraMain.getInstance().getUserManager().getUser(relay.getOwnerID());
		if (args.length < 3) {
			reply.append("Relay config **" + relay.getName() + "** is owned by ");
			if (owner == null) {
				reply.append("unknown user");
			} else {
				reply.append(owner.toNiceString());
			}
			reply.append("\n - Relay chat from Discord to Minecraft (chatfromdiscord): "
					+ relay.shouldRelayFromDiscord() + "\n");
			reply.append(
					" - Relay chat from Minecraft to Discord (chattodiscord): " + relay.shouldRelayToDiscord() + "\n");
			reply.append(" - Relay snitch alerts to Discord (showsnitches): " + relay.shouldShowSnitches() + "\n");
			reply.append(
					" - Auto deletes discord messages (deletemessages): " + relay.shouldDeleteDiscordMessage() + "\n");
			reply.append(" - Format used for group chat messages (chatformat): " + relay.getChatFormat() + "\n");
			reply.append("    Example: " + relay.formatChatMessage("hello this is an example message", "playerName", "exampleGroup") + "\n");
			reply.append(" - Format used for snitch alerts (snitchformat): " + relay.getSnitchFormat() + "\n");
			reply.append("    Example: " + relay.formatSnitchOutput(420, 100, 420, "snitchName", "playerName", SnitchHitType.ENTER, "exampleGroup") + "\n");
			reply.append(
					" - Format used for entering a snitch range (snitchentermessage): " + relay.getSnitchEnterString() + "\n");
			reply.append(" - Format used for logins within a snitch range (snitchloginmessage): "
					+ relay.getSnitchLoginAction() + "\n");
			reply.append(" - Format used for logouts within a snitch range (snitchloginmessage): "
					+ relay.getSnitchLogoutAction() + "\n");
			reply.append(
					" - Regex which will be replaced with an @ here ping for both chat messages and snitch alerts (hereformat): "
							+ relay.getHereFormat() + "\n");
			reply.append(
					" - Regex which will be replaced with an @ everyone ping for both chat messages and snitch alerts (everyoneformat): "
							+ relay.getEveryoneFormat() + "\n");
			reply.append("- Time format used for the time stamps of messages (timeformat): " + relay.getTimeFormat() + "\n");
			reply.append("    Example: " + relay.getFormattedTime() + "\n");
			reply.append(" - Is allowed to use @ here and @ everyone (ping): " + relay.shouldPing() + "\n");
			reply.append(" - Relaying of logins/logout, referred to as Skynet enabled (skynetenabled): " + relay.isSkynetEnabled() + "\n");
			reply.append(" - Skynet format (skynetformat): " + relay.getSkynetFormat() + "\n");
			reply.append("    Example: " + relay.formatSkynetMessage("ttk2", SkynetType.LOGIN) + "\n");
			reply.append(" - Skynet login format (skynetloginformat): " + relay.getSkynetLoginString() + "\n");
			reply.append(" - Skynet logout format (skynetlogoutformat): " + relay.getSkynetLogoutString() + "\n");
			reply.append(" - Use \"help relayconfig\" for more information on how to configure these properties\n");
			return reply.toString();
		}
		if (relay.getOwnerID() != user.getID()) {
			return "You do not own this relay config and thus can not manage it";
		}
		reply.append("Found relay with name " + relay.getName() + " and confirmed permission check\n");
		final String property = args[1];
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		final String arguments = sb.toString().substring(0, sb.length() - 1);
		switch (property.toLowerCase()) {
		case "chattodiscord":
			Boolean chatToDiscord = attemptBooleanParsing(arguments, reply);
			if (chatToDiscord != null) {
				reply.append("Relaying chat from ingame to discord set to: " + chatToDiscord + "\n");
				relay.updateRelayToDiscord(chatToDiscord);
			}
			break;
		case "chatfromdiscord":
			Boolean chatFromDiscord = attemptBooleanParsing(arguments, reply);
			if (chatFromDiscord != null) {
				reply.append("Relaying chat from discord to ingame set to: " + chatFromDiscord + "\n");
				relay.updateRelayFromDiscord(chatFromDiscord);
			}
			break;
		case "showsnitches":
			Boolean showsnitches = attemptBooleanParsing(arguments, reply);
			if (showsnitches != null) {
				reply.append("Showing snitches set to: " + showsnitches + "\n");
				relay.updateShowSnitches(showsnitches);
			}
			break;
		case "deletemessages":
		case "deletediscordmessages":
			Boolean deletemessages = attemptBooleanParsing(arguments, reply);
			if (deletemessages != null) {
				reply.append("Deleting discord messages set to: " + deletemessages + "\n");
				relay.updateDeleteDiscordMessages(deletemessages);
			}
			break;
		case "chatformat":
			if (passLengthCheck(arguments, 512, reply)) {
				reply.append("Setting chat format to: " + arguments + "\n");
				relay.updateChatFormat(arguments);
				reply.append("Example chat message would look like this:\n");
				reply.append(relay.formatChatMessage("hello there, this is a message", "ttk2", "groupName"));
				reply.append('\n');
			}
			break;
		case "snitchformat":
			if (passLengthCheck(arguments, 512, reply)) {
				reply.append("Setting snitch alert format to: " + arguments + "\n");
				relay.setSnitchFormat(arguments);
				reply.append("Example snitch message would look like this:\n");
				reply.append(relay.formatSnitchOutput(420, 100, 420, "exampleSnitch", "ttk2", SnitchHitType.ENTER,
						"snitchGroup"));
				reply.append('\n');
			}
			break;
		case "snitchloginmessage":	
		case "loginstring":
		case "loginmessage":
			if (passLengthCheck(arguments, 256, reply)) {
				reply.append("Setting login message to: " + arguments + "\n");
				relay.updateLoginAction(arguments);
				reply.append("Example snitch message would look like this:\n");
				reply.append(relay.formatSnitchOutput(420, 100, 420, "exampleSnitch", "ttk2", SnitchHitType.LOGIN,
						"snitchGroup"));
			}
			break;
		case "snitchlogoutmessage":
		case "logoutstring":
		case "logoutmessage":
			if (passLengthCheck(arguments, 256, reply)) {
				reply.append("Setting logout message to: " + arguments + "\n");
				relay.updateLogoutAction(arguments);
				reply.append(relay.formatSnitchOutput(420, 100, 420, "exampleSnitch", "ttk2", SnitchHitType.LOGOUT,
						"snitchGroup"));
			}
			break;
		case "snitchentermessage":
		case "enterstring":
		case "entermessage":
			if (passLengthCheck(arguments, 256, reply)) {
				reply.append("Setting enter message to: " + arguments + "\n");
				relay.updateEnterAction(arguments);
				reply.append(relay.formatSnitchOutput(420, 100, 420, "exampleSnitch", "ttk2", SnitchHitType.ENTER,
						"snitchGroup"));
			}
			break;
		case "hereformat":
		case "here":
			if (passLengthCheck(arguments, 256, reply)) {
				try {
					Pattern.compile(arguments);
					reply.append("Setting here trigger to: " + arguments + "\n");
					relay.updateHereFormat(arguments);
				} catch (PatternSyntaxException e) {
					reply.append(arguments
							+ " is not a valid regex, see https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html "
							+ "for more information on how to write proper regex\n");
				}
			}
			break;
		case "everyoneformat":
		case "everyone":
			if (passLengthCheck(arguments, 256, reply)) {
				try {
					Pattern.compile(arguments);
					reply.append("Setting everyone trigger to: " + arguments + "\n");
					relay.updateEveryoneFormat(arguments);
				} catch (PatternSyntaxException e) {
					reply.append(arguments
							+ " is not a valid regex, see https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html "
							+ "for more information on how to write proper regex\n");
				}
			}
			break;
		case "ping":
		case "shouldping":
			Boolean shouldPing = attemptBooleanParsing(arguments, reply);
			if (shouldPing != null) {
				reply.append("Setting pinging to: " + arguments + "\n");
				relay.updateShouldPing(shouldPing);
			}
			break;
		case "timeformat":
			try {
				DateTimeFormatter.ofPattern(arguments);
				relay.updateTimeFormat(arguments);
				reply.append("Setting time format to: " + arguments + "\n");
				reply.append("Example time stamp: " + relay.getFormattedTime() + "\n");
			} catch (IllegalArgumentException e) {
				reply.append(arguments
						+ " is not a valid time format, see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html "
						+ "for more information on how to format time properly\n");
			}
			break;
		case "skynetenabled":
			Boolean skynetEnabled = attemptBooleanParsing(arguments, reply);
			if (skynetEnabled != null) {
				relay.updateSkynetEnabled(skynetEnabled);
				reply.append("Setting skynet status to: " + relay.isSkynetEnabled());
			}
			break;
		case "skynetformat":
			if (passLengthCheck(arguments, 512, reply)) {
				reply.append("Setting skynet format to: " + arguments + "\n");
				relay.updateSkynetFormat(arguments);
				reply.append("Example skynet message would look like this:\n");
				reply.append(relay.formatSkynetMessage("ttk2", SkynetType.LOGIN));
				reply.append('\n');
			}
			break;
		case "skynetloginformat":
		case "skynetloginstring":
			if (passLengthCheck(arguments, 256, reply)) {
				reply.append("Setting skynet login format to: " + arguments + "\n");
				relay.updateSkynetLoginString(arguments);
				reply.append("Example skynet login message would look like this:\n");
				reply.append(relay.formatSkynetMessage("ttk2", SkynetType.LOGIN));
				reply.append('\n');
			}
			break;
		case "skynetlogoutformat":
		case "skynetlogoutstring":
			if (passLengthCheck(arguments, 256, reply)) {
				reply.append("Setting skynet logout format to: " + arguments + "\n");
				relay.updateSkynetLogoutString(arguments);
				reply.append("Example skynet logout message would look like this:\n");
				reply.append(relay.formatSkynetMessage("ttk2", SkynetType.LOGOUT));
				reply.append('\n');
			}
			break;	
		default:
			reply.append(property
					+ " is not a valid property to configure, see the command description for more information");
		}
		return reply.toString();
	}

	private Boolean attemptBooleanParsing(String input, StringBuilder sb) {
		Boolean bool = null;
		switch (input.toLowerCase()) {
		case "0":
		case "false":
		case "f":
		case "no":
			bool = false;
			break;
		case "1":
		case "true":
		case "t":
		case "y":
			bool = true;
		}
		if (bool == null) {
			sb.append("Could not parse " + input + " as boolean\n");
		}
		return bool;
	}

	private boolean passLengthCheck(String input, int maxLength, StringBuilder reply) {
		if (input.length() > maxLength) {
			reply.append("Input exceeded the maximum allowed length. Allowed is up to " + maxLength + ", but got "
					+ input.length());
			return false;
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "relayconfig [name]\n" + "relayconfig [name] chatToDiscord [true|false]\n"
				+ "relayconfig [name] chatFromDiscord [true|false]\n" + "relayconfig [name] showsnitches [true|false]\n"
				+ "relayconfig [name] deletemessages [true|false]\n"
				+ "relayconfig [name] chatformat [your chat message]\n"
				+ "relayconfig [name] snitchformat [your snitch message]\n"
				+ "relayconfig [name] snitchloginmessage [your login message]\n"
				+ "relayconfig [name] snitchlogoutmessage [your logout message]\n"
				+ "relayconfig [name] snitchentermessage [your enter message]\n"
				+ "relayconfig [name] hereformat [your here regex]\n"
				+ "relayconfig [name] everyoneformat [your everyone regex]\n"
				+ "relayconfig [name] shouldping [true|false]\n" 
				+ "relayconfig [name] skynetloginformat [your login message]\n"
				+ "relayconfig [name] skynetlogoutformat [your login message]\n"
				+ "relayconfig [name] skynetformat [your skynet format]\n"
				+ "relayconfig [name] skynetenabled [true|false]\n"
				+ "relayconfig [name] timeformat [your time format like HH:mm:ss]";
	}

	@Override
	public String getFunctionality() {
		return "Configures properties of a relay config";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}

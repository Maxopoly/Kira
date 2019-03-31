package com.github.maxopoly.Kira.relay;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.MinecraftLocation;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;

public class RelayConfig {

	private int id;
	private String name;
	private boolean relayFromDiscord;
	private boolean relayToDiscord;
	private boolean showSnitches;
	private boolean deleteDiscordMessages;
	private String chatFormat;
	private String snitchHitFormat;
	private String snitchLoginString;
	private String snitchLogoutString;
	private String snitchEnterString;
	private String hereFormat;
	private Pattern herePattern;
	private String everyoneFormat;
	private Pattern everyonePattern;
	private String timeFormat;
	private DateTimeFormatter timeFormatter;
	private boolean shouldPing;
	private String skynetLoginString;
	private String skynetLogoutString;
	private String skynetFormat;
	private boolean skynetEnabled;
	private int ownerID;

	public RelayConfig(int id, String name, boolean relayFromDiscord, boolean relayToDiscord, boolean showSnitches,
			boolean deleteDiscordMessages, String snitchHitFormat, String loginString, String logoutString,
			String enterString, String chatFormat, String hereFormat, String everyoneFormat, boolean shouldPing,
			String timeFormat, String skynetLoginString, String skynetLogoutString, String skynetFormat,
			boolean skynetEnabled, int ownerID) {
		this.relayFromDiscord = relayFromDiscord;
		this.id = id;
		this.name = name;
		this.relayToDiscord = relayToDiscord;
		this.showSnitches = showSnitches;
		this.deleteDiscordMessages = deleteDiscordMessages;
		this.snitchHitFormat = snitchHitFormat;
		this.snitchLoginString = loginString;
		this.ownerID = ownerID;
		this.snitchLogoutString = logoutString;
		this.snitchEnterString = enterString;
		this.hereFormat = hereFormat;
		this.everyoneFormat = everyoneFormat;
		this.shouldPing = shouldPing;
		this.chatFormat = chatFormat;
		this.ownerID = ownerID;
		this.timeFormat = timeFormat;
		this.timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
		this.skynetLoginString = skynetLoginString;
		this.skynetLogoutString = skynetLogoutString;
		this.skynetFormat = skynetFormat;
		this.skynetEnabled = skynetEnabled;
		this.herePattern = Pattern.compile(hereFormat);
		this.everyonePattern = Pattern.compile(everyoneFormat);
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSnitchFormat() {
		return snitchHitFormat;
	}

	public void setSnitchFormat(String snitchFormat) {
		this.snitchHitFormat = snitchFormat;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSnitchLoginAction() {
		return snitchLoginString;
	}

	public void updateLoginAction(String loginString) {
		this.snitchLoginString = loginString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSnitchLogoutAction() {
		return snitchLogoutString;
	}

	public void updateLogoutAction(String logoutString) {
		this.snitchLogoutString = logoutString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSnitchEnterString() {
		return snitchEnterString;
	}

	public void updateEnterAction(String enterString) {
		this.snitchEnterString = enterString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getChatFormat() {
		return chatFormat;
	}

	public void updateChatFormat(String chatFormat) {
		this.chatFormat = chatFormat;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getHereFormat() {
		return hereFormat;
	}

	public void updateHereFormat(String hereFormat) {
		this.hereFormat = hereFormat;
		this.herePattern = Pattern.compile(hereFormat);
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getEveryoneFormat() {
		return everyoneFormat;
	}

	public void updateEveryoneFormat(String everyoneFormat) {
		this.everyoneFormat = everyoneFormat;
		this.everyonePattern = Pattern.compile(everyoneFormat);
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public boolean shouldPing() {
		return shouldPing;
	}

	public void updateShouldPing(boolean shouldPing) {
		this.shouldPing = shouldPing;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public boolean shouldRelayFromDiscord() {
		return relayFromDiscord;
	}

	public void updateRelayFromDiscord(boolean relayFromDiscord) {
		this.relayFromDiscord = relayFromDiscord;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public boolean shouldRelayToDiscord() {
		return relayToDiscord;
	}

	public void updateRelayToDiscord(boolean relayToDiscord) {
		this.relayToDiscord = relayToDiscord;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public boolean shouldShowSnitches() {
		return showSnitches;
	}

	public void updateShowSnitches(boolean showSnitches) {
		this.showSnitches = showSnitches;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public boolean shouldDeleteDiscordMessage() {
		return deleteDiscordMessages;
	}

	public void updateDeleteDiscordMessages(boolean deleteDiscordMessages) {
		this.deleteDiscordMessages = deleteDiscordMessages;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSkynetLoginString() {
		return skynetLoginString;
	}

	public String getSkynetLogoutString() {
		return skynetLogoutString;
	}

	public String getSkynetFormat() {
		return skynetFormat;
	}

	public boolean isSkynetEnabled() {
		return skynetEnabled;
	}

	public void updateSkynetLoginString(String skynetLoginString) {
		this.skynetLoginString = skynetLoginString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public void updateSkynetLogoutString(String skynetLogoutString) {
		this.skynetLogoutString = skynetLogoutString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public void updateSkynetFormat(String skynetFormat) {
		this.skynetFormat = skynetFormat;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public void updateSkynetEnabled(boolean skynetEnabled) {
		this.skynetEnabled = skynetEnabled;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void updateTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		this.timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public int getOwnerID() {
		return ownerID;
	}

	public String formatSkynetMessage(SkynetAction action) {
		String output = skynetFormat;
		String actionString;
		switch (action.getType()) {
		case LOGIN:
			actionString = skynetLoginString;
			break;
		case LOGOUT:
			actionString = skynetLogoutString;
			break;
		default:
			throw new IllegalArgumentException();
		}
		output = output.replace("%ACTION%", actionString);
		output = output.replace("%PLAYER%", action.getPlayer());
		output = output.replace("%TIME%", getFormattedTime(action.getTimeStamp()));
		output = reformatPings(output);
		return output;
	}

	public String formatChatMessage(GroupChatMessageAction action) {
		String output = chatFormat;
		output = output.replace("%PLAYER%", action.getSender());
		output = output.replace("%MESSAGE%", action.getMessage());
		output = output.replace("%GROUP%", action.getGroupName());
		output = output.replace("%TIME%", getFormattedTime(action.getTimeStamp()));
		output = reformatPings(output);
		return output;
	}

	public String getFormattedTime(long unixMilli) {
		return timeFormatter.format(Instant.ofEpochMilli(unixMilli));
	}

	public String formatSnitchOutput(PlayerHitSnitchAction action) {
		String output = snitchHitFormat;
		String actionString;
		switch (action.getHitType()) {
		case ENTER:
			actionString = snitchEnterString;
			break;
		case LOGIN:
			actionString = snitchLoginString;
			break;
		case LOGOUT:
			actionString = snitchLogoutString;
			break;
		default:
			throw new IllegalArgumentException();
		}
		MinecraftLocation loc = action.getLocation();
		output = output.replace("%ACTION%", actionString);
		output = output.replace("%X%", String.valueOf(loc.getX()));
		output = output.replace("%Y%", String.valueOf(loc.getY()));
		output = output.replace("%Z%", String.valueOf(loc.getZ()));
		output = output.replace("%SNITCH%", action.getSnitchName());
		output = output.replaceAll("%PLAYER%", action.getPlayerName());
		output = output.replace("%GROUP%", action.getGroupName());
		output = output.replace("%TIME%", getFormattedTime(action.getTimeStamp()));
		output = reformatPings(output);
		return output;
	}

	private String reformatPings(String output) {
		if (!shouldPing) {
			// only remove existing pings
			output = output.replace("@here", "@;here");
			output = output.replace("@everyone", "@;everyone");
			return output;
		}
		DiscordPing ping = DiscordPing.NONE;
		if (output.matches(hereFormat) || herePattern.matcher(output).find()) {
			ping = DiscordPing.HERE;
		}
		if (output.matches(everyoneFormat) || everyonePattern.matcher(output).find()) {
			ping = DiscordPing.EVERYONE;
		}
		output = output.replace("%PING%", ping.toString());
		return output;
	}

}

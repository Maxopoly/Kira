package com.github.maxopoly.Kira.relay;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.relay.actions.SkynetType;
import com.github.maxopoly.Kira.relay.actions.SnitchHitType;

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
	private String everyoneFormat;
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
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getEveryoneFormat() {
		return everyoneFormat;
	}

	public void updateEveryoneFormat(String everyoneFormat) {
		this.everyoneFormat = everyoneFormat;
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

	public String formatSkynetMessage(String player, SkynetType type) {
		String output = skynetFormat;
		String action;
		switch (type) {
		case LOGIN:
			action = skynetLoginString;
			break;
		case LOGOUT:
			action = skynetLogoutString;
			break;
		default:
			throw new IllegalArgumentException("Invalid type :" + type);
		}
		output = output.replace("%ACTION%", action);
		output = output.replace("%PLAYER%", player);
		output = output.replace("%TIME%", getFormattedTime());
		output = reformatPings(output);
		return output;
	}

	public String formatChatMessage(String msg, String sender, String groupName) {
		String output = chatFormat;
		output = output.replace("%PLAYER%", sender);
		output = output.replace("%MESSAGE%", msg);
		output = output.replace("%GROUP%", groupName);
		output = output.replace("%TIME%", getFormattedTime());
		output = reformatPings(output);
		return output;
	}

	public String getFormattedTime() {
		return timeFormatter.format(ZonedDateTime.now());
	}

	public String formatSnitchOutput(int x, int y, int z, String snitchName, String player, SnitchHitType type,
			String groupName) {
		String output = snitchHitFormat;
		String action;
		switch (type) {
		case ENTER:
			action = snitchEnterString;
			break;
		case LOGIN:
			action = snitchLoginString;
			break;
		case LOGOUT:
			action = snitchLogoutString;
			break;
		default:
			throw new IllegalArgumentException("Invalid type :" + type);
		}
		output = output.replace("%ACTION%", action);
		output = output.replace("%X%", String.valueOf(x));
		output = output.replace("%Y%", String.valueOf(y));
		output = output.replace("%Z%", String.valueOf(z));
		output = output.replace("%SNITCH%", snitchName);
		output = output.replaceAll("%PLAYER%", player);
		output = output.replace("%GROUP%", groupName);
		output = output.replace("%TIME%", getFormattedTime());
		output = reformatPings(output);
		return output;
	}

	private String reformatPings(String output) {
		if (shouldPing) {
			output = output.replaceAll(hereFormat, "@here");
			output = output.replaceAll(everyoneFormat, "@everyone");
		}
		String ping = "";
		if (output.contains("@here")) {
			ping = "@here";
		}
		if (output.contains("@everyone")) {
			ping = "@everyone";
		}
		output = output.replace("%PING%", ping);
		if (!shouldPing) {
			output = output.replace("@here", "@;here");
			output = output.replace("@everyone", "@;everyone");
		}
		return output;
	}

}

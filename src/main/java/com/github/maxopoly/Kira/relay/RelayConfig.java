package com.github.maxopoly.Kira.relay;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.github.maxopoly.Kira.KiraMain;

public class RelayConfig {

	private int id;
	private String name;
	private boolean relayFromDiscord;
	private boolean relayToDiscord;
	private boolean showSnitches;
	private boolean deleteDiscordMessages;
	private String chatFormat;
	private String snitchHitFormat;
	private String loginString;
	private String logoutString;
	private String enterString;
	private String hereFormat;
	private String everyoneFormat;
	private String timeFormat;
	private DateTimeFormatter timeFormatter;
	private boolean shouldPing;
	private int ownerID;

	public RelayConfig( int id, String name, boolean relayFromDiscord, boolean relayToDiscord, boolean showSnitches,
			boolean deleteDiscordMessages, String snitchHitFormat, String loginString, String logoutString,String enterString, 
			String chatFormat, String hereFormat, String everyoneFormat, boolean shouldPing, String timeFormat, int ownerID) {
		this.relayFromDiscord = relayFromDiscord;
		this.id = id;
		this.name = name;
		this.relayToDiscord = relayToDiscord;
		this.showSnitches = showSnitches;
		this.deleteDiscordMessages = deleteDiscordMessages;
		this.snitchHitFormat = snitchHitFormat;
		this.loginString = loginString;
		this.ownerID = ownerID;
		this.logoutString = logoutString;
		this.enterString = enterString;
		this.hereFormat = hereFormat;
		this.everyoneFormat = everyoneFormat;
		this.shouldPing = shouldPing;
		this.chatFormat = chatFormat;
		this.ownerID = ownerID;
		this.timeFormat = timeFormat;
		this.timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
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
		return loginString;
	}

	public void updateLoginAction(String loginString) {
		this.loginString = loginString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSnitchLogoutAction() {
		return logoutString;
	}

	public void updateLogoutAction(String logoutString) {
		this.logoutString = logoutString;
		KiraMain.getInstance().getDAO().updateRelayConfig(this);
	}

	public String getSnitchEnterString() {
		return enterString;
	}

	public void updateEnterAction(String enterString) {
		this.enterString = enterString;
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

	public String formatChatMessage(String msg, String sender, String groupName) {
		String output = chatFormat;
		output = output.replace("%PLAYER%", sender);
		output = output.replace("%MESSAGE%", msg);
		output = output.replace("%GROUP%", groupName);
		output = output.replace("%TIME%", timeFormatter.format(ZonedDateTime.now()));
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
			action = enterString;
			break;
		case LOGIN:
			action = loginString;
			break;
		case LOGOUT:
			action = logoutString;
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
		if (shouldPing) {
			output = output.replaceAll(hereFormat, "@here");
			output = output.replaceAll(everyoneFormat, "@everyone");
		} else {
			output = output.replace("@here", "X");
			output = output.replace("@everyone", "X");
		}
		return output;
	}

}

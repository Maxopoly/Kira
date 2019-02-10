package com.github.maxopoly.Kira.user;

import java.util.UUID;

public class User {

	private int id;
	private String name;
	private long discordID;
	private UUID uuid;
	private String redditAccount;

	public User(int id, String name, long discordID, UUID uuid, String redditAccount) {
		this.id = id;
		this.name = name;
		this.discordID = discordID;
		this.uuid = uuid;
		this.redditAccount = redditAccount;
	}

	public long getDiscordID() {
		return discordID;
	}

	public boolean hasDiscord() {
		return discordID > 0;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public UUID getIngameUUID() {
		return uuid;
	}

	public boolean hasIngameAccount() {
		return uuid != null;
	}

	public String getRedditAccount() {
		return redditAccount;
	}

	public void updateIngame(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", discordId: ");
		result.append(discordID);
		result.append(", uuid: ");
		result.append(uuid == null ? "null" : uuid.toString());
		result.append("}");
		return result.toString();
	}
}

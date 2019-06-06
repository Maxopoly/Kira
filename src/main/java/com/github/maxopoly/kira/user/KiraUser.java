package com.github.maxopoly.kira.user;

import java.util.UUID;

public class KiraUser {

	private int id;
	private String name;
	private long discordID;
	private UUID uuid;
	private String redditAccount;

	public KiraUser(int id, String name, long discordID, UUID uuid, String redditAccount) {
		this.id = id;
		this.name = name;
		this.discordID = discordID;
		this.uuid = uuid;
		this.redditAccount = redditAccount;
	}

	public long getDiscordID() {
		return discordID;
	}

	public int getID() {
		return id;
	}

	public UUID getIngameUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getRedditAccount() {
		return redditAccount;
	}

	public boolean hasDiscord() {
		return discordID > 0;
	}

	public boolean hasIngameAccount() {
		return uuid != null;
	}

	public String toNiceString() {
		if (hasIngameAccount()) {
			return name;
		}
		return "id:" + id;
	}

	@Override
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

	public void updateIngame(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
}

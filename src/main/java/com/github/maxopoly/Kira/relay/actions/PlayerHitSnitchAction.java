package com.github.maxopoly.Kira.relay.actions;

import org.json.JSONObject;

public class PlayerHitSnitchAction extends MinecraftAction {

	private String playerName;
	private String snitchName;
	private MinecraftLocation location;
	private SnitchHitType hitType;
	private String groupName;
	private SnitchType snitchType;

	public PlayerHitSnitchAction(long timestamp, String playerName, String snitchname, String groupName,
			MinecraftLocation location, SnitchHitType hitType, SnitchType snitchType) {
		super(timestamp);
		this.playerName = playerName;
		this.snitchName = snitchname;
		this.location = location;
		this.hitType = hitType;
		this.groupName = groupName;
		this.snitchType = snitchType;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getSnitchName() {
		return snitchName;
	}

	public MinecraftLocation getLocation() {
		return location;
	}

	public SnitchHitType getHitType() {
		return hitType;
	}

	@Override
	protected void internalConstructJSON(JSONObject json) {
		json.put("player", playerName);
		json.put("action", hitType.toString());
		JSONObject snitch = new JSONObject();
		snitch.put("name", snitchName);
		snitch.put("group", groupName);
		snitch.put("type", snitchType.toString());
		snitch.put("location", location.toJson());
		json.put("snitch", snitch);
	}

}

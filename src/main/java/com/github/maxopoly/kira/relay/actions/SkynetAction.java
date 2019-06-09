package com.github.maxopoly.kira.relay.actions;

import org.json.JSONObject;

public class SkynetAction extends MinecraftAction {

	private SkynetType type;
	private String player;

	public SkynetAction(long timestamp, String player, SkynetType type) {
		super(timestamp);
		this.player = player;
		this.type = type;
	}

	public String getPlayer() {
		return player;
	}

	public SkynetType getType() {
		return type;
	}

	@Override
	protected void internalConstructJSON(JSONObject json) {
		json.put("player", player);
		json.put("action", type.toString());
	}

}

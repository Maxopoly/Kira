package com.github.maxopoly.kira.relay.actions;

import org.json.JSONObject;

public class NewPlayerAction extends MinecraftAction {

	private String player;

	public NewPlayerAction(long timestamp, String player) {
		super(timestamp);
		this.player = player;
	}

	public String getPlayer() {
		return player;
	}

	@Override
	protected void internalConstructJSON(JSONObject json) {
		json.put("player", player);
	}

}

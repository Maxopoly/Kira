package com.github.maxopoly.Kira.relay.actions;

import org.json.JSONObject;

/**
 * Something that happened in Minecraft like a player loggin in, hitting a snitch etc.
 *
 */
public abstract class MinecraftAction {
	
	private long timeStamp;
	private JSONObject json;
	
	public MinecraftAction(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public JSONObject getJSON() {
		if (json != null) {
			return json;
		}
		json = constructJSON();
		return json;
	}
	
	private final JSONObject constructJSON() {
		JSONObject json = new JSONObject();
		json.put("time", timeStamp);
		internalConstructJSON(json);
		return json;
	}
	
	protected abstract void internalConstructJSON(JSONObject json);

}

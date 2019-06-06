package com.github.maxopoly.kira.rabbit.session;

import java.util.UUID;

import org.json.JSONObject;

public abstract class PermissionCheckSession extends RequestSession {

	private UUID playerUUID;
	private String group;
	private String permission;

	public PermissionCheckSession(UUID player, String group, String permission) {
		super("permissioncheck");
		this.playerUUID = player;
		this.group = group;
		this.permission = permission;
	}

	@Override
	public JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("player", playerUUID.toString());
		json.put("group", group);
		json.put("permission", permission);
		return json;
	}

	public abstract void handlePermissionReply(boolean hasPerm);

	@Override
	public void handleReply(JSONObject json) {
		boolean hasPermission = json.getBoolean("hasPermission");
		handlePermissionReply(hasPermission);
	}

}

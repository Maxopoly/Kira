package com.github.maxopoly.kira.relay.actions;

import org.json.JSONObject;

public class GroupChatMessageAction extends MinecraftAction {
	
	private String group;
	private String sender;
	private String message;

	public GroupChatMessageAction(long timeStamp, String group, String sender, String message) {
		super(timeStamp);
		this.group = group;
		this.sender = sender;
		this.message = message;
	}
	
	public String getGroupName() {
		return group;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getSender() {
		return sender;
	}

	@Override
	protected void internalConstructJSON(JSONObject json) {
		json.put("group", group);
		json.put("player", sender);
		json.put("message", message);		
	}

}

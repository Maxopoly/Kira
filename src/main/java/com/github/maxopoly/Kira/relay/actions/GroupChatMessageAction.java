package com.github.maxopoly.Kira.relay.actions;

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
	
	public String getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}

}

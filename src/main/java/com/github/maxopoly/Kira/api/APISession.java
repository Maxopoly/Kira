package com.github.maxopoly.Kira.api;

import java.util.List;

import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;
import com.github.maxopoly.Kira.user.KiraUser;

public class APISession {

	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean skyNet;
	// -1 means never
	private long expirationTime;
	
	public APISession(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime) {
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.skyNet = skyNet;
		this.expirationTime = expirationTime;
	}
	
	public KiraUser getUser() {
		return user;
	}
	
	public List<String> getSnitchGroups() {
		return snitchGroups;
	}
	
	public List<String> getChatGroups() {
		return chatGroups;
	}
	
	public boolean receivesSkynet() {
		return skyNet;
	}

	public void sendSnitchAlert(PlayerHitSnitchAction action) {

	}

	public void sendGroupChatMessage(GroupChatMessageAction action) {

	}
	
	public void sendSkynetAlert(SkynetAction action) {
		
	}

	public boolean isClosed() {
		return true;
	}

}

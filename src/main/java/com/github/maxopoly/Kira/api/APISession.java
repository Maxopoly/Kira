package com.github.maxopoly.Kira.api;

import java.util.List;

import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;
import com.github.maxopoly.Kira.user.KiraUser;

public class APISession {

	private String token;
	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean skyNet;
	// -1 means never
	private long expirationTime;

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

package com.github.maxopoly.Kira.api;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;
import com.github.maxopoly.Kira.user.KiraUser;

public class APISession {

	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean receivesSkynet;
	// -1 means never
	private long expirationTime;
	private KiraAPIConnection connection;

	private List<PlayerHitSnitchAction> snitchHits;
	private List<GroupChatMessageAction> groupMessages;
	private List<SkynetAction> skynets;

	public APISession(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime, KiraAPIConnection connection) {
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.receivesSkynet = skyNet;
		this.expirationTime = expirationTime;
		this.snitchHits = new LinkedList<>();
		this.groupMessages = new LinkedList<>();
		this.skynets = new LinkedList<>();
		this.connection = connection;
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
		return receivesSkynet;
	}

	public void sendSnitchAlert(PlayerHitSnitchAction action) {
		synchronized (snitchHits) {
			snitchHits.add(action);
		}
	}

	public void sendGroupChatMessage(GroupChatMessageAction action) {
		synchronized (groupMessages) {
			groupMessages.add(action);
		}
	}

	public void sendSkynetAlert(SkynetAction action) {
		synchronized (skynets) {
			skynets.add(action);
		}
	}

	public boolean hasPendingNotifications() {
		return !(snitchHits.isEmpty() && groupMessages.isEmpty() && skynets.isEmpty());
	}

	public JSONObject popPendingNotifications() {
		JSONObject json = new JSONObject();
		json.put("type", "data");
		synchronized (snitchHits) {
			if (!snitchHits.isEmpty()) {
				JSONArray snitchArray = new JSONArray();
				for(PlayerHitSnitchAction action : snitchHits) {
					snitchArray.put(action.getJSON());
				}
				json.put("snitch-alerts", snitchArray);
				snitchHits.clear();
			}
		}
		synchronized (groupMessages) {
			if (!groupMessages.isEmpty()) {
				JSONArray chatArray = new JSONArray();
				for(GroupChatMessageAction action : groupMessages) {
					chatArray.put(action.getJSON());
				}
				json.put("group-messages", chatArray);
				groupMessages.clear();
			}
		}
		synchronized (skynets) {
			if (!skynets.isEmpty()) {
				JSONArray skyArray = new JSONArray();
				for(SkynetAction action : skynets) {
					skyArray.put(action.getJSON());
				}
				json.put("group-messages", groupMessages);
				skynets.clear();
			}
		}
		return json;
	}

	public boolean isClosed() {
		return connection.isClosed();
	}
	
	public boolean hasExpired() {
		if (expirationTime == -1) {
			return false;
		}
		return System.currentTimeMillis() > expirationTime;
	}

}

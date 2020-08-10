package com.github.maxopoly.kira.api;

import java.util.LinkedList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.kira.api.token.APIToken;
import com.github.maxopoly.kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.kira.relay.actions.NewPlayerAction;
import com.github.maxopoly.kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.kira.relay.actions.SkynetAction;
import com.github.maxopoly.kira.user.KiraUser;

public class APISession {

	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean receivesSkynet;
	// -1 means never
	private long expirationTime;
	private WebSocket connection;

	private List<PlayerHitSnitchAction> snitchHits;
	private List<GroupChatMessageAction> groupMessages;
	private List<SkynetAction> skynets;
	private List<NewPlayerAction> newPlayers;

	public APISession(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime, WebSocket connection) {
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.receivesSkynet = skyNet;
		this.expirationTime = expirationTime;
		this.snitchHits = new LinkedList<>();
		this.groupMessages = new LinkedList<>();
		this.skynets = new LinkedList<>();
		this.newPlayers = new LinkedList<>();
		this.connection = connection;
	}

	public void close() {
		if (!isClosed()) {
			//flush out
			if (hasPendingNotifications()) {
				popAndSendPendingNotifications();
			}
			this.connection.close();
		}
	}

	public List<String> getChatGroups() {
		return chatGroups;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public List<String> getSnitchGroups() {
		return snitchGroups;
	}
	
	public KiraUser getUser() {
		return user;
	}

	public boolean hasExpired() {
		if (expirationTime == -1) {
			return false;
		}
		return System.currentTimeMillis() > expirationTime;
	}

	public boolean hasPendingNotifications() {
		return !(snitchHits.isEmpty() && groupMessages.isEmpty()
				&& skynets.isEmpty() && newPlayers.isEmpty());
	}

	public boolean isClosed() {
		return !connection.isOpen();
	}

	public void popAndSendPendingNotifications() {
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
				json.put("skynet", skyArray);
				skynets.clear();
			}
		}
		synchronized (newPlayers) {
			if (!newPlayers.isEmpty()) {
				JSONArray newPlayersArray = new JSONArray();
				for(NewPlayerAction action : newPlayers) {
					newPlayersArray.put(action.getJSON());
				}
				json.put("new-players", newPlayersArray);
				newPlayers.clear();
			}
		}
		connection.send(json.toString());
	}

	public boolean receivesSkynet() {
		return receivesSkynet;
	}

	public void sendAuthMessage() {
		JSONObject json = new JSONObject();
		json.put("type", "auth");
		json.put("valid", true);
		json.put("expires",  expirationTime);
		json.put("chats", getChatGroups());
		json.put("snitches", getSnitchGroups());
		json.put("skynet", receivesSkynet());
		connection.send(json.toString());
	}
	
	public void sendReplacementToken(APIToken token) {
		JSONObject json = new JSONObject();
		json.put("type", "new-token");
		json.put("secret", token.getSecret());
		json.put("expires", token.getExpirationTime());
		connection.send(json.toString());
	}
	
	public void sendInGameCommandResponse(String identifier, String response) {
		JSONObject json = new JSONObject();
		json.put("type", "ingame-response");
		json.put("identifier", identifier);
		json.put("response", response);
		connection.send(json.toString());
	}

	public void sendGroupChatMessage(GroupChatMessageAction action) {
		synchronized (groupMessages) {
			groupMessages.add(action);
		}
	}

	public void sendNewPlayerAlert(NewPlayerAction action) {
		synchronized (newPlayers) {
			newPlayers.add(action);
		}
	}

	public void sendSkynetAlert(SkynetAction action) {
		synchronized (skynets) {
			skynets.add(action);
		}
	}

	public void sendSnitchAlert(PlayerHitSnitchAction action) {
		synchronized (snitchHits) {
			snitchHits.add(action);
		}
	}

}

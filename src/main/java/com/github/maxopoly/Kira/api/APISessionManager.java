package com.github.maxopoly.Kira.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.api.input.APIInputHandler;
import com.github.maxopoly.Kira.api.token.APITokenManager;
import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.MinecraftAction;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;

public class APISessionManager {

	private Set<APISession> sessions;
	private Map<String, List<APISession>> chatTakers;
	private Map<String, List<APISession>> snitchTakers;
	private List<APISession> skynetTakers;
	private APIInputHandler inputHandler;
	private APITokenManager tokenManager;

	public APISessionManager(Logger logger) {
		this.sessions = new HashSet<>();
		this.chatTakers = new HashMap<>();
		this.snitchTakers = new HashMap<>();
		this.skynetTakers = new LinkedList<>();
		this.inputHandler = new APIInputHandler(logger);
		this.tokenManager = new APITokenManager();
	}

	public APIInputHandler getInputHandler() {
		return inputHandler;
	}
	
	public APITokenManager getTokenManager() {
		return tokenManager;
	}

	public void registerSession(APISession session) {
		sessions.add(session);
		for (String chat : session.getChatGroups()) {
			List<APISession> existing = chatTakers.get(chat);
			if (existing == null) {
				existing = new LinkedList<>();
				chatTakers.put(chat, existing);
			}
			synchronized (existing) {
				existing.add(session);
			}
		}
		for (String snitch : session.getSnitchGroups()) {
			List<APISession> existing = snitchTakers.get(snitch);
			if (existing == null) {
				existing = new LinkedList<>();
				snitchTakers.put(snitch, existing);
			}
			synchronized (existing) {
				existing.add(session);
			}
		}
		if (session.receivesSkynet()) {
			synchronized (skynetTakers) {
				skynetTakers.add(session);
			}
		}
	}

	public void handleSnitchHit(PlayerHitSnitchAction action) {
		List<APISession> applyingSessions = snitchTakers.get(action.getGroupName());
		iterateAndCleanUp(applyingSessions, (session, a) -> {
			session.sendSnitchAlert(action);
		}, action);
	}

	public void handleGroupMessage(GroupChatMessageAction action) {
		List<APISession> applyingSessions = snitchTakers.get(action.getGroupName());
		iterateAndCleanUp(applyingSessions, (session, a) -> {
			session.sendGroupChatMessage(action);
		}, action);
	}

	public void handleSkynetMessage(SkynetAction action) {
		iterateAndCleanUp(skynetTakers, (session, a) -> {
			session.sendSkynetAlert(action);
		}, action);
	}

	private void iterateAndCleanUp(List<APISession> sessions, BiConsumer<APISession, MinecraftAction> function,
			MinecraftAction action) {
		if (sessions == null) {
			return;
		}
		synchronized (sessions) {
			Iterator<APISession> iter = sessions.iterator();
			while (iter.hasNext()) {
				APISession session = iter.next();
				if (session.isClosed()) {
					iter.remove();
					continue;
				}
				function.accept(session, action);
			}
		}
	}

}

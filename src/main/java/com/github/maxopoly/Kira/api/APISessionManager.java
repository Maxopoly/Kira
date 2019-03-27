package com.github.maxopoly.Kira.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.github.maxopoly.Kira.rabbit.input.SkynetMessage;
import com.github.maxopoly.Kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.Kira.relay.actions.MinecraftAction;
import com.github.maxopoly.Kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;

public class APISessionManager {

	private Set<APISession> sessions;
	private Map<String, List<APISession>> chatTakers;
	private Map<String, List<APISession>> snitchTakers;
	private List<APISession> skynetTakers;

	public APISessionManager() {
		this.sessions = new HashSet<>();
		this.chatTakers = new HashMap<>();
		this.snitchTakers = new HashMap<>();
		this.skynetTakers = new LinkedList<>();
	}
	
	//TODO TODO TODO consider thread safety

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

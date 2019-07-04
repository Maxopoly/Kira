package com.github.maxopoly.kira.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.maxopoly.kira.relay.actions.NewPlayerAction;
import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.input.APIInputHandler;
import com.github.maxopoly.kira.api.token.APITokenManager;
import com.github.maxopoly.kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.kira.relay.actions.SkynetAction;

public class APISessionManager {

	private Logger logger;
	private Set<APISession> sessions;
	private Map<String, List<APISession>> chatTakers;
	private Map<String, List<APISession>> snitchTakers;
	private List<APISession> skynetTakers;
	private APIInputHandler inputHandler;
	private APITokenManager tokenManager;
	private KiraWebSocketServer socketServer;

	public APISessionManager(Logger logger, long sendingInterval) {
		this.logger = logger;
		this.sessions = new HashSet<>();
		this.chatTakers = new HashMap<>();
		this.snitchTakers = new HashMap<>();
		this.skynetTakers = new LinkedList<>();
		this.inputHandler = new APIInputHandler(logger);
		this.tokenManager = new APITokenManager();
		this.socketServer = new KiraWebSocketServer(logger, KiraMain.getInstance().getConfig());
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(() -> {
			sendUpdates();
		}, sendingInterval, sendingInterval, TimeUnit.MILLISECONDS);
	}

	public void closeSocket() {
		try {
			socketServer.stop();
		} catch (IOException | InterruptedException e) {
			logger.warn("Failed to close web socket", e);
		}
	}

	public APIInputHandler getInputHandler() {
		return inputHandler;
	}

	public APITokenManager getTokenManager() {
		return tokenManager;
	}

	public void handleGroupMessage(GroupChatMessageAction action) {
		List<APISession> applyingSessions = chatTakers.get(action.getGroupName());
		iterateAndCleanUp(applyingSessions, (session) -> {
			session.sendGroupChatMessage(action);
		});
	}

	public void handleSkynetMessage(SkynetAction action) {
		iterateAndCleanUp(skynetTakers, (session) -> {
			session.sendSkynetAlert(action);
		});
	}

	public void handleNewPlayerMessage(NewPlayerAction action) {
		// reuse skynetTakers because this is from the same event source
		// and thus visible to the same audience
		iterateAndCleanUp(skynetTakers, (session) -> {
			session.sendNewPlayerAlert(action);
		});
	}

	public void handleSnitchHit(PlayerHitSnitchAction action) {
		List<APISession> applyingSessions = snitchTakers.get(action.getGroupName());
		iterateAndCleanUp(applyingSessions, (session) -> {
			session.sendSnitchAlert(action);
		});
	}

	private void iterateAndCleanUp(List<APISession> sessionList, Consumer<APISession> function) {
		if (sessionList == null) {
			return;
		}
		synchronized (sessionList) {
			Iterator<APISession> iter = sessionList.iterator();
			while (iter.hasNext()) {
				APISession session = iter.next();
				if (session.isClosed()) {
					iter.remove();
					continue;
				}
				function.accept(session);
			}
		}
	}

	public void registerSession(APISession session) {
		for (String chat : session.getChatGroups()) {
			List<APISession> existing = chatTakers.computeIfAbsent(chat, s -> new LinkedList<>());
			synchronized (existing) {
				existing.add(session);
			}
		}
		for (String snitch : session.getSnitchGroups()) {
			List<APISession> existing = snitchTakers.computeIfAbsent(snitch, s -> new LinkedList<>());
			synchronized (existing) {
				existing.add(session);
			}
		}
		if (session.receivesSkynet()) {
			synchronized (skynetTakers) {
				skynetTakers.add(session);
			}
		}
		synchronized (sessions) {
			sessions.add(session);
		}
	}

	private void sendUpdates() {
		Iterator<APISession> iter = sessions.iterator();
		while (iter.hasNext()) {
			APISession session = iter.next();
			if (session.isClosed()) {
				iter.remove();
				continue;
			}
			if (session.hasPendingNotifications()) {
				session.popAndSendPendingNotifications();
			}
		}
	}

}

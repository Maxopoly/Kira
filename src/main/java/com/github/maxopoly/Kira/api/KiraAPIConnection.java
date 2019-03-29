package com.github.maxopoly.Kira.api;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.api.input.APISupplier;

@ServerEndpoint(value = "/kira/api")
public class KiraAPIConnection {

	private Session session;
	private APISession apiSession;
	private boolean closed;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		KiraMain.getInstance().getAPISessionManager().getInputHandler().handle(message, new APISupplier(null, this));
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		this.session = null;
		if (!closed) {
			close();
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		KiraMain.getInstance().getLogger().warn("Error occured in API handling", throwable);
		close();
	}
	
	public void sendMessage(String text) {
		session.getAsyncRemote().sendText(text);
	}
	
	public void close() {
		this.closed = true;
		try {
			session.close();
		} catch (IOException e) {
			KiraMain.getInstance().getLogger().warn("Failed to close API session", e);
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public APISession getAPISession() {
		return apiSession;
	}
}

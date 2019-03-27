package com.github.maxopoly.Kira.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.github.maxopoly.Kira.KiraMain;

@ServerEndpoint(value = "/kira/api")
public class KiraWebSocket {

	private Session session;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		this.session = null;
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}
	
	public void close() {
		
	}
}

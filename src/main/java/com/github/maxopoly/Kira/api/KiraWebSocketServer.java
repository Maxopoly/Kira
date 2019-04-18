package com.github.maxopoly.Kira.api;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.api.input.APISupplier;
import com.github.maxopoly.Kira.api.token.APIToken;
import com.github.maxopoly.Kira.api.token.APITokenManager;

public class KiraWebSocketServer extends WebSocketServer {
	
	private static final int API_VERSION = 1;
	
	private Map <WebSocket, APISession> connections;
	private Logger logger;

	
	public KiraWebSocketServer(Logger logger) {
		super(new InetSocketAddress("mc.civclassic.com",14314));
		this.logger = logger;
		connections = new HashMap<>();
		logger.info("Starting Web socket API server");
		start();
	}
	
	private APISession getAPISession(WebSocket socket) {
		return connections.get(socket);
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		KiraMain.getInstance().getLogger().warn("Closing connection with " + conn.getRemoteSocketAddress() + ", because of: " + reason);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		KiraMain.getInstance().getLogger().warn("Error occured in API handling of " + conn, ex);	
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		KiraMain.getInstance().getAPISessionManager().getInputHandler().handle(message, new APISupplier(getAPISession(conn)));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		APISession session = setupSession(conn, handshake);
		if (session != null) {
			KiraMain.getInstance().getAPISessionManager().registerSession(session);
			connections.put(conn, session);
		}
	}

	@Override
	public void onStart() {
		//already did everything in constructor, but still have to override this
	}
	
	private APISession setupSession(WebSocket conn, ClientHandshake handshake) {
		String tokenString = handshake.getFieldValue("apiToken");
		if (tokenString.equals("")) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because no api token was given");
			conn.close(CloseFrame.POLICY_VALIDATION, "No token supplied");
			return null;
		}
		String appId = handshake.getFieldValue("applicationId");
		if (appId.equals("")) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because no application id was given");
			conn.close(CloseFrame.POLICY_VALIDATION, "No app id supplied");
			return null;
		}
		String versionString = handshake.getFieldValue("apiVersion");
		if (versionString.equals("")) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because no api version was given");
			conn.close(CloseFrame.POLICY_VALIDATION, "No api version supplied");
			return null;
		}
		int version;
		try {
			version = Integer.parseInt(versionString);
		}
		catch (NumberFormatException e) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because illegal version " + versionString + " was supplied");
			conn.close(CloseFrame.POLICY_VALIDATION, "Illegal version, not a number");
			return null;
		}
		if (version != API_VERSION) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because they are using an outdated api version");
			conn.close(CloseFrame.POLICY_VALIDATION, "Outdated version, newest one is " + API_VERSION);
			return null;
		}
		APITokenManager tokenMan = KiraMain.getInstance().getAPISessionManager().getTokenManager();
		APIToken token = tokenMan.getToken(tokenString);
		if (token == null) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because they supplied an invalid token");
			conn.close(CloseFrame.POLICY_VALIDATION, "Invalid token");
			return null;
		}
		if (token.isOutdated()) {
			logger.info("Closing connection with " + conn.getRemoteSocketAddress() + ", because their token had timed out");
			conn.close(CloseFrame.POLICY_VALIDATION, "Outdated token");
		}
		return token.generateSession(conn);
	}
}

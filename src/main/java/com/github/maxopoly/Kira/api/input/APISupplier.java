package com.github.maxopoly.Kira.api.input;

import com.github.maxopoly.Kira.api.KiraWebSocket;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.user.KiraUser;

public class APISupplier implements InputSupplier {
	
	private KiraUser user;
	private KiraWebSocket connection;
	
	public APISupplier(KiraUser user, KiraWebSocket connection) {
		this.user = user;
		this.connection = connection;
	}

	@Override
	public String getIdentifier() {
		if (user == null) {
			return "Unidentified API connection";
		}
		return user.toString();
	}

	@Override
	public KiraUser getUser() {
		return user;
	}

	@Override
	public void reportBack(String msg) {
		throw new IllegalAccessError();
	}

	@Override
	public boolean hasPermission(String perm) {
		return false;
	}

	@Override
	public long getChannelID() {
		return -1;
	}
	
	public KiraWebSocket getConnection() {
		return connection;
	}

}

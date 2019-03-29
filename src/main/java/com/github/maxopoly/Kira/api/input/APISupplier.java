package com.github.maxopoly.Kira.api.input;

import com.github.maxopoly.Kira.api.KiraAPIConnection;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.user.KiraUser;

public class APISupplier implements InputSupplier {
	
	private KiraUser user;
	private KiraAPIConnection connection;
	
	public APISupplier(KiraUser user, KiraAPIConnection connection) {
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
	
	public KiraAPIConnection getConnection() {
		return connection;
	}

}

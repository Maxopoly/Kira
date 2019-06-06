package com.github.maxopoly.kira.api.input;

import com.github.maxopoly.kira.api.APISession;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.KiraUser;

public class APISupplier implements InputSupplier {
	
	private APISession session;
	
	public APISupplier(APISession session) {
		this.session = session;
	}

	@Override
	public long getChannelID() {
		return -1;
	}

	@Override
	public String getIdentifier() {
		if (session == null) {
			return "Unidentified API connection";
		}
		return session.getUser().toString();
	}

	public APISession getSession() {
		return session;
	}

	@Override
	public KiraUser getUser() {
		return session.getUser();
	}

	@Override
	public boolean hasPermission(String perm) {
		return false;
	}
	
	@Override
	public void reportBack(String msg) {
		throw new IllegalAccessError();
	}

}

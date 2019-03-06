package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.KiraUser;

public abstract class DiscordCommandSupplier implements InputSupplier {

	protected KiraUser user;

	public DiscordCommandSupplier(KiraUser user) {
		if (user == null) {
			throw new IllegalArgumentException("User can't be null");
		}
		this.user = user;
	}

	public KiraUser getUser() {
		return user;
	}

	@Override
	public String getIdentifier() {
		return user.toString();
	}

	@Override
	public boolean hasPermission(String perm) {
		return KiraMain.getInstance().getKiraRoleManager().hasPermission(user, perm);
	}

}

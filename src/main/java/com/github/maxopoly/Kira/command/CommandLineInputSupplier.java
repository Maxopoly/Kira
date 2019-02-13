package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.KiraUser;

public class CommandLineInputSupplier implements InputSupplier {

	@Override
	public String getIdentifier() {
		return "CONSOLE";
	}

	@Override
	public KiraUser getUser() {
		return null;
	}

	@Override
	public void reportBack(String msg) {
		KiraMain.getInstance().getLogger().info(msg);
	}

	@Override
	public boolean hasPermission(String perm) {
		return true;
	}

	@Override
	public long getChannelID() {
		return -1L;
	}

}

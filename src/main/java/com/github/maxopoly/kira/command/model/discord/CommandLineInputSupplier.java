package com.github.maxopoly.kira.command.model.discord;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.KiraUser;

public class CommandLineInputSupplier implements InputSupplier {

	@Override
	public long getChannelID() {
		return -1L;
	}

	@Override
	public String getIdentifier() {
		return "CONSOLE";
	}

	@Override
	public KiraUser getUser() {
		return null;
	}

	@Override
	public boolean hasPermission(String perm) {
		return true;
	}

	@Override
	public void reportBack(String msg) {
		KiraMain.getInstance().getLogger().info(msg);
	}

}

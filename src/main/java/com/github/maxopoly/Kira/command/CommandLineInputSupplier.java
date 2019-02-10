package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.User;

public class CommandLineInputSupplier implements InputSupplier {

	@Override
	public String getIdentifier() {
		return "CONSOLE";
	}

	@Override
	public User getUser() {
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

}

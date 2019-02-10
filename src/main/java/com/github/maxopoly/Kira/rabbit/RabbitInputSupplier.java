package com.github.maxopoly.Kira.rabbit;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.user.User;

public class RabbitInputSupplier implements InputSupplier {

	@Override
	public String getIdentifier() {
		return "RABBIT";
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

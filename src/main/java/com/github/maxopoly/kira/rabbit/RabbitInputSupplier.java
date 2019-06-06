package com.github.maxopoly.kira.rabbit;

import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.KiraMain;

public class RabbitInputSupplier implements InputSupplier {

	@Override
	public long getChannelID() {
		return -1L;
	}

	@Override
	public String getIdentifier() {
		return "RABBIT";
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

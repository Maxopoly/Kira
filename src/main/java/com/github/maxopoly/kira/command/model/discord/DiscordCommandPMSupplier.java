package com.github.maxopoly.kira.command.model.discord;

import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.util.DiscordMessageDivider;

public class DiscordCommandPMSupplier extends  DiscordCommandSupplier {

	public DiscordCommandPMSupplier(KiraUser user) {
		super(user);
	}

	@Override
	public long getChannelID() {
		return -1L;
	}

	@Override
	public void reportBack(String msg) {
		DiscordMessageDivider.sendPrivateMessage(user, msg);
	}
}

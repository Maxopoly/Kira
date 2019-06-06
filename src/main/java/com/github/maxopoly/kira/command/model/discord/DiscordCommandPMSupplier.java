package com.github.maxopoly.kira.command.model.discord;

import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.KiraMain;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.PrivateChannel;

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
		JDA jda = KiraMain.getInstance().getJDA();
		net.dv8tion.jda.core.entities.User discordUser = jda.getUserById(user.getDiscordID());
		PrivateChannel pm = discordUser.openPrivateChannel().complete();
		pm.sendMessage(msg).queue();
	}
}

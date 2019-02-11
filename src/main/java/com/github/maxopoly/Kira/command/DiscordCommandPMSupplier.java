package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.User;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.PrivateChannel;

public class DiscordCommandPMSupplier extends  DiscordCommandSupplier {

	public DiscordCommandPMSupplier(User user) {
		super(user);
	}

	@Override
	public void reportBack(String msg) {
		JDA jda = KiraMain.getInstance().getJDA();
		net.dv8tion.jda.core.entities.User discordUser = jda.getUserById(user.getDiscordID());
		PrivateChannel pm = discordUser.openPrivateChannel().complete();
		pm.sendMessage(msg).queue();
	}

}

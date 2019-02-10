package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.User;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.PrivateChannel;

public class DiscordCommandSupplier implements InputSupplier {

	private User user;

	public DiscordCommandSupplier(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User can't be null");
		}
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String getIdentifier() {
		return user.toString();
	}

	@Override
	public void reportBack(String msg) {
		JDA jda = KiraMain.getInstance().getJDA();
		net.dv8tion.jda.core.entities.User discordUser = jda.getUserById(user.getDiscordID());
		PrivateChannel pm = discordUser.openPrivateChannel().complete();
		pm.sendMessage(msg).queue();
	}

	@Override
	public boolean hasPermission(String perm) {
		return KiraMain.getInstance().getKiraRoleManager().hasPermission(user, perm);
	}

}

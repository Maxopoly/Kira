package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.user.User;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public class DiscordCommandChannelSupplier extends DiscordCommandSupplier {

	private long channelID;
	private long guildID;

	public DiscordCommandChannelSupplier(User user, long guildID, long channelID) {
		super(user);
		this.channelID = channelID;
		this.guildID = guildID;
	}

	@Override
	public void reportBack(String msg) {
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildID);
		if (guild == null) {
			return;
		}
		TextChannel channel = guild.getTextChannelById(channelID);
		if (channel == null) {
			return;
		}
		Member member = guild.getMemberById(user.getDiscordID());
		String tag = "";
		if (member != null) {
			tag = member.getAsMention() + " ";
		}
		channel.sendMessage(tag + msg).queue();
	}

}

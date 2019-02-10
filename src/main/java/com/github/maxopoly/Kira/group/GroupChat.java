package com.github.maxopoly.Kira.group;

import com.github.maxopoly.Kira.KiraMain;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GroupChat {
	
	private long channelId;
	private String name;
	private long guildId;
	
	
	public GroupChat(String name, long channelId, long guildId) {
		this.name = name;
		this.channelId = channelId;
		this.guildId = guildId;
	}
	
	public long getChannelId() {
		return channelId;
	}
	
	public String getName() {
		return name;
	}
	
	public long getGuildId() {
		return guildId;
	}
	
	public boolean sendMessage(String msg) {
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		channel.sendMessage(msg);
		return true;
	}

}

package com.github.maxopoly.Kira.group;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.permission.KiraPermission;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GroupChat {
	
	private final int id;
	private final long channelId;
	private final String name;
	private final long guildId;
	private final KiraPermission perm;
	
	
	public GroupChat(int id, String name, long channelId, long guildId, KiraPermission perm) {
		this.id = id;
		this.name = name;
		this.channelId = channelId;
		this.guildId = guildId;
		this.perm = perm;
	}
	
	public long getDiscordChannelId() {
		return channelId;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public long getGuildId() {
		return guildId;
	}
	
	public KiraPermission getRequiredPermission() {
		return perm;
	}
	
	public boolean sendMessage(String author, String msg) {
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

package com.github.maxopoly.Kira.group;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.permission.KiraRole;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GroupChat {
	
	private final int id;
	private final long channelId;
	private final String name;
	private final long guildId;
	private final KiraRole role;
	
	
	public GroupChat(int id, String name, long channelId, long guildId, KiraRole role) {
		this.id = id;
		this.name = name;
		this.channelId = channelId;
		this.guildId = guildId;
		this.role = role;
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
	
	public KiraRole getTiedRole() {
		return role;
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
		channel.sendMessage("`["+ name + "]` [**"+ author + "**]  " + msg).queue();
		return true;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", channelID: ");
		result.append(channelId);
		result.append(", guildID: ");
		result.append(guildId);
		result.append(", roleID: ");
		result.append(role.getID());
		result.append("}");
		return result.toString();
	}

}

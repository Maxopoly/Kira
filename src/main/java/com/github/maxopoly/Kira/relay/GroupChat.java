package com.github.maxopoly.Kira.relay;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.user.KiraUser;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GroupChat {

	private static final float internalWeight = 1.0f;
	private static final float externalWeight = 0.25f;

	private final int id;
	private final long channelId;
	private final String name;
	private final long guildId;
	private final KiraRole role;
	private final KiraUser creator;
	private RelayConfig config;

	public GroupChat(int id, String name, long channelId, long guildId, KiraRole role, KiraUser creator, RelayConfig config) {
		this.id = id;
		this.config = config;
		this.name = name;
		this.channelId = channelId;
		this.guildId = guildId;
		this.role = role;
		this.creator = creator;
		this.config = config;
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

	public KiraUser getCreator() {
		return creator;
	}

	public RelayConfig getConfig() {
		return config;
	}

	public void setConfig(RelayConfig config) {
		this.config = config;
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
		String discordMessage = config.formatChatMessage(msg, author, name);
		channel.sendMessage(discordMessage).queue();
		return true;
	}

	public boolean sendSnitchHit(String playerName, String snitchname, int x, int y, int z, SnitchHitType type) {
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		String msg = config.formatSnitchOutput(x, y, z, snitchname, playerName, type, name);
		channel.sendMessage(msg).queue();
		return true;
	}

	public float getWeight() {
		if (guildId == KiraMain.getInstance().getGuild().getIdLong()) {
			return internalWeight;
		}
		return externalWeight;
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

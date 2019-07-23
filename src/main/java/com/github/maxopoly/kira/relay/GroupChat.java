package com.github.maxopoly.kira.relay;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.permission.KiraRole;
import com.github.maxopoly.kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.kira.relay.actions.NewPlayerAction;
import com.github.maxopoly.kira.relay.actions.PlayerHitSnitchAction;
import com.github.maxopoly.kira.relay.actions.SkynetAction;
import com.github.maxopoly.kira.user.KiraUser;

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

	public RelayConfig getConfig() {
		return config;
	}

	public KiraUser getCreator() {
		return creator;
	}

	public long getDiscordChannelId() {
		return channelId;
	}

	public long getGuildId() {
		return guildId;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public KiraRole getTiedRole() {
		return role;
	}

	public float getWeight() {
		if (guildId == KiraMain.getInstance().getGuild().getIdLong()) {
			return internalWeight;
		}
		return externalWeight;
	}

	public boolean sendMessage(GroupChatMessageAction action) {
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		String discordMessage = config.formatChatMessage(action);
		channel.sendMessage(discordMessage).queue();
		return true;
	}

	public boolean sendNewPlayer(NewPlayerAction action) {
		if (!config.isNewPlayerEnabled()) {
			return true;
		}
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		String msg = config.formatNewPlayerMessage(action);
		channel.sendMessage(msg).queue();
		return true;
	}

	public boolean sendSkynet(SkynetAction action) {
		if (!config.isSkynetEnabled()) {
			return true;
		}
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		String msg = config.formatSkynetMessage(action);
		channel.sendMessage(msg).queue();
		return true;
	}

	public boolean sendSnitchHit(PlayerHitSnitchAction action) {
		JDA jda = KiraMain.getInstance().getJDA();
		Guild guild = jda.getGuildById(guildId);
		if (guild == null) {
			return false;
		}
		TextChannel channel = guild.getTextChannelById(channelId);
		if (channel == null) {
			return false;
		}
		String msg = config.formatSnitchOutput(action);
		channel.sendMessage(msg).queue();
		return true;
	}

	public void setConfig(RelayConfig config) {
		this.config = config;
	}

	@Override
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

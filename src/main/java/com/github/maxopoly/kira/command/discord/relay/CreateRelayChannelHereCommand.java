package com.github.maxopoly.kira.command.discord.relay;

import java.util.List;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.KiraUser;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;


public class CreateRelayChannelHereCommand extends ArgumentBasedCommand {

	public CreateRelayChannelHereCommand() {
		super("createrelayhere", 1, "createrelay", "makerelay", "setupsnitchchannel", "setuprelayhere");
		doesRequireIngameAccount();
	}

	@Override
	public String getFunctionality() {
		return "Attempts to create a relay in the channel this message was sent in for the group it was sent by";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "createrelayhere [group]";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		long channelID = sender.getChannelID();
		if (channelID <= -1) {
			return "You can't do this from here";
		}
		Channel channel = KiraMain.getInstance().getJDA().getTextChannelById(channelID);
		if (channel == null) {
			return "Something went wrong, tell an admin";
		}
		if (channel.getGuild().getIdLong() == KiraMain.getInstance().getGuild().getIdLong()) {
			return "You can't create relays here";
		}
		long discordUserID = sender.getUser().getDiscordID();
		List<Permission> perms = channel.getGuild().getMemberById(discordUserID).getPermissions(channel);
		if (!perms.contains(Permission.MANAGE_CHANNEL)) {
			return "You need the 'MANAGE_CHANNEL' permission to add a relay to this channel";
		}
		KiraMain.getInstance().getMCRabbitGateway().requestRelayCreation(user, args [0], channel);
		return "Checking permissions for channel handling...";
	}
}


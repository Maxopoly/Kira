package com.github.maxopoly.Kira.command.discord;

import java.text.DecimalFormat;
import java.util.Set;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.model.discord.Command;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.user.KiraUser;

public class GetWeightCommand extends Command {

	private DecimalFormat format = new DecimalFormat("##.##");

	public GetWeightCommand() {
		super("getchannels", "getweight");
		setRequireUser();
	}

	@Override
	public String getFunctionality() {
		return "Shows all relays owned by you";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "getchannels";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		StringBuilder reply = new StringBuilder();
		KiraUser user = sender.getUser();
		Set<String> ownedChats = KiraMain.getInstance().getDAO().getGroupChatChannelIdByCreator(user);
		float totalWeight = 0.0f;
		int totalCount = 0;
		for (String name : ownedChats) {
			GroupChat chat = KiraMain.getInstance().getGroupChatManager().getGroupChat(name);
			if (chat == null) {
				continue;
			}
			totalCount++;
			totalWeight += chat.getWeight();
			reply.append("'" + chat.getName() + "' is in channel " + chat.getDiscordChannelId() + " in guild "
					+ chat.getGuildId() + " with weight " + format.format(chat.getWeight()) + "\n");
		}
		reply.append("Total of " + totalCount + " relay(s) owned with a total weight of " + format.format(totalWeight));
		return reply.toString();
	}

}

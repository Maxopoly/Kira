package com.github.maxopoly.Kira.command.commands;

import java.util.Set;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.relay.GroupChat;

public class ChannelInfoCommand extends Command {

	public ChannelInfoCommand() {
		super("channelinfo", 0, 0, "relayinfo");
		setRequireUser();
	}

	@Override
	public String execute(InputSupplier supplier, String[] args) {
		StringBuilder reply = new StringBuilder();
		long channelID = supplier.getChannelID();
		if (channelID <= -1) {
			return "You can't do this from here";
		}
		Set <GroupChat> chats = KiraMain.getInstance().getGroupChatManager().getChatByChannelID(channelID);
		reply.append("A total of " + chats.size() + " relays are setup for this channel\n");
		reply.append("Channel id: " + channelID + "\n---\n");
		for(GroupChat chat : chats) {
			reply.append("**" + chat.getName() + "**");
			reply.append(String.format("  Owner: %s\n", chat.getCreator().getName()));
			reply.append(String.format("  Config: %s\n\n", chat.getConfig().getName()));
		}
		return reply.toString();
	}

	@Override
	public String getUsage() {
		return "getchannels";
	}

	@Override
	public String getFunctionality() {
		return "Shows all relays owned by you";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}

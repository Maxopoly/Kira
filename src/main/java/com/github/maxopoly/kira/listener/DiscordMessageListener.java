package com.github.maxopoly.kira.listener;

import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.CommandHandler;
import com.github.maxopoly.kira.command.model.discord.DiscordCommandChannelSupplier;
import com.github.maxopoly.kira.command.model.discord.DiscordCommandPMSupplier;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.relay.GroupChat;
import com.github.maxopoly.kira.relay.GroupChatManager;
import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.user.UserManager;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter {

	private String keyWord;
	private CommandHandler cmdHandler;
	private Logger logger;
	private UserManager userManager;
	private long ownID;

	public DiscordMessageListener(CommandHandler cmdHandler, Logger logger, UserManager userManager, long ownID) {
		this.cmdHandler = cmdHandler;
		this.logger = logger;
		this.ownID = ownID;
		this.userManager = userManager;
		this.keyWord = KiraMain.getInstance().getConfig().getCommandPrefix();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getAuthor().getIdLong() == ownID) {
			return;
		}
		KiraUser user = userManager.getOrCreateUserByDiscordID(event.getAuthor().getIdLong());
		String content = event.getMessage().getContentRaw();
		if (event.isFromType(ChannelType.PRIVATE)) {
			logger.info(String.format("CHAT [PM] %s: %s", event.getAuthor().getName(),
					event.getMessage().getContentDisplay()));
			cmdHandler.handle(content, new DiscordCommandPMSupplier(user));
		} else {
			logger.info(
					String.format("CHAT [%s][%s] %s: %s", event.getGuild().getName(), event.getTextChannel().getName(),
							event.getMember().getEffectiveName(), event.getMessage().getContentDisplay()));
			if (content.startsWith(keyWord)) {
				InputSupplier supplier = new DiscordCommandChannelSupplier(user, event.getGuild().getIdLong(),
						event.getChannel().getIdLong());
				cmdHandler.handle(content.substring(keyWord.length()), supplier);
				return;
			}
			GroupChatManager chatMan = KiraMain.getInstance().getGroupChatManager();
			Set<GroupChat> chats = chatMan.getChatByChannelID(event.getChannel().getIdLong());
			if (!chats.isEmpty() && user.hasIngameAccount()) {
				String message = event.getMessage().getContentDisplay();
				message = sanitize(message);
				boolean delete = false;
				if (!message.equals("")) {
					for (GroupChat chat : chats) {
						if (chat.getConfig().shouldRelayFromDiscord()) {
							KiraMain.getInstance().getMCRabbitGateway().sendGroupChatMessage(user, chat, message);
						}
						if (chat.getConfig().shouldDeleteDiscordMessage()) {
							delete = true;
						}
					}
				}
				if (delete) {
					event.getMessage().delete().queue();
				}
			}
		}
	}

	private String sanitize(String input) {
		String result = input.replace("\n", "");
		result = result.replace("\r", "");
		result = result.replace("\t", "");
		result = result.replace("§", "");
		result = result.trim();
		if (result.length() > 255) {
			result = result.substring(0, 255);
		}
		return result;
	}

}

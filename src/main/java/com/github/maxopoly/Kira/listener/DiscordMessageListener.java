package com.github.maxopoly.Kira.listener;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.command.CommandHandler;
import com.github.maxopoly.Kira.command.DiscordCommandSupplier;
import com.github.maxopoly.Kira.user.User;
import com.github.maxopoly.Kira.user.UserManager;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter {

	private CommandHandler cmdHandler;
	private Logger logger;
	private UserManager userManager;
	private long ownID;

	public DiscordMessageListener(CommandHandler cmdHandler, Logger logger, UserManager userManager, long ownID) {
		this.cmdHandler = cmdHandler;
		this.logger = logger;
		this.ownID = ownID;
		this.userManager = userManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getAuthor().getIdLong() == ownID) {
			return;
		}
		if (event.isFromType(ChannelType.PRIVATE)) {
			logger.info(String.format("CHAT [PM] %s: %s", event.getAuthor().getName(),
					event.getMessage().getContentDisplay()));
			String content = event.getMessage().getContentRaw();
			User user = userManager.getOrCreateUserByDiscordID(event.getAuthor().getIdLong());
			cmdHandler.handle(content, new DiscordCommandSupplier(user));
		} else {
			logger.info(
					String.format("CHAT [%s][%s] %s: %s", event.getGuild().getName(), event.getTextChannel().getName(),
							event.getMember().getEffectiveName(), event.getMessage().getContentDisplay()));
		}
	}

}

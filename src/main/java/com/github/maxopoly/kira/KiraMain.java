package com.github.maxopoly.kira;

import java.io.Console;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.api.APISessionManager;
import com.github.maxopoly.kira.command.model.discord.CommandHandler;
import com.github.maxopoly.kira.command.model.discord.CommandLineInputSupplier;
import com.github.maxopoly.kira.database.DAO;
import com.github.maxopoly.kira.database.DBConnection;
import com.github.maxopoly.kira.listener.DiscordMessageListener;
import com.github.maxopoly.kira.permission.KiraRoleManager;
import com.github.maxopoly.kira.rabbit.MinecraftRabbitGateway;
import com.github.maxopoly.kira.rabbit.RabbitHandler;
import com.github.maxopoly.kira.rabbit.session.RequestSessionManager;
import com.github.maxopoly.kira.relay.GroupChatManager;
import com.github.maxopoly.kira.relay.RelayConfigManager;
import com.github.maxopoly.kira.user.AuthManager;
import com.github.maxopoly.kira.user.DiscordRoleManager;
import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.user.UserManager;
import com.rabbitmq.client.ConnectionFactory;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

public class KiraMain {

	private static KiraMain instance;

	public static KiraMain getInstance() {
		return instance;
	}
	public static void main(String[] args) {
		instance = new KiraMain();
		if (!instance.loadConfig()) {
			return;
		}
		instance.authManager = new AuthManager();
		instance.userManager = new UserManager(instance.logger);
		if (!instance.loadDatabase()) {
			return;
		}
		if (!instance.loadPermission()) {
			return;
		}
		if (!instance.startJDA()) {
			return;
		}
		if (!instance.setupAuthManager()) {
			return;
		}
		if (!instance.startRabbit()) {
			return;
		}
		instance.commandHandler = new CommandHandler(instance.logger);
		if (!instance.loadGroupChats()) {
			return;
		}
		if (!instance.setupListeners()) {
			return;
		}
		instance.apiSessionManager = new APISessionManager(instance.logger, 500);
		instance.rabbit.beginAsyncListen();
		instance.parseInput();
	}
	private Logger logger = LogManager.getLogger("Main");
	private JDA jda;
	private boolean shutdown = false;
	private CommandHandler commandHandler;
	private Guild guild;
	private UserManager userManager;
	private ConfigManager configManager;
	private DiscordRoleManager roleManager;
	private DAO dao;
	private RabbitHandler rabbit;
	private MinecraftRabbitGateway mcRabbit;
	private AuthManager authManager;
	private KiraRoleManager kiraRoleManager;
	private GroupChatManager groupChatManager;
	private RelayConfigManager relayConfigManager;
	private RequestSessionManager requestSessionManager;
	private APISessionManager apiSessionManager;

	public APISessionManager getAPISessionManager() {
		return apiSessionManager;
	}

	public AuthManager getAuthManager() {
		return authManager;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public ConfigManager getConfig() {
		return configManager;
	}

	public DAO getDAO() {
		return dao;
	}

	public GroupChatManager getGroupChatManager() {
		return groupChatManager;
	}

	public Guild getGuild() {
		return guild;
	}

	public JDA getJDA() {
		return jda;
	}

	public KiraRoleManager getKiraRoleManager() {
		return kiraRoleManager;
	}

	public Logger getLogger() {
		return logger;
	}

	public MinecraftRabbitGateway getMCRabbitGateway() {
		return mcRabbit;
	}

	public RabbitHandler getRabbitHandler() {
		return rabbit;
	}

	public RelayConfigManager getRelayConfigManager() {
		return relayConfigManager;
	}

	public RequestSessionManager getRequestSessionManager() {
		return requestSessionManager;
	}

	public DiscordRoleManager getRoleManager() {
		return roleManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}

	private boolean loadConfig() {
		configManager = new ConfigManager(logger);
		return configManager.reload();
	}

	private boolean loadDatabase() {
		DBConnection dbConn = configManager.getDatabase();
		if (dbConn == null) {
			return false;
		}
		dao = new DAO(dbConn, logger);
		for (KiraUser user : dao.loadUsers()) {
			userManager.addUser(user);
		}
		return true;
	}

	private boolean loadGroupChats() {
		if (configManager.getRelaySectionID() == -1) {
			return false;
		}
		relayConfigManager = new RelayConfigManager(dao);
		groupChatManager = new GroupChatManager(dao, logger, configManager.getRelaySectionID(), relayConfigManager);
		return true;
	}

	private boolean loadPermission() {
		kiraRoleManager = dao.loadAllRoles();
		return kiraRoleManager != null;
	}

	private void parseInput() {
		Console c = System.console();
		Scanner scanner = null;
		if (c == null) {
			logger.warn("System console not detected, using scanner as fallback behavior");
			scanner = new Scanner(System.in);
		}
		while (!shutdown) {
			String msg;
			if (c == null) {
				msg = scanner.nextLine();
			} else {
				msg = c.readLine("");
			}
			if (msg == null) {
				continue;
			}
			commandHandler.handle(msg, new CommandLineInputSupplier());
		}
	}
	
	private boolean setupAuthManager() {
		roleManager = new DiscordRoleManager(guild, configManager.getAuthroleID(), logger, userManager);
		roleManager.syncFully();
		return true;
	}

	private boolean setupListeners() {
		jda.addEventListener(
				new DiscordMessageListener(commandHandler, logger, userManager, jda.getSelfUser().getIdLong()));
		return true;
	}

	private boolean startJDA() {
		String token = configManager.getBotToken();
		if (token == null) {
			logger.error("No bot token was supplied");
			return false;
		}
		try {
			jda = new JDABuilder(token).build();
			jda.awaitReady();
		} catch (LoginException | InterruptedException e) {
			logger.error("Failed to start jda", e);
			return false;
		}
		long serverID = configManager.getServerID();
		if (serverID == -1L) {
			logger.error("No server id was provided");
			return false;
		}
		guild = jda.getGuildById(serverID);
		if (guild == null) {
			logger.error("No guild with the provided id " + serverID + " could be found");
			return false;
		}
		long authID = configManager.getAuthroleID();
		if (authID == -1L) {
			logger.error("No auth role id was provided");
			return false;
		}
		Role authRole = guild.getRoleById(authID);
		if (authRole == null) {
			logger.error("No auth role with the provided id " + authID + " could be found");
			return false;
		}
		return true;
	}

	private boolean startRabbit() {
		String incomingQueue = configManager.getIncomingQueueName();
		String outgoingQueue = configManager.getOutgoingQueueName();
		ConnectionFactory connFac = configManager.getRabbitConfig();
		if (incomingQueue == null || outgoingQueue == null || connFac == null) {
			return false;
		}
		rabbit = new RabbitHandler(connFac, incomingQueue, outgoingQueue, logger);
		if (!rabbit.setup()) {
			return false;
		}
		mcRabbit = new MinecraftRabbitGateway(rabbit);
		requestSessionManager = new RequestSessionManager(rabbit, logger);
		return true;
	}


	public void stop() {
		rabbit.shutdown();
		apiSessionManager.shutdown();
		shutdown = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(2000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}).start();
	}
}

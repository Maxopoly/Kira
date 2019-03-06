package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.rabbit.session.PermissionCheckSession;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.relay.GroupChatManager;
import com.github.maxopoly.Kira.relay.RelayConfig;
import com.github.maxopoly.Kira.user.KiraUser;

public class TieRelayConfigCommand extends Command {

	public TieRelayConfigCommand() {
		super("setrelayconfig", 2, 2);
		setRequireIngameAccount();
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		GroupChat chat = KiraMain.getInstance().getGroupChatManager().getGroupChat(args[0]);
		if (chat == null) {
			return "No group chat with the name " + args[0] + " is known";
		}
		RelayConfig config = KiraMain.getInstance().getRelayConfigManager().getByName(args[1]);
		if (config == null) {
			return "No relay config with the name " + args[0] + " is known";
		}
		KiraMain.getInstance().getRequestSessionManager().request(new PermissionCheckSession(user.getIngameUUID(),
				chat.getName(), GroupChatManager.getNameLayerManageChannelPermission()) {

			@Override
			public void handlePermissionReply(boolean hasPerm) {
				KiraMain.getInstance().getGroupChatManager().setConfig(chat, config);
				sender.reportBack("Successfully set relay config for " + chat.getName() + " to " + config.getName());
			}
		});
		return "Requesting permission confirmation from server...";
	}

	@Override
	public String getUsage() {
		return "setrelayconfig [group] [relay]";
	}

	@Override
	public String getFunctionality() {
		return "Sets which configuration to use for a specific relay";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}

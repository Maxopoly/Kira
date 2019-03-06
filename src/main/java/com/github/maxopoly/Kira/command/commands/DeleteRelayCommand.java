package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.rabbit.session.PermissionCheckSession;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.relay.GroupChatManager;
import com.github.maxopoly.Kira.user.KiraUser;

public class DeleteRelayCommand extends Command {

	public DeleteRelayCommand() {
		super("deleterelay", 1, 1);
		setRequireIngameAccount();
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		GroupChat chat = man.getGroupChat(args[0]);
		if (chat == null) {
			return "No group chat with the name " + args[0] + " is known";
		}
		KiraMain.getInstance().getRequestSessionManager().request(new PermissionCheckSession(user.getIngameUUID(),
				chat.getName(), GroupChatManager.getNameLayerManageChannelPermission()) {

			@Override
			public void handlePermissionReply(boolean hasPerm) {
				GroupChat chat = man.getGroupChat(args[0]);
				if (chat == null) {
					logger.warn("Failed to delete group chat"+ args[0] + ", it was already gone");
					sender.reportBack("Channel deletion failed, channel was already gone");
					return;
				}
				logger.info("Attempting to delete group of chat for " + chat.getName() + " as initiated by " + user.toString());
				KiraMain.getInstance().getGroupChatManager().deleteGroupChat(chat);
				sender.reportBack("Successfully removed relay for group " + chat.getName());
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

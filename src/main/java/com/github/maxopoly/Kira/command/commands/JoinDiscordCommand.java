package com.github.maxopoly.Kira.command.commands;

import java.util.UUID;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

public class JoinDiscordCommand extends Command {
	public JoinDiscordCommand() {
		super("invite", 0, 0);
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		if (sender.getUser() == null) {
			return "You are not allowed to do that";
		}
		UUID uuid = sender.getUser().getIngameUUID();
		if (uuid == null) {
			return "You are not allowed to do this, link an ingame account";
		}
		String url = KiraMain.getInstance().getJDA().asBot().getInviteUrl();
		return url;
	}

	@Override
	public String getUsage() {
		return "invite";
	}

	@Override
	public String getFunctionality() {
		return "Allows you to add Kira to other discords";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}
}

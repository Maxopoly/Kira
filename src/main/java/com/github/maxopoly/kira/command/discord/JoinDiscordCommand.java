package com.github.maxopoly.Kira.command.discord;

import java.util.UUID;

import com.github.maxopoly.Kira.command.model.discord.Command;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.KiraMain;

public class JoinDiscordCommand extends Command {
	public JoinDiscordCommand() {
		super("invite");
	}

	@Override
	public String getFunctionality() {
		return "Allows you to add Kira to other discords";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "invite";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		if (sender.getUser() == null) {
			return "You are not allowed to do that";
		}
		UUID uuid = sender.getUser().getIngameUUID();
		if (uuid == null) {
			return "You are not allowed to do this, link an ingame account";
		}
		return KiraMain.getInstance().getJDA().asBot().getInviteUrl();
	}
}

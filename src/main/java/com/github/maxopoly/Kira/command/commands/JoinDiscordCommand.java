package com.github.maxopoly.Kira.command.commands;

import java.util.UUID;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Invite;

public class JoinDiscordCommand extends Command {
	public JoinDiscordCommand() {
		super("join", 1, 1, "accept");
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
		String [] split = args [0].split("/");
		String code = split[split.length - 1];
		JDA jda = KiraMain.getInstance().getJDA();
		Invite invite = Invite.resolve(jda, code).complete();
		if (invite == null) {
			return "Could not resolve invite";
		}
		String url = jda.asBot().getInviteUrl();
		url += "&guild_id=";
		url += invite.getGuild().getIdLong();
		return url;
	}

	@Override
	public String getUsage() {
		return "join [invitelink]";
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

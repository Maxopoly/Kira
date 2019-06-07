package com.github.maxopoly.kira.command.discord.admin;

import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.UserManager;
import com.github.maxopoly.kira.KiraMain;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class SyncUsernameCommand extends Command {

	public SyncUsernameCommand() {
		super("syncusernames");
	}

	@Override
	public String getFunctionality() {
		return "Updates all discord names to match the linked ingame names";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "syncusernames";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		UserManager userMan = KiraMain.getInstance().getUserManager();
		Guild guild = KiraMain.getInstance().getGuild();
		Member self = KiraMain.getInstance().getGuild().getSelfMember();
		StringBuilder sb = new StringBuilder();
		userMan.getAllUsers().stream().forEach(user -> {
			if (!user.hasIngameAccount()) {
				return;
			}
			Member member = guild.getMemberById(user.getDiscordID());
			if (member == null) {
				return; // not in the discord
			}
			if (member.getEffectiveName().equals(user.getName())) {
				return; //already correct
			}
			if (!self.canInteract(member)) {
				return; //no perm
			}
			sb.append("Changing name of " + user.toString() + " from " + member.getNickname() + " to " + user.getName() + "\n");
			guild.getController().setNickname(member, user.getName()).queue();

		});
		sb.append("Successfully updated all usernames");
		return sb.toString();
	}

}

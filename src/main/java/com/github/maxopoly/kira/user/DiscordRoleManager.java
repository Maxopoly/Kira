package com.github.maxopoly.kira.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class DiscordRoleManager {

	private Guild guild;
	private long roleID;
	private Logger logger;
	private UserManager userManager;

	private final ScheduledExecutorService scheduler;

	public DiscordRoleManager(Guild guild, long roleID, Logger logger, UserManager userManager) {
		this.guild = guild;
		this.userManager = userManager;
		this.roleID = roleID;
		this.logger = logger;
		this.scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(() -> {
			syncFully();
		}, 60, 60, TimeUnit.SECONDS);
	}

	public void giveDiscordRole(KiraUser user) {
		Role role = guild.getRoleById(roleID);
		if (role == null) {
			logger.warn(
					"Could not add auth role to " + user.toString() + ", role with id " + roleID + " did not exist");
			return;
		}
		if (user.getName() == null) {
			logger.warn("Could not add auth role to " + user.toString() + ", no name was tied");
			return;
		}
		logger.info("Giving auth role to " + user.getName());
		Member member = guild.getMemberById(user.getDiscordID());
		if (member == null) {
			logger.warn("Could not add auth role to " + user.toString() + ", he was not in the official discord");
			return;
		}
		Member self = guild.getSelfMember();
		if (self.canInteract(member)) {
			//has to be complete() instead of queue() because of a bug in JDA/Discord which results in race conditions
			guild.getController().setNickname(member, user.getName()).complete();
		}
		guild.getController().addSingleRoleToMember(member, role).queue();
	}

	public void syncFully() {
		Set<KiraUser> authUsers = userManager.getAllUsers();
		authUsers.stream().filter(u -> u.hasIngameAccount());
		Role role = guild.getRoleById(roleID);
		List<Member> members = guild.getMembersWithRoles(role);

		Map<Long, KiraUser> userByDiscordID = new HashMap<>();
		authUsers.forEach(u -> userByDiscordID.put(u.getDiscordID(), u));

		Map<Long, Member> memberByDiscordID = new HashMap<>();
		members.forEach(m -> memberByDiscordID.put(m.getUser().getIdLong(), m));

		// filter down, so only the users who have the role but shouldn't have it
		// remain, then take their role
		members.stream().filter(m -> !userByDiscordID.containsKey(m.getUser().getIdLong())).forEach(member -> {
			if (member.isOwner()) {
				return;
			}
			takeDiscordRole(member);
		});

		// same thing other way around, get the users which should be added and give it
		// to them
		authUsers.stream().filter(us -> us.hasIngameAccount() && !memberByDiscordID.containsKey(us.getDiscordID()))
				.forEach(user -> {
					Member member = guild.getMemberById(user.getDiscordID());
					if (member == null || member.isOwner()) {
						return;
					}
					giveDiscordRole(user);
				});

		// also make sure to update all user names
		Member self = guild.getSelfMember();
		members.forEach(m -> {
			KiraUser tiedUser = userByDiscordID.get(m.getUser().getIdLong());
			if (tiedUser != null && self.canInteract(m)) {
				guild.getController().setNickname(m, tiedUser.getName()).queue();
			}
		});
	}

	public boolean takeDiscordRole(KiraUser user) {
		if (!user.hasDiscord()) {
			logger.warn("Could not remove " + user.toString() + " from auth role, no discord account associated");
			return false;
		}
		Member member = guild.getMemberById(user.getDiscordID());
		if (member == null) {
			logger.warn("Could not remove " + user.toString() + " from auth role, discord account not found");
			return false;
		}
		return takeDiscordRole(member);
	}

	public boolean takeDiscordRole(Member member) {
		Role role = guild.getRoleById(roleID);
		if (member == null) {
			logger.warn("Could not remove null member");
			return false;
		}
		if (role == null) {
			logger.warn("Could not remove role from " + member.getEffectiveName() + ", role with id " + roleID
					+ " did not exist");
			return false;
		}
		logger.info("Taking auth role from " + member.getEffectiveName());
		guild.getController().removeSingleRoleFromMember(member, role).queue();
		return true;
	}
}

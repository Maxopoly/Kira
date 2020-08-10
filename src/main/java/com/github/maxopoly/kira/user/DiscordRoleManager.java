package com.github.maxopoly.kira.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.KiraMain;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.internal.requests.DeferredRestAction;

public class DiscordRoleManager {
	private long roleID;
	private Logger logger;
	private UserManager userManager;

	private final ScheduledExecutorService scheduler;

	public DiscordRoleManager(long roleID, Logger logger, UserManager userManager) {
		this.userManager = userManager;
		this.roleID = roleID;
		this.logger = logger;
		this.scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(() -> {
			try {
				syncFully();
			}
			catch (Exception e) {
				KiraMain.getInstance().getLogger().error("Failed to fix user roles", e);
			}
		}, 60, 60, TimeUnit.SECONDS);
	}

	public void giveDiscordRole(KiraUser user) {
		Guild guild = KiraMain.getInstance().getGuild();
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
			guild.modifyNickname(member, user.getName()).queue();
		}
		guild.addRoleToMember(member, role).queue(); 
	}

	public void syncFully() {
		Guild guild = KiraMain.getInstance().getGuild();
		Set<KiraUser> authUsers = userManager.getAllUsers();
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
			takeDiscordRole(guild, member);
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
				try {
					guild.modifyNickname(m, tiedUser.getName()).queue();
				}
				catch (ErrorResponseException e) {
					logger.error("Failed to update nickname for " + m + "; " + tiedUser.getName());
				}
			}
		});
	}

	public boolean takeDiscordRole(Guild guild, KiraUser user) {
		
		if (!user.hasDiscord()) {
			logger.warn("Could not remove " + user.toString() + " from auth role, no discord account associated");
			return false;
		}
		Member member = guild.getMemberById(user.getDiscordID());
		if (member == null) {
			logger.warn("Could not remove " + user.toString() + " from auth role, discord account not found");
			return false;
		}
		return takeDiscordRole(guild, member);
	}

	public boolean takeDiscordRole(Guild guild, Member member) {
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
		guild.removeRoleFromMember(member, role).queue();
		return true;
	}
}

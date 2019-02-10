package com.github.maxopoly.Kira.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.database.DAO;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class DiscordRoleManager {

	private Guild guild;
	private long roleID;
	private Logger logger;
	private UserManager userManager;

	public DiscordRoleManager(Guild guild, long roleID, Logger logger, UserManager userManager) {
		this.guild = guild;
		this.userManager = userManager;
		this.roleID = roleID;
		this.logger = logger;
	}

	public boolean takeDiscordRole(User user) {
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
		guild.getController().removeSingleRoleFromMember(member, role).queue();
		return true;
	}

	public void giveDiscordRole(User user) {
		Role role = guild.getRoleById(roleID);
		if (role == null) {
			logger.warn("Could not add role to " + user.toString() + ", role with id " + roleID + " did not exist");
			return;
		}
		if (user.getName() == null) {
			logger.warn("Could not add role to " + user.toString() + ", no name was tied");
			return;
		}
		Member member = guild.getMemberById(user.getDiscordID());
		Member self = guild.getSelfMember();
		if (self.canInteract(member)) {
			guild.getController().setNickname(member, user.getName()).queue();;
		}
		guild.getController().addSingleRoleToMember(member, role).queue();
	}

	public void syncFully() {
		Set<User> authUsers = userManager.getAllUsers();
		authUsers.stream().filter(u -> u.hasIngameAccount());
		Role role = guild.getRoleById(roleID);
		List<Member> members = guild.getMembersWithRoles(role);

		Map<Long, User> userByDiscordID = new HashMap<>();
		authUsers.forEach(u -> userByDiscordID.put(u.getDiscordID(), u));

		Map<Long, Member> memberByDiscordID = new HashMap<>();
		members.forEach(m -> memberByDiscordID.put(m.getUser().getIdLong(), m));

		// filter down, so only the users who have the role but shouldn't have it
		// remain, then take their role
		members.stream().filter(m -> !userByDiscordID.containsKey(m.getUser().getIdLong())).forEach(member -> {
			if (member.isOwner()) {
				return;
			}
			logger.info("Taking auth role from " + member.getEffectiveName());
			guild.getController().removeRolesFromMember(member, role).queue();
		});
		;

		// same thing other way around, get the users which should be added and give it
		// to them
		authUsers.stream().filter(us -> us.hasIngameAccount() && !memberByDiscordID.containsKey(us.getDiscordID()))
				.forEach(user -> {
					logger.info("Giving auth role to " + user.getName());
					Member member = guild.getMemberById(user.getDiscordID());
					if (member == null || member.isOwner()) {
						return;
					}
					giveDiscordRole(user);
				});
	}
}

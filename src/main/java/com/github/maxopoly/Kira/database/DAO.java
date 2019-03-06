package com.github.maxopoly.Kira.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.permission.KiraPermission;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.relay.RelayConfig;
import com.github.maxopoly.Kira.relay.RelayConfigManager;
import com.github.maxopoly.Kira.user.KiraUser;
import com.github.maxopoly.Kira.user.UserManager;

public class DAO {

	private static final String timestampField = "last_updated timestamp with time zone not null default now()";

	private DBConnection db;
	private Logger logger;

	public DAO(DBConnection connection, Logger logger) {
		this.logger = logger;
		this.db = connection;
		if (!createTables()) {
			logger.error("Failed to init account DB, shutting down");
			System.exit(1);
		}
	}

	private boolean createTables() {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users "
					+ "(id serial primary key, discord_id bigint not null unique, name varchar(255) unique, "
					+ "uuid char(36) unique, reddit varchar(255) unique," + timestampField + ");")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS permissions "
					+ "(id serial primary key, name varchar(255) not null unique," + timestampField + ");")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS relay_configs "
					+ "(id serial primary key, name varchar(255) not null unique, relayFromDiscord boolean not null, "
					+ "relayToDiscord boolean not null, showSnitches boolean not null, deleteMessages boolean not null, chatFormat text not null,"
					+ "snitchFormat text not null, loginAction text not null, logoutAction text not null,"
					+ "enterAction text not null, hereFormat text not null, everyoneFormat text not null, "
					+ "canPing boolean not null, timeFormat text not null, owner_id int references users (id) on delete cascade," + timestampField
					+ ");")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS group_chats "
					+ "(id serial primary key, channel_id bigint, guild_id bigint, name varchar(255) not null unique, "
					+ "role_id int references roles(id) on delete cascade, creator_id int references users(id) "
					+ "on delete cascade, config_id int references relay_configs(id)," + timestampField + ");")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS roles "
					+ "(id serial primary key, name varchar(255) not null unique," + timestampField + ");")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS role_permissions "
					+ "(role_id int references roles(id) on delete cascade, "
					+ "permission_id int references permissions(id) on delete cascade," + timestampField
					+ ", unique(role_id, permission_id));")) {
				prep.execute();
			}
			try (PreparedStatement prep = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS role_members " + "(user_id int references users(id) on delete cascade, "
							+ "role_id int references roles(id) on delete cascade," + timestampField
							+ ", unique(role_id, user_id));")) {
				prep.execute();
			}
		} catch (SQLException e) {
			logger.error("Failed to create table", e);
			return false;
		}
		return true;
	}

	public int createGroupChat(long guildID, long channelID, String name, KiraRole tiedPerm, int creatorID, RelayConfig config) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("insert into group_chats (channel_id, guild_id, "
						+ "role_id, name, creator_id, config_id) values (?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS)) {
			prep.setLong(1, channelID);
			prep.setLong(2, guildID);
			prep.setInt(3, tiedPerm.getID());
			prep.setString(4, name);
			prep.setInt(5, creatorID);
			prep.setInt(6, config.getID());
			prep.execute();
			try (ResultSet rs = prep.getGeneratedKeys()) {
				if (!rs.next()) {
					logger.error("No key created for group chat?");
					return -1;
				}
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Failed to create group chat", e);
			return -1;
		}
	}

	public Set<String> getGroupChatChannelIdByCreator(KiraUser user) {
		Set<String> result = new TreeSet<>();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn
						.prepareStatement("select name from group_chats where creator_id = ?;")) {
			prep.setInt(1, user.getID());
			try (ResultSet rs = prep.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString(1);
					result.add(name);
				}
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve group chats by creator", e);
			return null;
		}
		return result;
	}

	public void deleteGroupChat(GroupChat chat) {
		// everything else will cascade
		deleteRole(chat.getTiedRole());
	}

	public void setRelayConfigForChat(GroupChat chat, RelayConfig relay) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"update group_chats set config_id = ? where id = ?;")) {
			prep.setInt(1, relay.getID());
			prep.setInt(2, chat.getID());
			prep.executeUpdate();
		} catch (SQLException e) {
			logger.error("Failed to update relay config entry", e);
		}
	}

	public Set<Integer> getGroupChatMembers(GroupChat groupchat) {
		Set<Integer> result = new TreeSet<>();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("select user_id from role_members where role_id = ?;")) {
			prep.setInt(1, groupchat.getTiedRole().getID());
			try (ResultSet rs = prep.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					result.add(id);
				}
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve group chat members", e);
			return null;
		}
		return result;
	}

	public RelayConfig createRelayConfig(String name, boolean relayFromDiscord, boolean relayToDiscord, boolean showSnitches,
			boolean deleteMessages, String chatFormat, String snitchFormat, String loginAction, String logoutAction,
			String enterAction, String hereFormat, String everyoneFormat, boolean canPing, String timeFormat, KiraUser creator) {
		int creatorID = creator == null ? 1 : creator.getID();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"insert into relay_configs (relayFromDiscord, relayToDiscord, showSnitches,"
								+ "deleteMessages, chatFormat, snitchFormat, loginAction, logoutAction, enterAction, hereFormat, everyoneFormat,"
								+ "canPing, owner_id, name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
						Statement.RETURN_GENERATED_KEYS)) {
			prep.setBoolean(1, relayFromDiscord);
			prep.setBoolean(2, relayToDiscord);
			prep.setBoolean(3, showSnitches);
			prep.setBoolean(4, deleteMessages);
			prep.setString(5, chatFormat);
			prep.setString(6, snitchFormat);
			prep.setString(7, loginAction);
			prep.setString(8, logoutAction);
			prep.setString(9, enterAction);
			prep.setString(10, hereFormat);
			prep.setString(11, everyoneFormat);
			prep.setBoolean(12, canPing);
			prep.setInt(13, creatorID);
			prep.setString(14, name);
			prep.execute();
			try (ResultSet rs = prep.getGeneratedKeys()) {
				if (!rs.next()) {
					logger.error("No key created for relay config?");
					return null;
				}
				int id = rs.getInt(1);
				return new RelayConfig(id, name, relayFromDiscord, relayToDiscord, showSnitches, deleteMessages, snitchFormat,
						loginAction, logoutAction, enterAction, chatFormat, hereFormat, everyoneFormat, canPing, timeFormat, 
						creatorID);
			}
		} catch (SQLException e) {
			logger.error("Failed to create relay config", e);
			return null;
		}
	}

	public Collection<RelayConfig> loadRelayConfigs() {
		List<RelayConfig> result = new LinkedList<>();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn
						.prepareStatement("select relayFromDiscord, relayToDiscord, showSnitches, deleteMessages, "
								+ "chatFormat, snitchFormat, loginAction, logoutAction, enterAction, hereFormat, everyoneFormat,"
								+ "canPing, owner_id, id, name, timeFormat from relay_configs;");
				ResultSet rs = prep.executeQuery()) {
			while (rs.next()) {
				boolean relayFromDiscord = rs.getBoolean(1);
				boolean relayToDiscord = rs.getBoolean(2);
				boolean showSnitches = rs.getBoolean(3);
				boolean deleteMessages = rs.getBoolean(4);
				String chatFormat = rs.getString(5);
				String snitchFormat = rs.getString(6);
				String loginAction = rs.getString(7);
				String logoutAction = rs.getString(8);
				String enterAction = rs.getString(9);
				String hereFormat = rs.getString(10);
				String everyoneFormat = rs.getString(11);
				boolean canPing = rs.getBoolean(12);
				int ownerID = rs.getInt(13);
				int id = rs.getInt(14);
				String name = rs.getString(15);
				String timeFormat = rs.getString(16);
				RelayConfig config = new RelayConfig(id, name, relayFromDiscord, relayToDiscord, showSnitches,
						deleteMessages, snitchFormat, loginAction, logoutAction, enterAction, chatFormat,
						hereFormat, everyoneFormat, canPing, timeFormat, ownerID);
				result.add(config);
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve relay configs", e);
			return null;
		}
		return result;
	}

	public void updateRelayConfig(RelayConfig config) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"update relay_configs set relayFromDiscord = ?, relayToDiscord = ?, showSnitches = ?, deleteMessages = ?, chatFormat = ?,"
						+ "snitchFormat = ?, loginAction = ?, logoutAction = ?, enterAction = ?, hereFormat = ?, everyoneFormat = ?,"
						+ "canPing = ?, timeFormat = ? where id = ?;")) {
			prep.setBoolean(1, config.shouldRelayFromDiscord());
			prep.setBoolean(2, config.shouldRelayToDiscord());
			prep.setBoolean(3, config.shouldShowSnitches());
			prep.setBoolean(4, config.shouldDeleteDiscordMessage());
			prep.setString(5, config.getChatFormat());
			prep.setString(6, config.getSnitchFormat());
			prep.setString(7, config.getSnitchLoginAction());
			prep.setString(8, config.getSnitchLogoutAction());
			prep.setString(9, config.getSnitchEnterString());
			prep.setString(10, config.getHereFormat());
			prep.setString(11, config.getEveryoneFormat());
			prep.setBoolean(12, config.shouldPing());
			prep.setString(13, config.getTimeFormat());
			prep.setInt(14, config.getID());
			prep.executeUpdate();
		} catch (SQLException e) {
			logger.error("Failed to update relay", e);
		}
	}

	public Collection<GroupChat> loadGroupChats(RelayConfigManager relayConfigs) {
		List<GroupChat> result = new LinkedList<>();
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		UserManager userMan = KiraMain.getInstance().getUserManager();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"select id, channel_id, guild_id, name, role_id, creator_id, config_id from group_chats;");
				ResultSet rs = prep.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt(1);
				long channelID = rs.getLong(2);
				long guildID = rs.getLong(3);
				String name = rs.getString(4);
				int roleID = rs.getInt(5);
				int creatorID = rs.getInt(6);
				KiraRole role = roleMan.getRole(roleID);
				if (role == null) {
					logger.warn("Could not load group chat " + name + ", no role found");
					continue;
				}
				int configId = rs.getInt(7);
				GroupChat group = new GroupChat(id, name, channelID, guildID, role, userMan.getUser(creatorID), relayConfigs.getById(configId));
				result.add(group);
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve group chats", e);
			return null;
		}
		return result;
	}

	public Set<KiraUser> loadUsers() {
		Set<KiraUser> result = new HashSet<>();
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("select id, name, discord_id, uuid, reddit from users;");
				ResultSet rs = prep.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				long discordID = rs.getLong(3);
				String uuidString = rs.getString(4);
				UUID uuid = uuidString != null ? UUID.fromString(uuidString) : null;
				String redditAcc = rs.getString(5);
				result.add(new KiraUser(id, name, discordID, uuid, redditAcc));
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve users", e);
			return null;
		}
		if (result.isEmpty()) {
			int first = createUser(-1L);
			result.add(new KiraUser(first, null, -1L, null, null));
		}
		return result;
	}

	public int createUser(long discordID) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("insert into users (discord_id) values (?);",
						Statement.RETURN_GENERATED_KEYS)) {
			prep.setLong(1, discordID);
			prep.execute();
			try (ResultSet rs = prep.getGeneratedKeys()) {
				if (!rs.next()) {
					logger.error("No key created for user?");
					return -1;
				}
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Failed to create user", e);
			return -1;
		}
	}

	public void updateUser(KiraUser user) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"update users set name = ?, discord_id = ?, uuid = ?, reddit = ? where id = ?;")) {
			prep.setString(1, user.getName());
			if (user.hasDiscord()) {
				prep.setLong(2, user.getDiscordID());
			} else {
				prep.setObject(2, null);
			}
			if (user.getIngameUUID() != null) {
				prep.setString(3, user.getIngameUUID().toString());
			} else {
				prep.setString(3, null);
			}
			prep.setString(4, user.getRedditAccount());
			prep.setInt(5, user.getID());
			prep.executeUpdate();
		} catch (SQLException e) {
			logger.error("Failed to update user", e);
		}
	}

	public KiraPermission retrieveOrCreatePermission(String name) {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("select id from permissions where name = ?;")) {
				ps.setString(1, name);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int id = rs.getInt(1);
						return new KiraPermission(id, name);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve permission", e);
			return null;
		}
		return registerPermission(name);
	}

	public KiraRole retrieveOrCreateRole(String name) {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("select id from roles where name = ?;")) {
				ps.setString(1, name);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int id = rs.getInt(1);
						return new KiraRole(name, id);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Failed to retrieve role", e);
			return null;
		}
		return registerRole(name);
	}

	public KiraPermission registerPermission(String perm) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("insert into permissions (name) values (?);",
						Statement.RETURN_GENERATED_KEYS)) {
			prep.setString(1, perm);
			prep.execute();
			try (ResultSet rs = prep.getGeneratedKeys()) {
				if (!rs.next()) {
					logger.error("No key created for perm?");
					return null;
				}
				int id = rs.getInt(1);
				return new KiraPermission(id, perm);
			}
		} catch (SQLException e) {
			logger.error("Failed to create permission", e);
			return null;
		}
	}

	public KiraRole registerRole(String name) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("insert into roles (name) values (?);",
						Statement.RETURN_GENERATED_KEYS)) {
			prep.setString(1, name);
			prep.execute();
			try (ResultSet rs = prep.getGeneratedKeys()) {
				if (!rs.next()) {
					logger.error("No key created for role?");
					return null;
				}
				int id = rs.getInt(1);
				return new KiraRole(name, id);
			}
		} catch (SQLException e) {
			logger.error("Failed to create role", e);
			return null;
		}
	}

	public void addPermissionToRole(KiraPermission permission, KiraRole role) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"insert into role_permissions (role_id, permission_id) " + "values (?, ?);")) {
			prep.setInt(1, role.getID());
			prep.setInt(2, permission.getID());
			prep.execute();
		} catch (SQLException e) {
			logger.error("Failed to insert role permission", e);
		}
	}

	public void removePermissionFromRole(KiraPermission permission, KiraRole role) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement(
						"insert into role_permissions (role_id, permission_id) " + "values (?, ?);")) {
			prep.setInt(1, role.getID());
			prep.setInt(2, permission.getID());
			prep.execute();
		} catch (SQLException e) {
			logger.error("Failed to insert role permission", e);
		}
	}

	public void deleteRole(KiraRole role) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn.prepareStatement("delete from roles where id = ?;")) {
			prep.setInt(1, role.getID());
			prep.execute();
		} catch (SQLException e) {
			logger.error("Failed to delete role", e);
		}
	}

	public void addUserToRole(KiraUser user, KiraRole role) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn
						.prepareStatement("insert into role_members (role_id, user_id) " + "values (?, ?);")) {
			prep.setInt(1, role.getID());
			prep.setInt(2, user.getID());
			prep.execute();
		} catch (SQLException e) {
			logger.error("Failed to insert role addition for user", e);
		}
	}

	public void takeRoleFromUser(KiraUser user, KiraRole role) {
		try (Connection conn = db.getConnection();
				PreparedStatement prep = conn
						.prepareStatement("delete from role_members where role_id=?, user_id=?;")) {
			prep.setInt(1, role.getID());
			prep.setInt(2, user.getID());
			prep.execute();
		} catch (SQLException e) {
			logger.error("Failed to delete role for user", e);
		}
	}

	public KiraRoleManager loadAllRoles() {
		KiraRoleManager manager = new KiraRoleManager();
		Map<Integer, KiraPermission> permsById = new TreeMap<Integer, KiraPermission>();
		Map<Integer, KiraRole> roleById = new TreeMap<Integer, KiraRole>();
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("select id, name from permissions;");
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					String name = rs.getString(2);
					KiraPermission perm = new KiraPermission(id, name);
					permsById.put(id, perm);
				}
			}
			try (PreparedStatement ps = conn.prepareStatement("select id, name from roles;");
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					String name = rs.getString(2);
					KiraRole role = new KiraRole(name, id);
					roleById.put(id, role);
					manager.registerRole(role);
				}
			}
			try (PreparedStatement ps = conn.prepareStatement("select role_id, permission_id from role_permissions;");
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int roleID = rs.getInt(1);
					int permissionID = rs.getInt(2);
					KiraRole role = roleById.get(roleID);
					KiraPermission perm = permsById.get(permissionID);
					role.addPermission(perm);
				}
			}
			try (PreparedStatement ps = conn.prepareStatement("select user_id, role_id from role_members;");
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int userID = rs.getInt(1);
					int roleID = rs.getInt(2);
					KiraRole role = roleById.get(roleID);
					manager.addRole(userID, role, false);
				}
			}
		} catch (SQLException e) {
			logger.error("Failed to load permissions", e);
			return null;
		}
		return manager;
	}

}

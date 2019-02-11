package com.github.maxopoly.Kira.group;

import java.util.HashMap;
import java.util.Map;

import com.github.maxopoly.Kira.database.DAO;

public class GroupChatManager {
	
	private Map<String, GroupChat> groupChatByName;
	private DAO dao;
	
	public GroupChatManager(DAO dao) {
		groupChatByName = new HashMap<String, GroupChat>();
		this.dao = dao;
		for(GroupChat chat : dao.loadGroupChats()) {
			groupChatByName.put(chat.getName(), chat);
		}
	}
	
	public GroupChat getGroupChat(String name) {
		return groupChatByName.get(name);
	}
	
	public void putGroupChat(GroupChat chat) {
		groupChatByName.put(chat.getName(), chat);
	}

}

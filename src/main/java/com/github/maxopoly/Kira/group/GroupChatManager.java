package com.github.maxopoly.Kira.group;

import java.util.HashMap;
import java.util.Map;

public class GroupChatManager {
	
	private Map<String, GroupChat> groupChatByName;
	
	public GroupChatManager() {
		groupChatByName = new HashMap<String, GroupChat>();
	}
	
	public GroupChat getGroupChat(String name) {
		return groupChatByName.get(name);
	}
	
	public void putGroupChat(GroupChat chat) {
		groupChatByName.put(chat.getName(), chat);
	}

}

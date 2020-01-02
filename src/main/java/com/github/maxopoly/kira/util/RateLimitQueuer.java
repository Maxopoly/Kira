package com.github.maxopoly.kira.util;

import java.util.List;
import java.util.Map;

public abstract class RateLimitQueuer {
	
	private Map<Long, StringBuilder> queuedMessagesByKey;
	private List<StringBuilder> messageQueue;
	
	public void queue(long id, String msg) {
				
	}
	
	public abstract void sendMessage(long id, String msg);

}

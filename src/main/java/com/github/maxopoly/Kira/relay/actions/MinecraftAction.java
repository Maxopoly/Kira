package com.github.maxopoly.Kira.relay.actions;

/**
 * Something that happened in Minecraft like a player loggin in, hitting a snitch etc.
 *
 */
public abstract class MinecraftAction {
	
	private long timeStamp;
	
	public MinecraftAction(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

}

package com.github.maxopoly.Kira.relay.actions;

public class SkynetAction extends MinecraftAction {
	
	private SkynetType type;
	private String player;
	
	public SkynetAction(long timestamp, String player, SkynetType type) {
		super(timestamp);
		this.player = player;
		this.type = type;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public SkynetType getType() {
		return type;
	}

}

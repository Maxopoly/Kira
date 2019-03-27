package com.github.maxopoly.Kira.relay.actions;

public class PlayerHitSnitchAction extends MinecraftAction {
	
	private String playerName;
	private String snitchName;
	private MinecraftLocation location;
	private SnitchHitType hitType;
	private String groupName;
	
	public PlayerHitSnitchAction(long timestamp, String playerName, String snitchname, String groupName, MinecraftLocation location, SnitchHitType hitType) {
		super(timestamp);
		this.playerName = playerName;
		this.snitchName = snitchname;
		this.location= location;
		this.hitType = hitType;
		this.groupName = groupName;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getSnitchName() {
		return snitchName;
	}
	
	public MinecraftLocation getLocation() {
		return location;
	}
	
	public SnitchHitType getHitType() {
		return hitType;
	}

}

package crcm.model;

import javax.json.Json;
import javax.json.JsonObject;

public class Player implements Model
{
	private String playerName;
	private long playerId64;
	private PlayerStats playerStats;
	private PlayerState playerState;
	
	@Override
	public String toJsonString() 
	{
		return String.format("{\"%s\":\"%s\",\"%s\":%d, \"%s\":%s, \"%s\":%s}", 
				"playername", playerName, "id64", playerId64, "stats", 
				playerStats.toJsonString(), playerState.toJsonString());
	}
	
	@Override
	public JsonObject toJson() 
	{
		return Json.createObjectBuilder()
				.add("playername", playerName)
				.add("id64", playerId64)
				.add("stats", playerStats.toJson())
				.add("state", playerState.toJson())
				.build();
	}
}

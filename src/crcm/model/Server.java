package crcm.model;

import javax.json.JsonObject;

import talloniv.networking.server.RemoteClient;

public class Server implements Model
{

	private RemoteClient registeredBy;
	private String gameServerIp;
	private int gameServerPort;
	private int modMode;
	
	public RemoteClient getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(RemoteClient registeredBy) {
		this.registeredBy = registeredBy;
	}

	public String getGameServerIp() {
		return gameServerIp;
	}

	public void setGameServerIp(String gameServerIp) {
		this.gameServerIp = gameServerIp;
	}

	public int getGameServerPort() {
		return gameServerPort;
	}

	public void setGameServerPort(int gameServerPort) {
		this.gameServerPort = gameServerPort;
	}

	public int getModMode() {
		return modMode;
	}

	public void setModMode(int modMode) {
		this.modMode = modMode;
	}

	@Override
	public String toJsonString() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject toJson() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString()
	{
		return gameServerIp+":"+String.valueOf(gameServerPort);
	}

}

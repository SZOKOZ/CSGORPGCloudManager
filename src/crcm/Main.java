package crcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import crcm.model.ModelEnums;
import crcm.model.Server;
import crcm.protocol.JsonMessage;
import crcm.protocol.JsonProtocol;
import talloniv.networking.IProtocolMessage;
import talloniv.networking.server.IServerTask;
import talloniv.networking.server.RemoteClient;
import talloniv.networking.server.ServerBlockingThread;

public class Main 
{

	private static ArrayList<Server> registeredServers = new ArrayList<Server>();
	public static void main(String[] args) throws IOException 
	{
		ServerBlockingThread server = 
				new ServerBlockingThread(7878, true, new JsonProtocol());
		
		IServerTask serverRegistrationTask = new IServerTask()
				{

					@Override
					public boolean OnConnect(RemoteClient client) {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public void OnConnected(RemoteClient client) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void OnDataReceived(RemoteClient client, IProtocolMessage data) 
					{
						JsonObject json = (JsonObject) data.Extract();
						String state = json.getString("state");
						if (state == null)
							return;
						
						JsonObject message = null;
						if (state.equals("REGISTER"))
						{
							message = json.getJsonObject("message");
						}
						
						if (message == null)
							return;
						
						Server server = new Server();
						server.setRegisteredBy(client);
						server.setGameServerIp(client.GetIpAddress());
						server.setGameServerPort(message.getInt("hostport"));
						server.setModMode(message.getInt("modmode"));
						for (int i = 0; i < registeredServers.size(); i++)
						{
							Server registeredServer = registeredServers.get(i);
							if (server.getGameServerIp().equals(registeredServer.getGameServerIp()) && 
									server.getGameServerPort() == registeredServer.getGameServerPort())
							{
								client.Send(new JsonMessage(
										Json.createObjectBuilder().add("state", "REGISTER")
										.add("message", Json.createObjectBuilder()
												.add("status", "EXISTS"))
										.toString()));
								return;
							}
						}
						
						registeredServers.add(server);
						client.Send(new JsonMessage(
								Json.createObjectBuilder().add("state", "REGISTER")
								.add("message", Json.createObjectBuilder()
										.add("status", "ACCEPTED"))
								.build()
								.toString()));
						
						System.out.println("ServerList:"+registeredServers);
					}

					@Override
					public void OnDataSent(RemoteClient client, IProtocolMessage data) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void OnDisconnected(RemoteClient client) 
					{
						for (int i = 0; i < registeredServers.size(); i++)
						{
							Server registeredServer = registeredServers.get(i);
							if (registeredServer.getRegisteredBy() == client)
							{
								System.out.println("Bye Bye "+registeredServers.get(i));
								registeredServers.remove(i);
								System.out.println(registeredServers);
								return;
							}
						}
					}
			
				};
				
		IServerTask playerPrepTeleportTask = new IServerTask()
				{

					@Override
					public boolean OnConnect(RemoteClient client) {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public void OnConnected(RemoteClient client) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void OnDataReceived(RemoteClient client, IProtocolMessage data) {
						JsonObject json = (JsonObject) data.Extract();
						String state = json.getString("state");
						if (state == null)
						{
							System.out.println("No state, returning...");
							return;
						}
							
						
						JsonObject message = null;
						if (state.equals("TESTAUTHORISEPLAYER"))
						{
							message = json.getJsonObject("message");
						}
						
						if (message == null)
						{
							System.out.println("No message, returning...");
							return;
						}
							
						
						JsonArray players = message.getJsonArray("players");
						if (players == null)
						{
							System.out.println("No players, returning...");
							return;
						}
							
						
						Server missionServer = null;
						for (int i = 0; i < registeredServers.size(); i++)
						{
							if (registeredServers.get(i).getModMode() == 
									ModelEnums.MODMODE_MISSION.getValue())
								missionServer = registeredServers.get(i);
						}
						
						if (missionServer == null)
						{
							System.out.println("no mission server available");
							return;
						}
						
						missionServer.getRegisteredBy().Send(new JsonMessage(Json.createObjectBuilder()
								.add("state", "TESTAUTHORISEPLAYER")
								.add("message", Json.createObjectBuilder()
										.add("players", players))
								.build()
								.toString()));
						
						client.Send(new JsonMessage(Json.createObjectBuilder()
								.add("state", "TESTAUTHORISEPLAYER")
								.add("message", Json.createObjectBuilder()
										.add("players", players)
										.add("missionserver", missionServer.getGameServerIp()
												+":"+String.valueOf(missionServer.getGameServerPort())))
								.build()
								.toString())
								);
						System.out.println("Teleportation for player prepared!...");
					}

					@Override
					public void OnDataSent(RemoteClient client, IProtocolMessage data) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void OnDisconnected(RemoteClient client) {
						// TODO Auto-generated method stub
						
					}
			
				};
				
		server.AddServerTask(serverRegistrationTask);
		server.AddServerTask(playerPrepTeleportTask);
		server.Start();
	}
}

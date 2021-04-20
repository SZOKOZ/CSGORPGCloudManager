package crcm.protocol;

import java.io.StringReader;

import javax.json.Json;

import talloniv.networking.IProtocol;
import talloniv.networking.IProtocolMessage;

public class JsonProtocol implements IProtocol
{

	@Override
	public boolean Verify(byte[] data) 
	{
		StringReader in = null;
		try 
		{
			in = new StringReader(new String(data, "UTF-8"));
			Json.createReader(in).readObject();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public IProtocolMessage Deserialise(byte[] data) 
	{
		return new JsonMessage(data);
	}

	@Override
	public byte[] Serialise(IProtocolMessage data) 
	{
		return (byte[])data.Pack();
	}

}

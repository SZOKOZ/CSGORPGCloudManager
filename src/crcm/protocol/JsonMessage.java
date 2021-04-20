package crcm.protocol;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.json.Json;

import talloniv.networking.IProtocolMessage;

public class JsonMessage implements IProtocolMessage 
{
	private long timeCreated = System.currentTimeMillis();
	private byte[] message;
	
	public JsonMessage(byte[] bytes)
	{
		if (!new JsonProtocol().Verify(bytes))
		{
			return;
		}
		
		message = bytes;
	}
	
	public JsonMessage(String s)
	{
		byte[] bytes = null;
		try 
		{
			bytes = s.getBytes("UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			return;
		}
		
		if (new JsonProtocol().Verify(bytes))
		{
			message = bytes;
		}
	}
	
	@Override
	public Object Extract() 
	{
		if (message == null)
			return null;
		
		StringReader in = null;
		try 
		{
			in = new StringReader(new String(message, "UTF-8"));
			return Json.createReader(in).readObject();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Object Pack() 
	{
		if (message == null)
			return null;
		
		return message;
	}
	
	public long timeCreated()
	{
		return timeCreated;
	}

}

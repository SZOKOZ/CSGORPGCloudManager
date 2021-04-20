package crcm.model;

import javax.json.JsonObject;

public interface Model 
{
	public String toJsonString();
	public JsonObject toJson(); 
}

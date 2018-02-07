package com.theprogrammingturkey.nhlapi.managers;

import com.google.gson.JsonObject;

public class BaseManager
{

	public static String getStringSafe(JsonObject json, String key)
	{
		return getStringSafe(json, key, "");
	}

	public static String getStringSafe(JsonObject json, String key, String defaultValue)
	{
		if(json.has(key))
			return json.get(key).getAsString();
		return defaultValue;
	}

	public static int getIntSafe(JsonObject json, String key)
	{
		return getIntSafe(json, key, -1);
	}

	public static int getIntSafe(JsonObject json, String key, int defaultValue)
	{
		if(json.has(key))
			return json.get(key).getAsInt();
		return defaultValue;
	}

	public static boolean getBooleanSafe(JsonObject json, String key)
	{
		return getBooleanSafe(json, key, false);
	}

	public static boolean getBooleanSafe(JsonObject json, String key, boolean defaultValue)
	{
		if(json.has(key))
			return json.get(key).getAsBoolean();
		return defaultValue;
	}
}

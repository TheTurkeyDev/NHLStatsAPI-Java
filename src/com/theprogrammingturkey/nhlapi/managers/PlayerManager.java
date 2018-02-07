package com.theprogrammingturkey.nhlapi.managers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.PlayerData;
import com.theprogrammingturkey.nhlapi.data.PositionData;

public class PlayerManager extends BaseManager
{

	private static Map<Integer, PlayerData> cache = new HashMap<>();

	public static PlayerData getPlayerDataFromJSON(JsonObject json)
	{
		int id = getIntSafe(json, "id");
		if(cache.containsKey(id))
			return cache.get(id);

		PlayerData player = new PlayerData();

		player.id = id;
		player.fullName = getStringSafe(json, "fullName");
		player.firstName = getStringSafe(json, "firstName");
		player.lastName = getStringSafe(json, "lastName");
		player.primaryNumber = getStringSafe(json, "primaryNumber");
		player.birthDate = getStringSafe(json, "birthDate");
		player.currentAge = getIntSafe(json, "currentAge");
		player.birthCity = getStringSafe(json, "birthCity");
		player.birthStateProvince = getStringSafe(json, "birthStateProvince");
		player.birthCountry = getStringSafe(json, "birthCountry");
		player.nationality = getStringSafe(json, "nationality");
		player.height = getStringSafe(json, "height");
		player.weight = getIntSafe(json, "weight");
		player.active = getBooleanSafe(json, "active");
		player.alternateCaptain = getBooleanSafe(json, "alternateCaptain");
		player.captain = getBooleanSafe(json, "captain");
		player.rookie = getBooleanSafe(json, "rookie");
		player.shootsCatches = getStringSafe(json, "shootsCatches");
		player.rosterStatus = getStringSafe(json, "rosterStatus");
		player.currentTeamID = getIntSafe(json, "currentTeamID");
		player.primaryPosition = getPositionDataFromJSON(json.get("primaryPosition").getAsJsonObject());

		cache.put(id, player);

		return player;
	}

	public static PositionData getPositionDataFromJSON(JsonObject json)
	{
		PositionData position = new PositionData();

		position.code = getStringSafe(json, "code");
		position.name = getStringSafe(json, "name");
		position.type = getStringSafe(json, "type");
		position.abbreviation = getStringSafe(json, "abbreviation");

		return position;
	}

	public static PlayerData getPlayerFromID(int id)
	{
		return cache.get(id);
	}

	public static void clearCache()
	{
		cache.clear();
	}
}

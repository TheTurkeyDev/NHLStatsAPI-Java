package com.theprogrammingturkey.nhlapi.managers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.data.PlayerData;

public class PlayManager extends BaseManager
{

	public static PlayData getPlayDataFromJSON(JsonObject json)
	{
		PlayData data = new PlayData();

		if(json == null)
			return data;

		if(json.has("team"))
			data.team = TeamManager.getTeamFromID(json.get("team").getAsJsonObject().get("id").getAsInt());

		if(json.has("players"))
		{
			Map<PlayerData, String> players = new HashMap<>();
			for(JsonElement playerElem : json.get("players").getAsJsonArray())
			{
				PlayerData playerData = PlayerManager.getPlayerFromID(playerElem.getAsJsonObject().get("player").getAsJsonObject().get("id").getAsInt());
				players.put(playerData, playerElem.getAsJsonObject().get("playerType").getAsString());
			}
			data.players = players;
		}

		JsonObject result = json.get("result").getAsJsonObject();
		data.event = getStringSafe(result, "event");
		data.eventCode = getStringSafe(result, "eventCode");
		data.eventTypeId = getStringSafe(result, "eventTypeId");
		data.description = getStringSafe(result, "description");
		data.secondaryType = getStringSafe(result, "secondaryType");

		JsonObject about = json.get("about").getAsJsonObject();
		data.eventIdx = getIntSafe(about, "eventIdx");
		data.eventId = getIntSafe(about, "eventId");
		data.period = getIntSafe(about, "period");
		data.periodType = getStringSafe(about, "periodType");
		data.ordinalNum = getStringSafe(about, "ordinalNum");
		data.periodTime = getStringSafe(about, "periodTime");
		data.periodTimeRemaining = getStringSafe(about, "periodTimeRemaining");
		data.dateTime = getStringSafe(about, "dateTime	");
		data.awayScore = getIntSafe(about.get("goals").getAsJsonObject(), "away");
		data.homeScore = getIntSafe(about.get("goals").getAsJsonObject(), "home");

		JsonObject coordinates = json.get("coordinates").getAsJsonObject();
		data.xCoord = getIntSafe(coordinates, "x");
		data.yCoord = getIntSafe(coordinates, "y");

		return data;
	}
}

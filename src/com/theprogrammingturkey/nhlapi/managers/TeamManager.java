package com.theprogrammingturkey.nhlapi.managers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.TeamData;

public class TeamManager extends BaseManager
{
	private static Map<Integer, TeamData> cache = new HashMap<>();

	public static TeamData getTeamDataFromJSON(JsonObject json)
	{
		int id = getIntSafe(json, "id");
		if(cache.containsKey(id))
			return cache.get(id);

		TeamData team = new TeamData();

		team.id = id;
		team.name = json.get("name").getAsString();
		team.teamName = json.get("name").getAsString();
		team.abbreviation = json.get("abbreviation").getAsString();
		team.triCode = json.get("teamName").getAsString();
		team.location = json.get("locationName").getAsString();
		team.firstYearOfPlay = json.get("firstYearOfPlay").getAsString();
		team.divisionName = json.get("division").getAsJsonObject().get("name").getAsString();
		team.divisionID = json.get("division").getAsJsonObject().get("id").getAsInt();
		team.conferenceName = json.get("conference").getAsJsonObject().get("name").getAsString();
		team.conferenceID = json.get("conference").getAsJsonObject().get("id").getAsInt();
		team.franchiseName = json.get("franchise").getAsJsonObject().get("teamName").getAsString();
		team.franchiseID = json.get("franchise").getAsJsonObject().get("franchiseId").getAsInt();
		team.shortName = json.get("shortName").getAsString();
		team.website = json.get("officialSiteUrl").getAsString();
		team.active = json.get("active").getAsBoolean();
		team.venue = VenueManager.getVenueDataFromJSON(json.get("venue").getAsJsonObject());

		cache.put(id, team);
		return team;
	}

	public static TeamData getTeamFromID(int id)
	{
		return cache.get(id);
	}

	public static void clearCache()
	{
		cache.clear();
	}
}

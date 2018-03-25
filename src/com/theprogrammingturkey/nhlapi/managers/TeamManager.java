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
		team.name = getStringSafe(json, "name");
		team.teamName = getStringSafe(json, "name");
		team.abbreviation = getStringSafe(json, "abbreviation");
		team.triCode = getStringSafe(json, "teamName");
		team.location = getStringSafe(json, "locationName");
		team.firstYearOfPlay = getStringSafe(json, "firstYearOfPlay");
		team.divisionName = getStringSafe(getJsonObjectSafe(json, "division"), "name");
		team.divisionID = getIntSafe(getJsonObjectSafe(json, "division"), "id");
		team.conferenceName = getStringSafe(getJsonObjectSafe(json, "conference"), "name");
		team.conferenceID = getIntSafe(getJsonObjectSafe(json, "conference"), "id");
		team.franchiseName = getStringSafe(getJsonObjectSafe(json, "franchise"), "teamName");
		team.franchiseID = getIntSafe(getJsonObjectSafe(json, "franchise"), "franchiseId");
		team.shortName = getStringSafe(json, "shortName");
		team.website = getStringSafe(json, "officialSiteUrl");
		team.active = getBooleanSafe(json, "active");
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

package com.theprogrammingturkey.nhlapi.managers;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.TeamRecordData;

public class TeamRecordManager extends BaseManager
{
	public static TeamRecordData getTeamRecordFromJSON(JsonObject json)
	{
		TeamRecordData record = new TeamRecordData();

		int w = getIntSafe(json, "wins", 0);
		int l = getIntSafe(json, "losses", 0);
		int ot = getIntSafe(json, "ot", 0);
		int ties = getIntSafe(json, "ties", 0);
		record.wins = w;
		record.losses = l;
		record.ot = ot;
		record.ties = ties;
		record.gamesPlayed = w + l + ot + ties;
		record.type = getStringSafe(json, "type");

		return record;
	}
}

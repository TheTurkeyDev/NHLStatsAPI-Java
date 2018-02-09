package com.theprogrammingturkey.nhlapi.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.BoxScoreData;
import com.theprogrammingturkey.nhlapi.data.GoalieStatData;
import com.theprogrammingturkey.nhlapi.data.PlayerStatData;
import com.theprogrammingturkey.nhlapi.data.SkaterStatData;
import com.theprogrammingturkey.nhlapi.data.TeamStatData;

public class BoxScoreManager extends BaseManager
{

	public static BoxScoreData getBoxScoreFromJSON(JsonObject json)
	{
		BoxScoreData data = new BoxScoreData();

		data.team = TeamManager.getTeamFromID(json.get("team").getAsJsonObject().get("id").getAsInt());

		JsonObject teamStatJson = json.get("teamStats").getAsJsonObject().get("teamSkaterStats").getAsJsonObject();
		TeamStatData teamStat = new TeamStatData();
		teamStat.goals = getIntSafe(teamStatJson, "goals");
		teamStat.penaltyMinutes = getIntSafe(teamStatJson, "pim");
		teamStat.shots = getIntSafe(teamStatJson, "shots");
		teamStat.powerPlayPercentage = getStringSafe(teamStatJson, "powerPlayPercentage");
		teamStat.powerPlayGoals = getIntSafe(teamStatJson, "powerPlayGoals");
		teamStat.powerPlayAttempts = getIntSafe(teamStatJson, "powerPlayOpportunities");
		teamStat.faceoffPercentage = getStringSafe(teamStatJson, "faceOffWinPercentage");
		teamStat.blockedShots = getIntSafe(teamStatJson, "blocked");
		teamStat.takeaways = getIntSafe(teamStatJson, "takeaways");
		teamStat.giveaways = getIntSafe(teamStatJson, "giveaways");
		teamStat.hits = getIntSafe(teamStatJson, "hits");
		data.teamGameStats = teamStat;

		Map<Integer, PlayerStatData> statsMap = new HashMap<>();
		List<PlayerStatData> playerStats = new ArrayList<>();
		List<GoalieStatData> goaliesStats = new ArrayList<>();
		List<SkaterStatData> skaterStats = new ArrayList<>();
		List<PlayerStatData> onIcePlayers = new ArrayList<>();
		List<PlayerStatData> scratches = new ArrayList<>();
		List<PlayerStatData> penaltyBox = new ArrayList<>();

		for(Entry<String, JsonElement> statElem : json.get("players").getAsJsonObject().entrySet())
		{
			PlayerStatData playerstat = PlayerStatManager.getPlayerSataFromJSON(statElem.getValue().getAsJsonObject());
			statsMap.put(playerstat.playerID, playerstat);
		}

		playerStats.addAll(statsMap.values());
		for(JsonElement elem : json.get("goalies").getAsJsonArray())
			goaliesStats.add((GoalieStatData) statsMap.get(elem.getAsInt()));
		for(JsonElement elem : json.get("skaters").getAsJsonArray())
			skaterStats.add((SkaterStatData) statsMap.get(elem.getAsInt()));
		for(JsonElement elem : json.get("onIce").getAsJsonArray())
			onIcePlayers.add(statsMap.get(elem.getAsInt()));
		for(JsonElement elem : json.get("scratches").getAsJsonArray())
			scratches.add(statsMap.get(elem.getAsInt()));
		for(JsonElement elem : json.get("penaltyBox").getAsJsonArray())
			penaltyBox.add(statsMap.get(elem.getAsInt()));

		data.playerStats = playerStats;
		data.goaliesStats = goaliesStats;
		data.skaterStats = skaterStats;
		data.onIcePlayers = onIcePlayers;
		data.scratches = scratches;
		data.penaltyBox = penaltyBox;

		// TODO get coaches

		return data;
	}
}

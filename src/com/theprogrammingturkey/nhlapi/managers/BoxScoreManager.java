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

	public static BoxScoreData getBoxScoreFromJSON(JsonObject boxScore, JsonObject lineScore)
	{
		BoxScoreData data = new BoxScoreData();

		data.team = TeamManager.getTeamFromID(boxScore.get("team").getAsJsonObject().get("id").getAsInt());

		JsonObject teamBoxJson = boxScore.get("teamStats").getAsJsonObject().get("teamSkaterStats").getAsJsonObject();
		TeamStatData teamStat = new TeamStatData();
		teamStat.goals = getIntSafe(lineScore, "goals");
		teamStat.penaltyMinutes = getIntSafe(teamBoxJson, "pim");
		teamStat.shots = getIntSafe(teamBoxJson, "shots");
		teamStat.powerPlayPercentage = getStringSafe(teamBoxJson, "powerPlayPercentage");
		teamStat.powerPlayGoals = getIntSafe(teamBoxJson, "powerPlayGoals");
		teamStat.powerPlayAttempts = getIntSafe(teamBoxJson, "powerPlayOpportunities");
		teamStat.faceoffPercentage = getStringSafe(teamBoxJson, "faceOffWinPercentage");
		teamStat.blockedShots = getIntSafe(teamBoxJson, "blocked");
		teamStat.takeaways = getIntSafe(teamBoxJson, "takeaways");
		teamStat.giveaways = getIntSafe(teamBoxJson, "giveaways");
		teamStat.hits = getIntSafe(teamBoxJson, "hits");
		teamStat.goaliePulled = getBooleanSafe(lineScore, "goaliePulled");
		teamStat.powerPlay = getBooleanSafe(lineScore, "powerPlay");
		teamStat.numSkaters = getIntSafe(lineScore, "numSkaters");
		data.teamGameStats = teamStat;

		Map<Integer, PlayerStatData> statsMap = new HashMap<>();
		List<PlayerStatData> playerStats = new ArrayList<>();
		List<GoalieStatData> goaliesStats = new ArrayList<>();
		List<SkaterStatData> skaterStats = new ArrayList<>();
		List<PlayerStatData> onIcePlayers = new ArrayList<>();
		List<PlayerStatData> scratches = new ArrayList<>();
		List<PlayerStatData> penaltyBox = new ArrayList<>();

		for(Entry<String, JsonElement> statElem : boxScore.get("players").getAsJsonObject().entrySet())
		{
			PlayerStatData playerstat = PlayerStatManager.getPlayerSataFromJSON(statElem.getValue().getAsJsonObject());
			statsMap.put(playerstat.playerID, playerstat);
		}

		playerStats.addAll(statsMap.values());
		for(JsonElement elem : boxScore.get("goalies").getAsJsonArray())
			goaliesStats.add((GoalieStatData) statsMap.get(elem.getAsInt()));
		for(JsonElement elem : boxScore.get("skaters").getAsJsonArray())
		{
			SkaterStatData skaterdata;
			if(statsMap.get(elem.getAsInt()) instanceof GoalieStatData)
			{
				//TODO: Seems that goalies also get player stats and will be put into the skaters section causing a cast exception
				//TODO: IDK?????
				continue;
			}
			else
			{
				skaterdata = (SkaterStatData) statsMap.get(elem.getAsInt());
			}
			skaterStats.add(skaterdata);
		}
		for(JsonElement elem : boxScore.get("onIce").getAsJsonArray())
			onIcePlayers.add(statsMap.get(elem.getAsInt()));
		for(JsonElement elem : boxScore.get("scratches").getAsJsonArray())
			scratches.add(statsMap.get(elem.getAsInt()));
		for(JsonElement elem : boxScore.get("penaltyBox").getAsJsonArray())
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

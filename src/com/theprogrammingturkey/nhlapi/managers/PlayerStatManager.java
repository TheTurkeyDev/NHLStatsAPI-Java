package com.theprogrammingturkey.nhlapi.managers;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.GoalieStatData;
import com.theprogrammingturkey.nhlapi.data.PlayerStatData;
import com.theprogrammingturkey.nhlapi.data.SkaterStatData;

public class PlayerStatManager extends BaseManager
{

	public static PlayerStatData getPlayerSataFromJSON(JsonObject json)
	{
		PlayerStatData playerStat;
		for(Entry<String, JsonElement> statTypeElem : json.get("stats").getAsJsonObject().entrySet())
		{
			JsonObject statTypeObj = statTypeElem.getValue().getAsJsonObject();
			if(statTypeElem.getKey().equalsIgnoreCase("skaterStats"))
			{
				SkaterStatData skaterStat = new SkaterStatData();
				skaterStat.shots = getIntSafe(statTypeObj, "shots");
				skaterStat.hits = getIntSafe(statTypeObj, "hits");
				skaterStat.powerPlayGoals = getIntSafe(statTypeObj, "powerPlayGoals");
				skaterStat.powerPlayAssists = getIntSafe(statTypeObj, "powerPlayAssists");
				skaterStat.faceoffWins = getIntSafe(statTypeObj, "faceOffWins");
				skaterStat.faceoffsTaken = getIntSafe(statTypeObj, "faceoffTaken");
				skaterStat.takeaways = getIntSafe(statTypeObj, "takeaways");
				skaterStat.giveaways = getIntSafe(statTypeObj, "giveaways");
				skaterStat.shortHandedGoals = getIntSafe(statTypeObj, "shortHandedGoals");
				skaterStat.shortHandedAssists = getIntSafe(statTypeObj, "shortHandedAssists");
				skaterStat.blockedShots = getIntSafe(statTypeObj, "blocked");
				skaterStat.plusMinus = getIntSafe(statTypeObj, "plusMinus");
				skaterStat.evenTimeOnIce = getStringSafe(statTypeObj, "evenTimeOnIce");
				skaterStat.powerPlayTimeOnIce = getStringSafe(statTypeObj, "powerPlayTimeOnIce");
				skaterStat.shortHandedTimeOnIce = getStringSafe(statTypeObj, "shortHandedTimeOnIce");

				skaterStat.penaltyMinutes = getIntSafe(statTypeObj, "penaltyMinutes");
				playerStat = skaterStat;

			}
			else if(statTypeElem.getKey().equalsIgnoreCase("goalieStats"))
			{
				GoalieStatData goalieStat = new GoalieStatData();

				goalieStat.shots = getIntSafe(statTypeObj, "shots");
				goalieStat.saves = getIntSafe(statTypeObj, "saves");
				goalieStat.powerPlaySaves = getIntSafe(statTypeObj, "powerPlaySaves");
				goalieStat.shortHandedSaves = getIntSafe(statTypeObj, "shortHandedSaves");
				goalieStat.evenSaves = getIntSafe(statTypeObj, "evenSaves");
				goalieStat.shortHandedShotsAgainst = getIntSafe(statTypeObj, "shortHandedShotsAgainst");
				goalieStat.evenShotsAgainst = getIntSafe(statTypeObj, "evenShotsAgainst");
				goalieStat.powerPlayShotsAgainst = getIntSafe(statTypeObj, "powerPlayShotsAgainst");
				goalieStat.decision = getStringSafe(statTypeObj, "decision");
				goalieStat.savePercentage = getDoubleSafe(statTypeObj, "savePercentage");
				goalieStat.powerPlaySavePercentage = getDoubleSafe(statTypeObj, "powerPlaySavePercentage");
				goalieStat.evenStrengthSavePercentage = getDoubleSafe(statTypeObj, "evenStrengthSavePercentage");

				goalieStat.penaltyMinutes = getIntSafe(statTypeObj, "pim");
				playerStat = goalieStat;
			}
			else
			{
				playerStat = new PlayerStatData();
			}

			playerStat.playerID = json.get("person").getAsJsonObject().get("id").getAsInt();
			playerStat.jerseyNumber = getIntSafe(json, "jerseyNumber");
			playerStat.position = PlayerManager.getPositionDataFromJSON(json.get("position").getAsJsonObject());
			playerStat.timeOnIce = getStringSafe(statTypeObj, "timeOnIce");
			playerStat.goals = getIntSafe(statTypeObj, "goals");
			playerStat.assists = getIntSafe(statTypeObj, "assists");
			playerStat.penaltyMinutes = getIntSafe(statTypeObj, "pim");

			return playerStat;
		}
		return new PlayerStatData();
	}
}

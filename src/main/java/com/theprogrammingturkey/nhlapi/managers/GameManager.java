package com.theprogrammingturkey.nhlapi.managers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.NHLAPI;
import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.DecisionsData;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PeopleData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.data.PlayerData;
import com.theprogrammingturkey.nhlapi.util.WebHelper;

public class GameManager extends BaseManager
{
	private static boolean playSaving = true;
	private static boolean playerSaving = true;

	public static List<GameData> getGames(SerchCriteria criteria)
	{
		NHLAPI.log("Getting games from API...");
		JsonObject json;
		try
		{
			NHLAPI.logDebug("URL: " + "https://statsapi.web.nhl.com/api/v1/schedule" + criteria.toURLParams());
			json = WebHelper.makeRequest("https://statsapi.web.nhl.com/api/v1/schedule" + criteria.toURLParams());
		} catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<GameData>();
		}
		List<GameData> games = new ArrayList<GameData>();

		int index = 0;
		int lastPercent = 0;
		int total = json.get("totalGames").getAsInt();
		NHLAPI.log("Parsing " + total + " games from API...");
		NHLAPI.log("|0%              100%|");
		NHLAPI.logInline("[");
		for(JsonElement datesElement : json.getAsJsonArray("dates"))
		{
			for(JsonElement gamesElement : datesElement.getAsJsonObject().getAsJsonArray("games"))
			{
				index++;
				String gameID = gamesElement.getAsJsonObject().get("gamePk").getAsString();
				if(((double)index / total) * 100 > lastPercent)
				{
					NHLAPI.logInline("=");
					lastPercent += 5;
					if(lastPercent == 100)
						NHLAPI.log("]");
				}
				
				try
				{
					games.add(getGameDataFromJson(gamesElement.getAsJsonObject(), WebHelper.makeRequest("http://statsapi.web.nhl.com/api/v1/game/" + gameID + "/feed/live")));
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return games;
	}

	private static GameData getGameDataFromJson(JsonObject scheduleJson, JsonObject json)
	{
		GameData data = new GameData();

		JsonObject gameData = json.getAsJsonObject("gameData");
		JsonObject game = gameData.getAsJsonObject("game");
		data.gameID = getIntSafe(game, "pk");
		data.season = getStringSafe(game, "season");
		data.gameType = getStringSafe(game, "type");

		JsonObject date = gameData.getAsJsonObject("datetime");
		try
		{
			String dateString = getStringSafe(date, "dateTime");
			data.date = NHLAPI.DATE_TIME_FORMAT.parse(dateString);
			data.endDate = NHLAPI.DATE_TIME_FORMAT.parse(getStringSafe(date, "endDateTime", dateString));
		} catch(ParseException e)
		{
			e.printStackTrace();
		}

		JsonObject status = gameData.getAsJsonObject("status");
		data.gameState = getStringSafe(status, "detailedState");
		data.startTimeTBD = getBooleanSafe(status, "startTimeTBD");

		JsonObject teams = gameData.getAsJsonObject("teams");
		data.homeTeam = TeamManager.getTeamDataFromJSON(teams.getAsJsonObject("home"));
		data.homeRecord = TeamRecordManager.getTeamRecordFromJSON(scheduleJson.getAsJsonObject("teams").getAsJsonObject("home").getAsJsonObject("leagueRecord"));
		data.awayTeam = TeamManager.getTeamDataFromJSON(teams.getAsJsonObject("away"));
		data.awayRecord = TeamRecordManager.getTeamRecordFromJSON(scheduleJson.getAsJsonObject("teams").getAsJsonObject("away").getAsJsonObject("leagueRecord"));

		if(playerSaving)
		{
			JsonObject playersObj = gameData.getAsJsonObject("players");
			List<PlayerData> players = new ArrayList<>();
			for(Entry<String, JsonElement> player : playersObj.entrySet())
			{
				PlayerManager.getPlayerDataFromJSON(player.getValue().getAsJsonObject());
			}
			data.players = players;
		}

		data.gameVenue = getStringSafe(getJsonObjectSafe(gameData, "venue"), "name");

		JsonObject liveData = json.getAsJsonObject("liveData");

		JsonObject plays = liveData.getAsJsonObject("plays");

		if(playSaving)
		{
			List<PlayData> playsList = new ArrayList<>();
			for(JsonElement playElem : plays.getAsJsonArray("allPlays"))
			{
				playsList.add(PlayManager.getPlayDataFromJSON(playElem.getAsJsonObject()));
			}
			data.plays = playsList;

			List<PlayData> scoringPlays = new ArrayList<>();
			for(JsonElement scoringElem : plays.getAsJsonArray("scoringPlays"))
			{
				scoringPlays.add(playsList.get(scoringElem.getAsInt()));
			}
			data.scoringPlays = scoringPlays;

			List<PlayData> penaltyPlays = new ArrayList<>();
			for(JsonElement scoringElem : plays.getAsJsonArray("penaltyPlays"))
			{
				penaltyPlays.add(playsList.get(scoringElem.getAsInt()));
			}
			data.penaltyPlays = penaltyPlays;

			JsonArray periodPlaysArray = plays.getAsJsonArray("playsByPeriod");
			List<List<PlayData>> playsByPeriod = new ArrayList<>();
			for(JsonElement periodElem : periodPlaysArray)
			{
				List<PlayData> periodPlays = new ArrayList<>();
				for(JsonElement playIndexes : periodElem.getAsJsonObject().getAsJsonArray("plays"))
				{
					periodPlays.add(playsList.get(playIndexes.getAsInt()));
				}
				playsByPeriod.add(periodPlays);
			}
			data.playsByPeriod = playsByPeriod;

			data.currentPlay = PlayManager.getPlayDataFromJSON(getJsonObjectSafe(plays, "currentPlay", null));
		}

		JsonObject lineScore = liveData.getAsJsonObject("linescore");
		data.currentPeriod = getIntSafe(lineScore, "currentPeriod");
		data.currentPeriodOrdinal = getStringSafe(lineScore, "currentPeriodOrdinal");
		data.currentPeriodTimeRemaining = getStringSafe(lineScore, "currentPeriodTimeRemaining");
		data.powerPlayStrength = getStringSafe(lineScore, "powerPlayStrength");
		data.hasShootout = getBooleanSafe(lineScore, "hasShootout");

		JsonObject teamBoxScores = liveData.getAsJsonObject("boxscore").getAsJsonObject("teams");
		JsonObject teamLineScores = lineScore.getAsJsonObject("teams");

		data.homeBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.getAsJsonObject("home"), teamLineScores.getAsJsonObject("home"));
		data.awayBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.getAsJsonObject("away"), teamLineScores.getAsJsonObject("away"));

		data.officials = new ArrayList<PeopleData>();
		JsonArray officialsJsonArray = liveData.getAsJsonObject("boxscore").getAsJsonArray("officials");
		for(JsonElement officialJson : officialsJsonArray)
		{
			PeopleData official = new PeopleData();
			JsonObject temp = officialJson.getAsJsonObject();
			official.positionTitle = getStringSafe(temp, "officialType");
			temp = getJsonObjectSafe(temp, "official");
			official.id = getIntSafe(temp, "id");
			official.name = getStringSafe(temp, "fullName");
		}
		
		if(playerSaving)
		{
			JsonObject decisionJson = liveData.getAsJsonObject("decisions");
			DecisionsData decisions = new DecisionsData();
			decisions.winner = PlayerManager.getPlayerDataFromJSON(getJsonObjectSafe(decisionJson, "winner"));
			decisions.loser = PlayerManager.getPlayerDataFromJSON(getJsonObjectSafe(decisionJson, "loser"));
			decisions.firstStar = PlayerManager.getPlayerDataFromJSON(getJsonObjectSafe(decisionJson, "firstStar"));
			decisions.secondStar = PlayerManager.getPlayerDataFromJSON(getJsonObjectSafe(decisionJson, "secondStar"));
			decisions.thirdStar = PlayerManager.getPlayerDataFromJSON(getJsonObjectSafe(decisionJson ,"thirdStar"));
	
			data.decisions = decisions;
		}
		
		return data;
	}

	public static void disablePlaySaving()
	{
		playSaving = false;
	}

	public static void enablePlaySaving()
	{
		playSaving = true;
	}

	public static void disablePlayerSaving()
	{
		playerSaving = false;
	}

	public static void enablePlayerSaving()
	{
		playerSaving = true;
	}
}

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
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.data.PlayerData;
import com.theprogrammingturkey.nhlapi.util.WebHelper;

public class GameManager extends BaseManager
{
	private static boolean playSaving = true;
	private static boolean playerStatSaving = true;

	public static List<GameData> getGames(SerchCriteria criteria)
	{
		NHLAPI.log("Getting games from api...");
		JsonObject json;
		try
		{
			NHLAPI.log("URL: " + "https://statsapi.web.nhl.com/api/v1/schedule" + criteria.toURLParams());
			json = WebHelper.makeRequest("https://statsapi.web.nhl.com/api/v1/schedule" + criteria.toURLParams());
		} catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<GameData>();
		}
		NHLAPI.log("Parsing data from API...");
		List<GameData> games = new ArrayList<GameData>();

		int index = 0;
		int total = json.get("totalGames").getAsInt();
		for(JsonElement datesElement : json.get("dates").getAsJsonArray())
		{
			for(JsonElement gamesElement : datesElement.getAsJsonObject().get("games").getAsJsonArray())
			{
				index++;
				String gameID = gamesElement.getAsJsonObject().get("gamePk").getAsString();
				NHLAPI.log("Getting game data " + index + "/" + total);
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

		JsonObject gameData = json.get("gameData").getAsJsonObject();
		JsonObject game = gameData.get("game").getAsJsonObject();
		data.gameID = getIntSafe(game, "pk");
		data.season = getStringSafe(game, "season");
		data.gameType = getStringSafe(game, "type");

		JsonObject date = gameData.get("datetime").getAsJsonObject();
		try
		{
			String dateString = getStringSafe(date, "dateTime");
			data.date = NHLAPI.DATE_TIME_FORMAT.parse(dateString);
			data.endDate = NHLAPI.DATE_TIME_FORMAT.parse(getStringSafe(date, "endDateTime", dateString));
		} catch(ParseException e)
		{
			e.printStackTrace();
		}

		JsonObject status = gameData.get("status").getAsJsonObject();
		data.gameState = getStringSafe(status, "detailedState");
		data.startTimeTBD = getBooleanSafe(status, "startTimeTBD");

		JsonObject teams = gameData.get("teams").getAsJsonObject();
		data.homeTeam = TeamManager.getTeamDataFromJSON(teams.get("home").getAsJsonObject());
		data.homeRecord = TeamRecordManager.getTeamRecordFromJSON(scheduleJson.get("teams").getAsJsonObject().get("home").getAsJsonObject().get("leagueRecord").getAsJsonObject());
		data.awayTeam = TeamManager.getTeamDataFromJSON(teams.get("away").getAsJsonObject());
		data.awayRecord = TeamRecordManager.getTeamRecordFromJSON(scheduleJson.get("teams").getAsJsonObject().get("away").getAsJsonObject().get("leagueRecord").getAsJsonObject());

		if(playerStatSaving)
		{
			JsonObject playersObj = gameData.get("players").getAsJsonObject();
			List<PlayerData> players = new ArrayList<>();
			for(Entry<String, JsonElement> player : playersObj.entrySet())
			{
				PlayerManager.getPlayerDataFromJSON(player.getValue().getAsJsonObject());
			}
			data.players = players;
		}

		data.gameVenue = getStringSafe(getJsonObjectSafe(gameData, "venue"), "name");

		JsonObject liveData = json.get("liveData").getAsJsonObject();

		JsonObject plays = liveData.get("plays").getAsJsonObject();

		if(playSaving)
		{
			List<PlayData> playsList = new ArrayList<>();
			for(JsonElement playElem : plays.get("allPlays").getAsJsonArray())
			{
				playsList.add(PlayManager.getPlayDataFromJSON(playElem.getAsJsonObject()));
			}
			data.plays = playsList;

			List<PlayData> scoringPlays = new ArrayList<>();
			for(JsonElement scoringElem : plays.get("scoringPlays").getAsJsonArray())
			{
				scoringPlays.add(playsList.get(scoringElem.getAsInt()));
			}
			data.scoringPlays = scoringPlays;

			List<PlayData> penaltyPlays = new ArrayList<>();
			for(JsonElement scoringElem : plays.get("penaltyPlays").getAsJsonArray())
			{
				penaltyPlays.add(playsList.get(scoringElem.getAsInt()));
			}
			data.penaltyPlays = penaltyPlays;

			JsonArray periodPlaysArray = plays.get("playsByPeriod").getAsJsonArray();
			List<List<PlayData>> playsByPeriod = new ArrayList<>();
			for(JsonElement periodElem : periodPlaysArray)
			{
				List<PlayData> periodPlays = new ArrayList<>();
				for(JsonElement playIndexes : periodElem.getAsJsonObject().get("plays").getAsJsonArray())
				{
					periodPlays.add(playsList.get(playIndexes.getAsInt()));
				}
				playsByPeriod.add(periodPlays);
			}
			data.playsByPeriod = playsByPeriod;

			data.currentPlay = PlayManager.getPlayDataFromJSON(getJsonObjectSafe(plays, "currentPlay", null));
		}

		JsonObject lineScore = liveData.get("linescore").getAsJsonObject();
		data.currentPeriod = getIntSafe(lineScore, "currentPeriod");
		data.currentPeriodOrdinal = getStringSafe(lineScore, "currentPeriodOrdinal");
		data.currentPeriodTimeRemaining = getStringSafe(lineScore, "currentPeriodTimeRemaining");
		data.powerPlayStrength = getStringSafe(lineScore, "powerPlayStrength");
		data.hasShootout = getBooleanSafe(lineScore, "hasShootout");

		JsonObject teamBoxScores = liveData.get("boxscore").getAsJsonObject().get("teams").getAsJsonObject();
		JsonObject teamLineScores = lineScore.get("teams").getAsJsonObject();

		data.homeBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.get("home").getAsJsonObject(), teamLineScores.get("home").getAsJsonObject());
		data.awayBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.get("away").getAsJsonObject(), teamLineScores.get("away").getAsJsonObject());

		// TODO Officials
		// TODO Decisions

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

	public static void disablePlayerStatSaving()
	{
		playerStatSaving = false;
	}

	public static void enablePlayerStatSaving()
	{
		playerStatSaving = true;
	}
}

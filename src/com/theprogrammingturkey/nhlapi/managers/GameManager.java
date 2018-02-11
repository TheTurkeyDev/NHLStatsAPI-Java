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

	public static List<GameData> getGames(SerchCriteria criteria)
	{
		JsonObject json;
		try
		{
			json = WebHelper.makeRequest("https://statsapi.web.nhl.com/api/v1/schedule" + criteria.toURLParams());
		} catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<GameData>();
		}
		List<GameData> games = new ArrayList<GameData>();

		for(JsonElement datesElement : json.get("dates").getAsJsonArray())
		{
			for(JsonElement gamesElement : datesElement.getAsJsonObject().get("games").getAsJsonArray())
			{
				String gameID = gamesElement.getAsJsonObject().get("gamePk").getAsString();
				try
				{
					games.add(getGameDataFromJson(WebHelper.makeRequest("http://statsapi.web.nhl.com/api/v1/game/" + gameID + "/feed/live")));
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return games;
	}

	private static GameData getGameDataFromJson(JsonObject json)
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
		data.awayTeam = TeamManager.getTeamDataFromJSON(teams.get("away").getAsJsonObject());

		JsonObject playersObj = gameData.get("players").getAsJsonObject();
		List<PlayerData> players = new ArrayList<>();
		for(Entry<String, JsonElement> player : playersObj.entrySet())
		{
			PlayerManager.getPlayerDataFromJSON(player.getValue().getAsJsonObject());
		}

		data.players = players;

		data.gameVenue = getStringSafe(gameData.get("venue").getAsJsonObject(), "name");

		JsonObject liveData = json.get("liveData").getAsJsonObject();
		JsonObject plays = liveData.get("plays").getAsJsonObject();

		List<PlayData> playsList = new ArrayList<>();
		for(JsonElement playElem : plays.get("allPlays").getAsJsonArray())
		{
			playsList.add(PlayManager.getPlayDataFromJSON(playElem.getAsJsonObject()));
		}
		data.plays = playsList;

		JsonArray scoringPlaysArray = plays.get("scoringPlays").getAsJsonArray();
		List<PlayData> scoringPlays = new ArrayList<>();
		for(int i = 0; i < scoringPlaysArray.size(); i++)
		{
			scoringPlays.add(playsList.get(scoringPlaysArray.get(i).getAsInt()));
		}
		data.scoringPlays = scoringPlays;

		JsonArray penaltyPlaysArray = plays.get("penaltyPlays").getAsJsonArray();
		List<PlayData> penaltyPlays = new ArrayList<>();
		for(int i = 0; i < penaltyPlaysArray.size(); i++)
		{
			penaltyPlays.add(playsList.get(penaltyPlaysArray.get(i).getAsInt()));
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

		// TODO Line Score

		JsonObject teamBoxScores = liveData.get("boxscore").getAsJsonObject().get("teams").getAsJsonObject();
		data.homeBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.get("home").getAsJsonObject());
		data.awayBoxScore = BoxScoreManager.getBoxScoreFromJSON(teamBoxScores.get("away").getAsJsonObject());

		// TODO Officials
		// TODO Decisions

		return data;
	}
}

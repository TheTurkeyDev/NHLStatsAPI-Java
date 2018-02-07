package com.theprogrammingturkey.nhlapi.managers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.NHLAPI;
import com.theprogrammingturkey.nhlapi.criteria.GameSerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.data.PlayerData;
import com.theprogrammingturkey.nhlapi.util.WebHelper;

public class GameManager extends BaseManager
{

	public static List<GameData> getGames(GameSerchCriteria criteria)
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
			data.date = NHLAPI.DATE_TIME_FORMAT.parse(getStringSafe(date, "dateTime"));
			data.endDate = NHLAPI.DATE_TIME_FORMAT.parse(getStringSafe(date, "endDateTime"));
		} catch(ParseException e)
		{
			e.printStackTrace();
		}

		JsonObject status = gameData.get("status").getAsJsonObject();
		data.gameState = getStringSafe(status, "detailedState");
		data.startTimeTBD = getBooleanSafe(status, "startTimeTBD");

		JsonObject teams = gameData.get("teams").getAsJsonObject();
		data.homeTeam = TeamManager.getTeamDataFromJSON(teams.get("home").getAsJsonObject());
		data.homeTeam = TeamManager.getTeamDataFromJSON(teams.get("away").getAsJsonObject());

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
		int[] scoringPlays = new int[scoringPlaysArray.size()];
		for(int i = 0; i < scoringPlaysArray.size(); i++)
		{
			scoringPlays[i] = scoringPlaysArray.get(i).getAsInt();
		}
		data.scoringPlays = scoringPlays;

		JsonArray penaltyPlaysArray = plays.get("penaltyPlays").getAsJsonArray();
		int[] penaltyPlays = new int[penaltyPlaysArray.size()];
		for(int i = 0; i < penaltyPlaysArray.size(); i++)
		{
			penaltyPlays[i] = penaltyPlaysArray.get(i).getAsInt();
		}
		data.penaltyPlays = penaltyPlays;

		// TODO: Plays by period

		data.currentPlay = PlayManager.getPlayDataFromJSON(plays.get("currentPlay").getAsJsonObject());

		//TODO Line Score
		//TODO Box Score
		//TODO Decisions
		
		return data;
	}
}

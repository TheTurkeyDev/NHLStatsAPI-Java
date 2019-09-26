package com.theprogrammingturkey.nhlapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class SeasonOpenerTest
{
	public static void main(String[] args)
	{
		testSeasonOpenerExample();
	}

	public static void testSeasonOpenerExample()
	{
		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2000-10-01");
		criteria.setEndDate("2019-05-01");
		criteria.setTeamId("29");
		criteria.setGameType("R");
		System.out.println("Collecting game data...");
		GameManager.disablePlayerSaving();
		GameManager.disablePlaySaving();
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		Map<String, GameData> firstGames = new HashMap<>();
		Map<String, GameData> homeFirstGames = new HashMap<>();
		Map<String, GameData> awayFirstGames = new HashMap<>();

		for(GameData game : games)
		{
			if(!firstGames.containsKey(game.season) || firstGames.get(game.season).date.after(game.date))
				firstGames.put(game.season, game);

			if(game.homeTeam.id == 29)
			{
				if(!homeFirstGames.containsKey(game.season) || homeFirstGames.get(game.season).date.after(game.date))
					homeFirstGames.put(game.season, game);
			}
			else
			{
				if(!awayFirstGames.containsKey(game.season) || awayFirstGames.get(game.season).date.after(game.date))
					awayFirstGames.put(game.season, game);
			}
		}

		Map<String, Record> firstGameVsRecord = new HashMap<>();
		Map<String, Record> homeGameVsRecord = new HashMap<>();
		Map<String, Record> awayGameVsRecord = new HashMap<>();
		Record firstGameRecord = new Record();
		Record homeGameRecord = new Record();
		Record awayGameRecord = new Record();

		String[] sortedSeasons = firstGames.keySet().toArray(new String[0]);
		Arrays.sort(sortedSeasons);
		for(String season : Arrays.asList(sortedSeasons))
		{
			GameData homeGame = homeFirstGames.get(season);
			GameData awayGame = awayFirstGames.get(season);
			System.out.println(season.substring(0, 4) + "-" + season.substring(4) + ":");
			System.out.println("\tHome: " + homeGame.homeTeam.name + "(" + homeGame.getHomeGoals() + ") vs " + homeGame.awayTeam.name + "(" + homeGame.getAwayGoals() + ")");
			System.out.println("\tAway: " + awayGame.homeTeam.name + "(" + homeGame.getHomeGoals() + ") vs " + awayGame.awayTeam.name + "(" + homeGame.getAwayGoals() + ")");

			if(homeGame.date.before(awayGame.date))
			{

				Record record = firstGameVsRecord.computeIfAbsent(homeGame.awayTeam.name, key -> new Record());
				if(homeGame.homeTeamWon())
				{
					record.wins++;
					firstGameRecord.wins++;
				}
				else
				{
					record.losses++;
					firstGameRecord.losses++;
				}
			}
			else
			{
				Record record = firstGameVsRecord.computeIfAbsent(awayGame.homeTeam.name, key -> new Record());
				if(homeGame.homeTeamWon())
				{
					record.losses++;
					firstGameRecord.losses++;
				}
				else
				{
					record.wins++;
					firstGameRecord.wins++;
				}
			}

			Record vsRecord = homeGameVsRecord.computeIfAbsent(homeGame.awayTeam.name, key -> new Record());
			if(homeGame.homeTeamWon())
			{
				vsRecord.wins++;
				homeGameRecord.wins++;
			}
			else
			{
				vsRecord.losses++;
				homeGameRecord.losses++;
			}
			vsRecord = awayGameVsRecord.computeIfAbsent(awayGame.homeTeam.name, key -> new Record());
			if(homeGame.homeTeamWon())
			{
				vsRecord.losses++;
				awayGameRecord.losses++;
			}
			else
			{
				vsRecord.wins++;
				awayGameRecord.wins++;
			}
		}

		System.out.println("==============Stats=================");
		System.out.println("Season Opener Record" + firstGameRecord.wins + "-" + firstGameRecord.losses);
		System.out.println("Home Opener Record" + homeGameRecord.wins + "-" + homeGameRecord.losses);
		System.out.println("Away Opener Record" + awayGameRecord.wins + "-" + awayGameRecord.losses);

		System.out.println("");
		System.out.println("Season Opener Record vs Opponents:");
		for(String team : firstGameVsRecord.keySet())
		{
			Record record = firstGameVsRecord.get(team);
			System.out.println("vs " + team + ": " + record.wins + "-" + record.losses);
		}

		System.out.println("");
		System.out.println("Home Opener Record vs Opponents:");
		for(String team : homeGameVsRecord.keySet())
		{
			Record record = homeGameVsRecord.get(team);
			System.out.println("vs " + team + ": " + record.wins + "-" + record.losses);
		}

		System.out.println("");
		System.out.println("Away Opener Record vs Opponents:");
		for(String team : awayGameVsRecord.keySet())
		{
			Record record = awayGameVsRecord.get(team);
			System.out.println("vs" + team + ": " + record.wins + "-" + record.losses);
		}

	}

	public static class Record
	{
		public int wins = 0;
		public int losses = 0;
	}
}

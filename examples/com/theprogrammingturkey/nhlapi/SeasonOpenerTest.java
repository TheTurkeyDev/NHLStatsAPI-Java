package com.theprogrammingturkey.nhlapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		int gfFirstGame = 0;
		int gaFirstGame = 0;
		int gfHomeGame = 0;
		int gaHomeGame = 0;
		int gfAwayGame = 0;
		int gaAwayGame = 0;

		String[] sortedSeasons = firstGames.keySet().toArray(new String[0]);
		Arrays.sort(sortedSeasons);
		for(String season : Arrays.asList(sortedSeasons))
		{
			GameData homeGame = homeFirstGames.get(season);
			GameData awayGame = awayFirstGames.get(season);
			System.out.println(season.substring(0, 4) + "-" + season.substring(4) + ":");

			if(homeGame.date.before(awayGame.date))
			{
				System.out.println("\tHome(Season Opener): " + homeGame.homeTeam.name + "(" + homeGame.getHomeGoals() + ") vs " + homeGame.awayTeam.name + "(" + homeGame.getAwayGoals() + ")");
				System.out.println("\tAway: " + awayGame.homeTeam.name + "(" + homeGame.getHomeGoals() + ") vs " + awayGame.awayTeam.name + "(" + homeGame.getAwayGoals() + ")");
				Record record = firstGameVsRecord.computeIfAbsent(homeGame.awayTeam.name, key -> new Record());
				gfFirstGame += homeGame.homeBoxScore.goals;
				gaFirstGame += homeGame.awayBoxScore.goals;
				if(homeGame.isGameTied())
				{
					record.ties++;
					firstGameRecord.ties++;
				}
				else if(homeGame.homeTeamWon())
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
				System.out.println("\tHome: " + homeGame.homeTeam.name + "(" + homeGame.getHomeGoals() + ") vs " + homeGame.awayTeam.name + "(" + homeGame.getAwayGoals() + ")");
				System.out.println("\tAway(Season Opener): " + awayGame.homeTeam.name + "(" + awayGame.getHomeGoals() + ") vs " + awayGame.awayTeam.name + "(" + awayGame.getAwayGoals() + ")");
				Record record = firstGameVsRecord.computeIfAbsent(awayGame.homeTeam.name, key -> new Record());
				gfFirstGame += awayGame.awayBoxScore.goals;
				gaFirstGame += awayGame.homeBoxScore.goals;
				if(awayGame.isGameTied())
				{
					record.ties++;
					firstGameRecord.ties++;
				}
				else if(awayGame.homeTeamWon())
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
			gfHomeGame += homeGame.homeBoxScore.goals;
			gaHomeGame += homeGame.awayBoxScore.goals;
			if(homeGame.isGameTied())
			{
				vsRecord.ties++;
				homeGameRecord.ties++;
			}
			else if(homeGame.homeTeamWon())
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
			gfAwayGame += awayGame.awayBoxScore.goals;
			gaAwayGame += awayGame.homeBoxScore.goals;
			if(awayGame.isGameTied())
			{
				vsRecord.ties++;
				awayGameRecord.ties++;
			}
			else if(awayGame.homeTeamWon())
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

		int numGames = firstGameRecord.wins + firstGameRecord.losses;

		System.out.println("==============Stats=================");
		System.out.println("Season Openers:");
		System.out.println("\tRecord: " + firstGameRecord.wins + "-" + firstGameRecord.losses + "-" + firstGameRecord.ties);
		System.out.println("\tGF: " + gfFirstGame);
		System.out.println("\tGA: " + gaFirstGame);
		System.out.println("\tGFA: " + ((double) gfFirstGame / numGames));
		System.out.println("\tGAA: " + ((double) gaFirstGame / numGames));

		System.out.println("Home Openers:");
		System.out.println("\tRecord: " + homeGameRecord.wins + "-" + homeGameRecord.losses + "-" + homeGameRecord.ties);
		System.out.println("\tGF: " + gfHomeGame);
		System.out.println("\tGA: " + gaHomeGame);
		System.out.println("\tGFA: " + ((double) gfHomeGame / numGames));
		System.out.println("\tGAA: " + ((double) gaHomeGame / numGames));

		System.out.println("Away Openers:");
		System.out.println("\tRecord: " + awayGameRecord.wins + "-" + awayGameRecord.losses + "-" + awayGameRecord.ties);
		System.out.println("\tGF: " + gfAwayGame);
		System.out.println("\tGA: " + gaAwayGame);
		System.out.println("\tGFA: " + ((double) gfAwayGame / numGames));
		System.out.println("\tGAA: " + ((double) gaAwayGame / numGames));
		System.out.println("");
		System.out.println("Season Opener Record vs Opponents:");
		List<String> keys = new ArrayList<>();
		keys.addAll(firstGameVsRecord.keySet());
		Collections.sort(keys);
		for(String team : keys)
		{
			Record record = firstGameVsRecord.get(team);
			System.out.println("vs " + team + ": " + record.wins + "-" + record.losses + "-" + record.ties);
		}

		System.out.println("");
		System.out.println("Home Opener Record vs Opponents:");
		keys = new ArrayList<>();
		keys.addAll(homeGameVsRecord.keySet());
		Collections.sort(keys);
		for(String team : keys)
		{
			Record record = homeGameVsRecord.get(team);
			System.out.println("vs " + team + ": " + record.wins + "-" + record.losses + "-" + firstGameRecord.ties);
		}

		System.out.println("");
		System.out.println("Away Opener Record vs Opponents:");
		keys = new ArrayList<>();
		keys.addAll(awayGameVsRecord.keySet());
		Collections.sort(keys);
		for(String team : keys)
		{
			Record record = awayGameVsRecord.get(team);
			System.out.println("@ " + team + ": " + record.wins + "-" + record.losses + "-" + firstGameRecord.ties);
		}

	}

	public static class Record
	{
		public int wins = 0;
		public int losses = 0;
		public int ties = 0;
	}
}

package com.theprogrammingturkey.nhlapi;

import java.util.List;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class Last20GamesExample
{
	public static void main(String[] args)
	{
		test20GamesExample();
	}

	public static void test20GamesExample()
	{
		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2000-08-02");
		criteria.setEndDate("2018-03-25");
		criteria.setTeamId(29);
		criteria.setGameType("R");
		NHLAPI.DEBUG = true;
		System.out.println("Collecting game data...");
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		int wins = 0;
		int losses = 0;

		for(GameData game : games)
		{
			boolean home = game.homeTeam.id == 29;
			if(home)
			{
				if(game.homeRecord.gamesPlayed >= 62)
				{
					if(game.homeTeamWon())
						wins++;
					else
						losses++;
				}
			}
			else
			{
				if(game.awayRecord.gamesPlayed >= 62)
				{
					if(game.homeTeamWon())
						losses++;
					else
						wins++;
				}
			}
		}

		System.out.println(wins + "-" + losses + " in last 20 games!");
	}
}

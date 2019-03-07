package com.theprogrammingturkey.nhlapi;

import java.util.List;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class ScoreFirstTest
{

	public static void main(String[] args)
	{
		testDownTwoOne();
	}

	public static void testDownTwoOne()
	{
		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2018-10-02");
		criteria.setEndDate("2019-03-06");
		criteria.setTeamId("29");

		System.out.println("Collecting game data...");
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		int winsFirst = 0;
		int lossesFirst = 0;
		int winsNotFirst = 0;
		int lossesNotFirst = 0;

		for(GameData game : games)
		{
			boolean home = game.homeTeam.id == 29;
			if(home)
			{
				if(didHomeTeamScoreFirst(game))
				{
					if(game.homeTeamWon())
						winsFirst++;
					else
						lossesFirst++;
				}
				else
				{
					if(game.homeTeamWon())
						winsNotFirst++;
					else
						lossesNotFirst++;
				}
			}
			else
			{
				if(didHomeTeamScoreFirst(game))
				{
					if(game.homeTeamWon())
						lossesNotFirst++;
					else
						winsNotFirst++;
				}
				else
				{
					if(game.homeTeamWon())
						lossesFirst++;
					else
						winsFirst++;
				}
			}
		}

		System.out.println(winsFirst + "-" + lossesFirst + " when scoring first.");
		System.out.println(winsNotFirst + "-" + lossesNotFirst + " when not scoring first.");
	}
	
	public static boolean didHomeTeamScoreFirst(GameData game) {
		PlayData data = game.scoringPlays.get(0);
		if(data != null)
			return data.homeScore == 1;
		return false;
	}
}

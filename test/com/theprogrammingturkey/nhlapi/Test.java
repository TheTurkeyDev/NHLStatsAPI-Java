package com.theprogrammingturkey.nhlapi;

import java.util.List;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class Test
{

	public static void main(String[] args)
	{
		testDownTwoOne();
	}

	public static void testDownTwoOne()
	{
		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2017-10-02");
		criteria.setEndDate("2018-02-07");
		criteria.setTeamId(29);

		System.out.println("Collecting game data...");
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		int total = 0;
		int wins = 0;
		int losses = 0;

		for(GameData game : games)
		{
			boolean home = game.homeTeam.id == 29;
			for(PlayData play : game.scoringPlays)
			{
				if(play.homeScore > 2 || play.awayScore > 2)
				{
					break;
				}

				System.out.println(play.homeScore + "-" + play.awayScore + "  " + home);
				if(home)
				{
					if(play.homeScore == 1 && play.awayScore == 2)
					{
						total++;
						if(!game.homeTeamWon())
							losses++;
						else
							wins++;
						break;
					}
				}
				else
				{
					if(play.homeScore == 2 && play.awayScore == 1)
					{
						total++;
						if(game.homeTeamWon())
							losses++;
						else
							wins++;
						break;
					}
				}
			}
		}

		System.out.println(total);
		System.out.println(wins + "-" + losses + " when down 2-1.");
	}

	public static void testShotPercentage()
	{

	}
}

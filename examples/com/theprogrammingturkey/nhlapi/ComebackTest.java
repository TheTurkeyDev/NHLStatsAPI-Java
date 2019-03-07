package com.theprogrammingturkey.nhlapi;

import java.util.List;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class ComebackTest
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

				if(home)
				{
					if(play.homeScore == 1 && play.awayScore == 2)
					{
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
						if(game.homeTeamWon())
							losses++;
						else
							wins++;
						break;
					}
				}
			}
		}

		System.out.println(wins + "-" + losses + " when down 2-1.");
	}
}

package com.theprogrammingturkey.nhlapi;

import java.util.List;

import com.theprogrammingturkey.nhlapi.criteria.GameSerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class Test
{

	public static void main(String[] args)
	{
		GameSerchCriteria criteria = new GameSerchCriteria();
		criteria.setStartDate("20178-02-02");
		criteria.setEndDate("2018-02-07");
		criteria.setTeamId(29);

		List<GameData> games = GameManager.getGames(criteria);

		for(GameData game : games)
		{
			boolean home = game.homeTeam.id == 29;
			for(int playIndex : game.scoringPlays)
			{
				PlayData play = game.plays.get(playIndex);
				if((home && play.homeScore == 1 && play.awayScore == 2) || (!home && play.homeScore == 2 && play.awayScore == 1))
				{
					System.out.println("Down 2-1!");
					break;
				}
				else if(play.homeScore > 2 || play.awayScore > 2)
				{
					break;
				}
			}
		}
	}
}
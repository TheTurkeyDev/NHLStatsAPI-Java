package com.theprogrammingturkey.nhlapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.managers.BoxScoreManager;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class GoalsPerGameAllYearsExample
{

	public static void main(String[] args)
	{
		NHLAPI.DEBUG = false;

		Map<String, List<Integer>> goals = new HashMap<>();
		Map<String, List<Integer>> gameGoals = new HashMap<>();

		int year1 = 1915;
		int year2 = 1916;

		while(year2 <= 2018)
		{
			SerchCriteria criteria = new SerchCriteria();
			criteria.setStartDate(year1 + "-08-01");
			criteria.setEndDate(year2 + "-05-01");
			criteria.setGameType("R");

			GameManager.disablePlayerSaving();
			GameManager.disablePlaySaving();
			BoxScoreManager.disablePlayerStatSaving();
			List<GameData> games = GameManager.getGames(criteria);
			
			System.out.println("Retrived " + year1 + "-" + year2 + " season (" + games.size() + " games)");

			for(GameData game : games)
			{
				List<Integer> seasonGoals = goals.get(game.season);
				List<Integer> seasonGameGoals = gameGoals.get(game.season);
				if(seasonGoals == null)
				{
					seasonGoals = new ArrayList<Integer>();
					goals.put(game.season, seasonGoals);
					seasonGameGoals = new ArrayList<Integer>();
					gameGoals.put(game.season, seasonGameGoals);
				}
				seasonGoals.add(game.getHomeGoals());
				seasonGoals.add(game.getAwayGoals());
				seasonGameGoals.add(game.getHomeGoals() + game.getAwayGoals());
			}
			year1++;
			year2++;
		}

		for(String season : goals.keySet())
		{
			Integer[] seasonGoals = goals.get(season).toArray(new Integer[0]);
			Integer[] seasonGameGoals = gameGoals.get(season).toArray(new Integer[0]);
			Arrays.sort(seasonGoals);
			Arrays.sort(seasonGameGoals);
			int q1sg = seasonGoals.length / 4;
			int q2sg = seasonGoals.length / 2;
			int q3sg = q1sg + q2sg;

			int q1sgg = seasonGameGoals.length / 4;
			int q2sgg = seasonGameGoals.length / 2;
			int q3sgg = q1sgg + q2sgg;

			System.out.println("Season: " + season);

			System.out.println("Goals Min: " + seasonGoals[0]);
			System.out.println("Goals Q1: " + seasonGoals[q1sg]);
			System.out.println("Goals Q2: " + seasonGoals[q2sg]);
			System.out.println("Goals Q3: " + seasonGoals[q3sg]);
			System.out.println("Goals Max: " + seasonGoals[seasonGoals.length - 1]);

			System.out.println("Game Goals Min: " + seasonGameGoals[0]);
			System.out.println("Game Goals Q1: " + seasonGameGoals[q1sgg]);
			System.out.println("Game Goals Q2: " + seasonGameGoals[q2sgg]);
			System.out.println("Game Goals Q3: " + seasonGameGoals[q3sgg]);
			System.out.println("Game Goals Max: " + seasonGameGoals[seasonGameGoals.length - 1]);
		}
	}
}

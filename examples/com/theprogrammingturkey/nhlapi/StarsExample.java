package com.theprogrammingturkey.nhlapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayerData;
import com.theprogrammingturkey.nhlapi.data.TeamData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;
import com.theprogrammingturkey.nhlapi.managers.PlayerManager;

public class StarsExample {

	private static Map<Integer, Integer> stars = new HashMap<>();
	private static Map<Integer, Integer> starPoints = new HashMap<>();
	private static Map<Integer, Integer> firstStars = new HashMap<>();

	public static void main(String[] args) {
		NHLAPI.DEBUG = true;
		List<GameData> games = new ArrayList<>();
		
		int year1 = 2000;
		int year2 = 2001;
		SerchCriteria criteria;

		while(year2 <= 2018)
		{
			criteria = new SerchCriteria();
			criteria.setStartDate(year1 + "-08-01");
			criteria.setEndDate(year2 + "-07-01");
			criteria.setTeamId("29");
			criteria.setGameType("R,P");

			GameManager.disablePlaySaving();
			games.addAll(GameManager.getGames(criteria));
			
			year1++;
			year2++;
		}


		TeamData team;
		
		System.out.println("Getting stars...");
		for (GameData game : games) {
			int starsCount = 1;
			int starPointsCount = 3;
			int firstStarsCount = 1;

			PlayerData firstStar = game.decisions.firstStar;
			PlayerData secondStar = game.decisions.secondStar;
			PlayerData thirdStar = game.decisions.thirdStar;

			team = game.getTeamFromPlayer(firstStar);
			if(team != null && team.id == 29)
			{
				
				if (stars.containsKey(firstStar.id))
					starsCount += stars.get(firstStar.id);
				stars.put(firstStar.id, starsCount);
				
				if (starPoints.containsKey(firstStar.id))
					starPointsCount += starPoints.get(firstStar.id);
				starPoints.put(firstStar.id, starPointsCount);
				
				if (firstStars.containsKey(firstStar.id))
					firstStarsCount += firstStars.get(firstStar.id);
				firstStars.put(firstStar.id, firstStarsCount);
			}
			
			starsCount = 1;
			starPointsCount = 2;
			
			team = game.getTeamFromPlayer(secondStar);
			if(team != null && team.id == 29)
			{
				if (stars.containsKey(secondStar.id))
					starsCount += stars.get(secondStar.id);
				stars.put(secondStar.id, starsCount);
				
				if (starPoints.containsKey(secondStar.id))
					starPointsCount += starPoints.get(secondStar.id);
				starPoints.put(secondStar.id, starPointsCount);
			}
			
			starsCount = 1;
			starPointsCount = 1;
			
			team = game.getTeamFromPlayer(thirdStar);
			if(team != null && team.id == 29)
			{
				if (stars.containsKey(thirdStar.id))
					starsCount += stars.get(thirdStar.id);
				stars.put(thirdStar.id, starsCount);
				
				if (starPoints.containsKey(thirdStar.id))
					starPointsCount += starPoints.get(thirdStar.id);
				starPoints.put(thirdStar.id, starPointsCount);
			}
		}
		
		List<Integer> starsSorted = sortHashMap(stars);
		List<Integer> starPointsSorted = sortHashMap(starPoints);
		List<Integer> firstStarsSorted = sortHashMap(firstStars);
		
		System.out.println("Top 3 Most Stars");
		System.out.println(PlayerManager.getPlayerFromID(starsSorted.get(0)).fullName + "(" + stars.get(starsSorted.get(0)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(starsSorted.get(1)).fullName + "(" + stars.get(starsSorted.get(1)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(starsSorted.get(2)).fullName + "(" + stars.get(starsSorted.get(2)) + ")");
		System.out.println("Top 3 Most Star Points");
		System.out.println(PlayerManager.getPlayerFromID(starPointsSorted.get(0)).fullName + "(" + starPoints.get(starPointsSorted.get(0)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(starPointsSorted.get(1)).fullName + "(" + starPoints.get(starPointsSorted.get(1)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(starPointsSorted.get(2)).fullName + "(" + starPoints.get(starPointsSorted.get(2)) + ")");
		System.out.println("Top 3 Most First Stars");
		System.out.println(PlayerManager.getPlayerFromID(firstStarsSorted.get(0)).fullName + "(" + firstStars.get(firstStarsSorted.get(0)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(firstStarsSorted.get(1)).fullName + "(" + firstStars.get(firstStarsSorted.get(1)) + ")");
		System.out.println(PlayerManager.getPlayerFromID(firstStarsSorted.get(2)).fullName + "(" + firstStars.get(firstStarsSorted.get(2)) + ")");
		
		
	}
	
	public static List<Integer> sortHashMap(Map<Integer, Integer> mapToSort)
	{
	    List<Integer> keys = new ArrayList<>(mapToSort.keySet());
	    List<Integer> values = new ArrayList<>(mapToSort.values());
	    Collections.sort(values);
	    Collections.reverse(values);

	    List<Integer> keysSorted = new ArrayList<>();

	    for(int value: values)
	    {
	        for(int i = keys.size() - 1; i >= 0; i--)
		    {
	        	Integer key = keys.get(i);
	            Integer comp1 = mapToSort.get(key);

	            if (comp1 == value)
	            {
	            	keys.remove(i);
	            	keysSorted.add(key);
	                break;
	            }
	        }
	    }
	    return keysSorted;
	}
}

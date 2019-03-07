package com.theprogrammingturkey.nhlapi.data;

import java.util.List;

public class BoxScoreData
{
	public TeamData team;
	public List<PlayerStatData> playerStats;
	public List<GoalieStatData> goaliesStats;
	public List<SkaterStatData> skaterStats;
	public List<PlayerStatData> onIcePlayers;
	public List<PlayerStatData> scratches;
	public List<PlayerStatData> penaltyBox;
	
	public int goals;
	public int penaltyMinutes;
	public int shots;
	public String powerPlayPercentage;
	public int powerPlayGoals;
	public int powerPlayAttempts;
	public String faceoffPercentage;
	public int blockedShots;
	public int takeaways;
	public int giveaways;
	public int hits;
	public boolean goaliePulled;
	public boolean powerPlay;
	public int numSkaters;
}

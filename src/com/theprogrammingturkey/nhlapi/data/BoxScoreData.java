package com.theprogrammingturkey.nhlapi.data;

import java.util.List;

public class BoxScoreData
{
	public TeamData team;
	public TeamStatData teamGameStats;
	public List<PlayerStatData> playerStats;
	public List<GoalieStatData> goaliesStats;
	public List<SkaterStatData> skaterStats;
	public List<PlayerStatData> onIcePlayers;
	public List<PlayerStatData> scratches;
	public List<PlayerStatData> penaltyBox;
}

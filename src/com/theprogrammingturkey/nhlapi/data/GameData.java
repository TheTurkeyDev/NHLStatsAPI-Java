package com.theprogrammingturkey.nhlapi.data;

import java.util.Date;
import java.util.List;

public class GameData
{
	public int gameID;
	public String season;
	public String gameType;
	public Date date;
	public Date endDate;
	public String gameState;
	public boolean startTimeTBD;
	public String gameVenue;
	public TeamData homeTeam;
	public TeamData awayTeam;
	public List<PlayerData> players;
	public List<PlayData> plays;
	public int[] scoringPlays;
	public int[] penaltyPlays;
	public List<int[]> playsByPeriod;
	public PlayData currentPlay;
}
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
	public TeamRecordData homeRecord;
	public TeamData awayTeam;
	public TeamRecordData awayRecord;
	public List<PlayerData> players;
	public List<PlayData> plays;
	public List<PlayData> scoringPlays;
	public List<PlayData> penaltyPlays;
	public List<List<PlayData>> playsByPeriod;
	public PlayData currentPlay;
	public BoxScoreData homeBoxScore;
	public BoxScoreData awayBoxScore;
	public int currentPeriod;
	public String currentPeriodOrdinal;
	public String currentPeriodTimeRemaining;
	public String powerPlayStrength;
	public boolean hasShootout;
	public DecisionsData decisions;
	public List<PeopleData> officials;
	// TODO Period Info
	// TODO Shootout Info
	// TODO Powerplay Info
	// TODO Intermission Info

	public boolean homeTeamWon()
	{
		return this.homeBoxScore.goals > this.awayBoxScore.goals;
	}

	public boolean isGameTied()
	{
		return this.homeBoxScore.goals == this.awayBoxScore.goals;
	}

	public String getFinalScore()
	{
		return this.homeBoxScore.goals + " - " + this.awayBoxScore.goals;
	}

	public int getHomeGoals()
	{
		return this.homeBoxScore.goals;
	}

	public int getAwayGoals()
	{
		return this.awayBoxScore.goals;
	}

	public TeamData getTeamFromPlayer(PlayerData player)
	{
		for(PlayerStatData playerStat : homeBoxScore.playerStats)
			if(playerStat.playerID == player.id)
				return homeTeam;

		for(PlayerStatData playerStat : awayBoxScore.playerStats)
			if(playerStat.playerID == player.id)
				return awayTeam;

		return null;
	}
}

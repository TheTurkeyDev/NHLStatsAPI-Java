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
	// TODO Period Info
	// TODO Shootout Info
	// TODO Powerplay Info
	// TODO Intermission Info

	public boolean homeTeamWon()
	{
		return this.homeBoxScore.teamGameStats.goals > this.awayBoxScore.teamGameStats.goals;
	}

	public String getFinalScore()
	{
		return this.homeBoxScore.teamGameStats.goals + " - " + this.awayBoxScore.teamGameStats.goals;
	}
}

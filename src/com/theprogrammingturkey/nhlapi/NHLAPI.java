package com.theprogrammingturkey.nhlapi;

import java.text.SimpleDateFormat;

public class NHLAPI
{
	public static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1";

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss'Z'");

	/**
	 * https://statsapi.web.nhl.com/api/v1/schedule?startDate=2017-10-02&endDate=2018-02-05&teamId=29
	 * http://statsapi.web.nhl.com/api/v1/game/2017020798/feed/live
	 * http://statsapi.web.nhl.com/api/v1/game/2017020798/content
	 * https://statsapi.web.nhl.com/api/v1/standings?season=20152016
	 * https://statsapi.web.nhl.com/api/v1/schedule?startDate=2016-01-31&endDate=2016-02-05&expand=schedule.teams,schedule.linescore,schedule.broadcasts,schedule.ticket,schedule.game.content.media.epg&leaderCategories=&site=en_nhl&teamId=29
	 * http://statsapi.web.nhl.com/api/v1/teams
	 * http://statsapi.web.nhl.com/api/v1/teams/29?expand=team.schedule.next
	 * http://statsapi.web.nhl.com/api/v1/teams/29/roster?expand=roster.person,person.names
	 * http://statsapi.web.nhl.com/api/v1/divisions
	 * http://statsapi.web.nhl.com/api/v1/divisions/18
	 * http://statsapi.web.nhl.com/api/v1/conferences
	 * http://statsapi.web.nhl.com/api/v1/conferences/6 <== 7 is world cup of hockey
	 * http://statsapi.web.nhl.com/api/v1/people/8476429
	 * http://statsapi.web.nhl.com/api/v1/people/8476429?stats=gameLog&season=20152016  <== stats can also be 'yearByYear'
	 * http://statsapi.web.nhl.com/api/v1/venues/someValue <== Doesn't seem to work and is always referenced with null as the value. Maybe will be set up in a future date? 
	 * http://statsapi.web.nhl.com/api/v1/franchises/36
	 *
	 * ======== GAME ID's =========
	 *2015030412 = game code, change this to access different games
	 *2015       = season code, first year of the season (e.g., 2015 is for the 2015-16 seasons)
     *	  03     = game type code; 1 = preseason, 2 = regular season; 3 = playoffs
     *   	04   = playoff only: round number (1st round = 1, 2nd round = 2, ECF/WCF = 3, SCF = 4)
     *		  1  = series number: 1-8 in round 1, 1-4 in round 2, 1-2 in round 3,1 in round 4
     *   	   2 = game number: 1-7 for any given series
	 *
	 *2015020807 = game code, change this to access different games
	 *2015       = season code, first year of the season (e.g., 2015 is for the 2015-16 seasons)
     *    02     = game type code; 1 = preseason, 2 = regular season; 3 = playoffs
     *    0807   = game ID; generally 1-1230 in a normal regular season, but sometimes games will be missing (e.g., games cancelled due to weather) and sometimes games will be added on the end, starting with 1231 (e.g., make-up games for weather-cancelled games). Numbers are usually approx. 1-130ish in the pre-season, but it can be arbitrary.
	 *
	 *
	 */
}

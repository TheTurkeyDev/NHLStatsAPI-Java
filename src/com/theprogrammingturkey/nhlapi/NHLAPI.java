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
	 * http://statsapi.web.nhl.com/api/v1/people/8476429
	 */
}

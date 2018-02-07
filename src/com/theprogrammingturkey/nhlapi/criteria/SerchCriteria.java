package com.theprogrammingturkey.nhlapi.criteria;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.theprogrammingturkey.nhlapi.NHLAPI;

public class SerchCriteria
{
	private Map<String, String> params = new HashMap<>();

	public void setStartDate(Date date)
	{
		params.put("startDate", NHLAPI.DATE_FORMAT.format(date));
	}

	public void setStartDate(String date)
	{
		params.put("startDate", date);
	}

	public void setEndDate(Date date)
	{
		params.put("endDate", NHLAPI.DATE_FORMAT.format(date));
	}

	public void setEndDate(String date)
	{
		params.put("endDate", date);
	}

	public void setTeamId(int id)
	{
		params.put("teamId", "" + id);
	}
	
	/**
	 * 
	 * @param season Format Y1Y1Y1Y1Y2Y2Y2Y2 ex: 20172018
	 */
	public void setSeason(String season)
	{
		params.put("season", season);
	}
	
	/**
	 * Valid params. Any number/ combination of these can be used.
	 * schedule.teams
	 * schedule.linescore
	 * schedule.broadcasts
	 * schedule.ticket
	 * schedule.game.content.media.epg
	 * @param param
	 */
	public void addExpands(String param)
	{
		String base = params.get("expand");
		if(base == null)
			base = "";
		base += "," + param;
		params.put("expand", base);
	}

	public String toURLParams()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("?");
		for(String key : this.params.keySet())
		{
			builder.append(key);
			builder.append("=");
			builder.append(this.params.get(key));
			builder.append("&");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}

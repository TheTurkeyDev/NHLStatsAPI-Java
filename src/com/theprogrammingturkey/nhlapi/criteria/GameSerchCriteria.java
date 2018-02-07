package com.theprogrammingturkey.nhlapi.criteria;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.theprogrammingturkey.nhlapi.NHLAPI;

public class GameSerchCriteria
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

package com.theprogrammingturkey.nhlapi.managers;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.VenueData;

public class VenueManager extends BaseManager
{
	public static VenueData getVenueDataFromJSON(JsonObject json)
	{
		VenueData venue = new VenueData();

		venue.name = getStringSafe(json, "name");
		venue.city = getStringSafe(json, "city");
		venue.timezone = getStringSafe(getJsonObjectSafe(json, "timeZone"), "id");
		venue.timezoneOffset = getIntSafe(getJsonObjectSafe(json, "timeZone"), "offset");

		return venue;
	}
}

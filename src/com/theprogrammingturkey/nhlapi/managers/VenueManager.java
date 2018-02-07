package com.theprogrammingturkey.nhlapi.managers;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.nhlapi.data.VenueData;

public class VenueManager
{
	public static VenueData getVenueDataFromJSON(JsonObject json)
	{
		VenueData venue = new VenueData();

		venue.name = json.get("name").getAsString();
		venue.city = json.get("city").getAsString();
		venue.timezone = json.get("timeZone").getAsJsonObject().get("id").getAsString();
		venue.timezoneOffset = json.get("timeZone").getAsJsonObject().get("offset").getAsInt();

		return venue;
	}
}

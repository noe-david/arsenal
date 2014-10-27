package com.edit.reach.system;

import com.edit.reach.model.BoundingBox;

import java.net.MalformedURLException;
import java.net.URL;

public class WorldTruckerEndpoints {
	private static final String baseURL = "https://api.worldtrucker.com/v1/";

	private WorldTruckerEndpoints() {
	}

	public static URL getMilestonesURL(BoundingBox bbox) throws MalformedURLException {
		String urlString = "tip?bbox=" + bbox + "&categories=";
		return createURL(urlString);
	}

	private static URL createURL(String url) throws MalformedURLException {
		return new URL(baseURL + url);
	}
}

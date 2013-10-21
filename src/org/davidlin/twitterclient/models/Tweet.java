package org.davidlin.twitterclient.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends BaseModel {

	private User user;
	
	public User getUser() {
		return user;
	}
	
	public String getBody() {
		return getString("text");
	}
	
	public long getId() {
		return getLong("id");
	}
	
	public boolean isFavorited() {
		return getBoolean("favorited");
	}
	
	public boolean isRetweeted() {
		return getBoolean("retweeted");
	}
	
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.jsonObject = jsonObject;
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static List<Tweet> fromJson(JSONArray jsonArray) {
		List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweetJson != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
}

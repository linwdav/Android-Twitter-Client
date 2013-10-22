package org.davidlin.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String name;
	private String screenName;
	private String profileImageUrl;
	
	public String getName() {
		return this.name;
	}
	
	public String getScreenName() {
		return this.screenName;
	}
	
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}
	
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
            return null;
		}
		return user;
	}
	
	/*
	public String getName() {
		return getString("name");
	}
	
	public long getId() {
		return getLong("id");
	}
	
	public String getScreenName() {
		return getString("screen_name");
	}
	
	public String getProfileImageUrl() {
		return getString("profile_image_url");
	}
	
	public String getProfileBackgroundImageUrl() {
		return getString("profile_background_image_url");
	}
	
	public long getNumTweets() {
		return getInt("statuses_count");
	}
	
	public int getFollowersCount() {
		return getInt("followers_count");
	}
	
	public int getFriendsCount() {
		return getInt("friends_count");
	}
	
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		try {
			user.jsonObject = jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	*/
	
}

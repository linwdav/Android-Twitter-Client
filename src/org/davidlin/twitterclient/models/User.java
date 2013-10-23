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
	
}

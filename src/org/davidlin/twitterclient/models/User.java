package org.davidlin.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model {

	@Column(name = "name")
	private String name;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	public User() {
		super();
	}
	
	// Parse model from JSON
	public User(JSONObject jsonObject) {
		super();
		try {
			this.name = jsonObject.getString("name");
			this.screenName = jsonObject.getString("screen_name");
			this.profileImageUrl = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Getters
	public String getName() {
		return this.name;
	}
	public String getScreenName() {
		return this.screenName;
	}
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}
	
}

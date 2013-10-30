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
	
	@Column(name = "followersCount")
	private String followersCount;
	
	@Column(name = "followingCount")
	private String followingCount;
	
	@Column(name = "tagline")
	private String tagline;
	
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
			this.followersCount = jsonObject.getString("followers_count");
			this.followingCount = jsonObject.getString("friends_count");
			this.tagline = jsonObject.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return this.name;
	}
	public String getScreenName() {
		return this.screenName;
	}
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}
	public String getFollowersCount() {
		return this.followersCount;
	}
	public String getFollowingCount() {
		return this.followingCount;
	}
	public String getTagline() {
		return this.tagline;
	}
	
}

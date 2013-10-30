package org.davidlin.twitterclient.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.davidlin.twitterclient.TimelineActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {

	private static final long serialVersionUID = -2250715350493382896L;
	
	@Column(name = "user")
	private User user;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "twitterDate")
	private Date twitterDate;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "tweetId")
	private long tweetId;

	public Tweet() {
		super();
	}
	
	// Parse model from JSON
	public Tweet(JSONObject jsonObject) {
		super();
		try {
			//this.user = User.fromJson(jsonObject.getJSONObject("user"));
			this.user = new User(jsonObject.getJSONObject("user"));
			this.user.save();
			this.body = jsonObject.getString("text");
			this.twitterDate = getTwitterDate(jsonObject.getString("created_at"));
			this.date = new SimpleDateFormat("h:mm a - d MMM ''yy", TimelineActivity.getContext().getResources().getConfiguration().locale).format(this.twitterDate);
			this.tweetId = Long.valueOf(jsonObject.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	// Getters
	public User getUser() {
		return this.user;
	}
	public String getBody() {
		return this.body;
	}
	public Date getTwitterDate() {
		return this.twitterDate;
	}
	public String getDate() {
		return this.date;
	}
	public Long getTweetId() {
		return this.tweetId;
	}
	
	/*
	public static Tweet createTweet(User user, String body, String date) {
		Tweet tweet = new Tweet();
		tweet.user = user;
		tweet.body = body;
		tweet.date = date;
		tweet.id = 1844674407370955161L;
		return tweet;
	}
	*/

	public static List<Tweet> fromJson(JSONArray jsonArray) {
		List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				Tweet tweet = new Tweet(jsonArray.getJSONObject(i));
				if (tweet != null) {
					tweets.add(tweet);
					if (Tweet.all(Tweet.class).size() < 25) {
						tweet.save();
						Log.d("DEBUG", "Saving tweet # " + Tweet.all(Tweet.class).size());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return tweets;
	}

	public static Date getTwitterDate(String date) throws ParseException {
		final String twitterDateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterDateFormat, TimelineActivity.getContext().getResources().getConfiguration().locale);
		sf.setLenient(true);
		return sf.parse(date);
	}

}

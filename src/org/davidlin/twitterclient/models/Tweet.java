package org.davidlin.twitterclient.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.davidlin.twitterclient.TimelineActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

	private User user;
	private String body;
	private Date twitterDate;
	private String date;

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

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.body = jsonObject.getString("text");
			tweet.twitterDate = getTwitterDate(jsonObject.getString("created_at"));
			
			tweet.date = new SimpleDateFormat("h:mm a - d MMM ''yy", TimelineActivity.getContext().getResources().getConfiguration().locale).format(tweet.twitterDate);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tweet;
	}

	public static List<Tweet> fromJson(JSONArray jsonArray) {
		List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				Tweet tweet = fromJson(jsonArray.getJSONObject(i));
				if (tweet != null) {
					tweets.add(tweet);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return tweets;
	}

	private static Date getTwitterDate(String date) throws ParseException {
		final String twitterDateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterDateFormat, TimelineActivity.getContext().getResources().getConfiguration().locale);
		sf.setLenient(true);
		return sf.parse(date);
	}

}

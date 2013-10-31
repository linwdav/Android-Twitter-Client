package org.davidlin.twitterclient.fragments;

import java.util.List;

import org.davidlin.twitterclient.TwitterApp;
import org.davidlin.twitterclient.models.Tweet;
import org.davidlin.twitterclient.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	private long oldestTweetId = 0;
	private volatile String screenName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadTweets(false);
	}
	
	@Override
	public void loadTweets(final boolean isLoadingMore) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		Log.d("DEBUG", stackTraceElements[3].getMethodName());
		if (isNetworkConnected()) {
			if (getAdapter() != null && getAdapter().getCount() > 0)  {
				oldestTweetId = getAdapter().getItem(getAdapter().getCount() - 1).getTweetId() - 1;
			}
			Log.d("DEBUG", "Calling getUserTimeline from loadTweets with screenName " + screenName);
			TwitterApp.getRestClient().getUserTimeline(oldestTweetId, screenName, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					if (!isLoadingMore) {
						// Clear persisted tweets and users
						Tweet.delete(Tweet.class);
						User.delete(User.class);
					}
					List<Tweet> tweets = Tweet.fromJson(jsonTweets);
					Log.d("DEBUG", "There are this many tweets " + tweets.size());
					getAdapter().addAll(tweets);
				}
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					Toast.makeText(getActivity(), "Error loading tweets", Toast.LENGTH_SHORT).show();
					super.onFailure(arg0, arg1);
				}
			});
		} else {
			loadTweetsFromDb();
		}
	}
	
	private void loadTweetsFromDb() {
		List<Tweet> tweets = Tweet.all(Tweet.class);
		if (getAdapter() != null) {
			getAdapter().clear();
		}
		getAdapter().addAll(tweets);
	}

	@Override
	public void refreshTweets() {
		if (!isNetworkConnected()) {
			Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
		}
		else if (getAdapter() != null)  {
			getAdapter().clear();
			oldestTweetId = 0;
		}
		loadTweets(false);
	}
	
	public long getOldestTweetId() {
		return this.oldestTweetId;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
		refreshTweets();
	}

}

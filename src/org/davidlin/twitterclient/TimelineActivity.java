package org.davidlin.twitterclient;

import java.util.List;

import org.davidlin.twitterclient.models.Tweet;
import org.davidlin.twitterclient.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends SherlockActivity {

	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		context = this.getApplicationContext();
		getScreenName();
		getHomeTimeline();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	private void getScreenName() {
		TwitterApp.getRestClient().verifyAccountCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonAccount) {
				User user = User.fromJson(jsonAccount);
				ActionBar actionBar = getSupportActionBar();
				actionBar.setTitle("@" + user.getScreenName());
			}
		});
	}
	
	private void getHomeTimeline() {
		TwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", jsonTweets.toString());
				List<Tweet> tweets = Tweet.fromJson(jsonTweets);
				ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
	}
	
	public void refreshTweets(MenuItem mi) {
		Toast.makeText(getApplicationContext(), "Refresh tweets", Toast.LENGTH_SHORT).show();
	}
	
	public void composeTweet(MenuItem mi) {
		Toast.makeText(getApplicationContext(), "Compose tweet", Toast.LENGTH_SHORT).show();
	}
	
	public static Context getContext() {
		return context;
	}

}

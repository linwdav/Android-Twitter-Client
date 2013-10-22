package org.davidlin.twitterclient;

import java.util.List;

import org.davidlin.twitterclient.models.Tweet;
import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		TwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				List<Tweet> tweets = Tweet.fromJson(jsonTweets);
				ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void refreshTweets(MenuItem mi) {
		Toast.makeText(getApplicationContext(), "Refresh tweets", Toast.LENGTH_SHORT).show();
	}
	
	public void composeTweet(MenuItem mi) {
		Toast.makeText(getApplicationContext(), "Compose tweet", Toast.LENGTH_SHORT).show();
	}

}

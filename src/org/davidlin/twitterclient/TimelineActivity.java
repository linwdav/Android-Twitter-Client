package org.davidlin.twitterclient;

import java.util.List;

import org.davidlin.twitterclient.models.Tweet;
import org.davidlin.twitterclient.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends SherlockActivity {

	private static Context context;
	private static User currentUser;
	private TweetsAdapter adapter;
	private long oldestTweetId = 0;
	private ListView lvTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		context = this.getApplicationContext();
		setupViews();
		getScreenName();
		loadTweets();
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
				//User user = User.fromJson(jsonAccount);
				User user = new User(jsonAccount);
				currentUser = user;
				ActionBar actionBar = getSupportActionBar();
				actionBar.setTitle("@" + user.getScreenName());
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Toast.makeText(getApplicationContext(), "Error getting user screen name", Toast.LENGTH_SHORT).show();
				super.onFailure(arg0, arg1);
			}
			
			
		});
	}
	
	private void setupViews() {
		lvTweets = (ListView) findViewById(R.id.lvTweets);
	}
	
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else {
			return true;
		}
	}
	
	private void loadTweets() {
		if (isNetworkConnected()) {
			if (adapter != null && adapter.getCount() > 0)  {
				oldestTweetId = adapter.getItem(adapter.getCount() - 1).getTweetId() - 1;
			}
			TwitterApp.getRestClient().getHomeTimeline(oldestTweetId, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					// Clear persisted tweets and users
					Tweet.delete(Tweet.class);
					User.delete(User.class);
					List<Tweet> tweets = Tweet.fromJson(jsonTweets);
					addTweetsToListView(tweets);
				}
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					Toast.makeText(getApplicationContext(), "Error loading tweets", Toast.LENGTH_SHORT).show();
					super.onFailure(arg0, arg1);
				}
			});
		} else {
			loadTweetsFromDb();
		}
	}
	
	private void loadTweetsFromDb() {
		List<Tweet> tweets = Tweet.all(Tweet.class);
		if (adapter != null) {
			adapter.clear();
		}
		addTweetsToListView(tweets);
	}
	
	private void addTweetsToListView(List<Tweet> tweets) {
		if (adapter != null && adapter.getCount() > 0)  {
			// Add tweets to bottom for endless scrolling
			adapter.addAll(tweets);
		} else {
			adapter = new TweetsAdapter(getBaseContext(), tweets);
			lvTweets.setAdapter(adapter);
			lvTweets.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					if (isNetworkConnected()) {
						loadTweets();
					}
				}
			});
		}
	}
	
	public void refreshTweets(MenuItem mi) {
		if (!isNetworkConnected()) {
			Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
		}
		else if (adapter != null && adapter.getCount() > 0)  {
			adapter.clear();
			oldestTweetId = 0;
		}
		loadTweets();
	}
	
	public void composeTweet(MenuItem mi) {
		if (!isNetworkConnected()) {
			Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
		}
		else {
			Intent i = new Intent(this, TweetActivity.class);
			if (currentUser != null) {
				i.putExtra("profileImageUrl", currentUser.getProfileImageUrl());
				i.putExtra("screenName", "@" + currentUser.getScreenName());
			}
			else {
				i.putExtra("profileImageUrl", "");
				i.putExtra("screenName", "@User");
			}
	    	startActivityForResult(i, 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == 0) {
		 String tweetText = data.getExtras().getString("tweetMsg");
		 //Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
		 if (tweetText != null) {
			 /*
			 if (adapter != null) {
				 adapter.insert(tweet, 0);
			 } else {
				 List<Tweet> tweets = new ArrayList<Tweet>();
				 tweets.add(tweet);
				 adapter = new TweetsAdapter(getBaseContext(), tweets);
				 lvTweets.setAdapter(adapter);
					lvTweets.setOnScrollListener(new EndlessScrollListener() {
						@Override
						public void onLoadMore(int page, int totalItemsCount) {
							loadTweets();
						}
						
					});
			 }
			 */
			 TwitterApp.getRestClient().postTweet(tweetText, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					refreshTweets(null);
				}
				 
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					Toast.makeText(getApplicationContext(), "Error posting tweet", Toast.LENGTH_SHORT).show();
					super.onFailure(arg0, arg1);
				}
                 
			 });
		 }
	  }
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}

}

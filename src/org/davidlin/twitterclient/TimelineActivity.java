package org.davidlin.twitterclient;

import org.davidlin.twitterclient.fragments.HomeTimelineFragment;
import org.davidlin.twitterclient.fragments.MentionsFragment;
import org.davidlin.twitterclient.fragments.TweetsListFragment;
import org.davidlin.twitterclient.models.User;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends SherlockFragmentActivity implements TabListener {

	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		context = getApplicationContext();
		setupNavigationTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMention = actionBar.newTab().setText("Mentions").setTag("MentionsFragment").setIcon(R.drawable.ic_at).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMention);
		actionBar.selectTab(tabHome);
	}
	
	public void refreshTweets(MenuItem mi) {
		TweetsListFragment tweetFrag = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
		tweetFrag.refreshTweets();
	}
	
	public void composeTweet(MenuItem mi) {
		Intent i = new Intent(this, TweetActivity.class);
		User currentUser = TweetsListFragment.getCurrentUser();
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
	
	public void showProfile(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
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

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else if (tab.getTag() == "MentionsFragment") {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

}

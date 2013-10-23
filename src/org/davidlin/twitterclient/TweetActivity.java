package org.davidlin.twitterclient;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TweetActivity extends Activity {
	
	private ImageView ivProfileImage;
	private TextView tvScreenName;
	private EditText etTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
		String screenName = getIntent().getStringExtra("screenName");
		setupViews(profileImageUrl, screenName);
	}

	private void setupViews(String profileImageUrl, String screenName) {
		ivProfileImage = (ImageView) findViewById(R.id.ivTweetProfileImage);
		ImageLoader.getInstance().displayImage(profileImageUrl, ivProfileImage);
		tvScreenName = (TextView) findViewById(R.id.tvTweetUsername);
		tvScreenName.setText(screenName);
		etTweet = (EditText) findViewById(R.id.etTweet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
		return true;
	}
	
	public void cancel(View v) {
		finish();
	}
	
	public void tweet(View v) {
		String tweetMsg = etTweet.getText().toString();
		if (tweetMsg.length() > 140) {
			Toast.makeText(getApplicationContext(), "Tweet is " + (tweetMsg.length() - 140) + " characters over the 140 character limit.", Toast.LENGTH_LONG).show();
		}
		else {
			Intent i = new Intent();
			i.putExtra("tweetMsg", tweetMsg);
			setResult(RESULT_OK, i);
			finish();
		}
	}

}

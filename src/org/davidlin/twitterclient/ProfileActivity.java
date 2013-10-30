package org.davidlin.twitterclient;

import org.davidlin.twitterclient.fragments.UserTimelineFragment;
import org.davidlin.twitterclient.models.User;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		String screenName = getIntent().getStringExtra("screenName");
		if (screenName == null) {
			loadProfileInfo();
		} else {
			loadProfileInfo(screenName);
		}
	}
	
	public void loadProfileInfo() {
		final Context context = this.getApplicationContext();
		TwitterApp.getRestClient().getMyProfileInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonAccount) {
				Log.d("DEBUG", jsonAccount.toString());
				User user = new User(jsonAccount);
				populateProfileHeader(user);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.d("DEBUG", arg1.toString());
				Toast.makeText(context, "Error getting user", Toast.LENGTH_SHORT).show();
				super.onFailure(arg0, arg1);
			}
		});
	}
	
	public void loadProfileInfo(final String screenName) {
		final Context context = this.getApplicationContext();
		TwitterApp.getRestClient().getProfileInfo(screenName, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonAccount) {
				Log.d("DEBUG", jsonAccount.toString());
				User user = new User(jsonAccount);
				populateProfileHeader(user);
				UserTimelineFragment frag = (UserTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
				frag.setScreenName(screenName);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.d("DEBUG", arg1.toString());
				Toast.makeText(context, "Error getting user", Toast.LENGTH_SHORT).show();
				super.onFailure(arg0, arg1);
			}
		});
	}
	
	private void populateProfileHeader(User user) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName.setText(user.getName());
		String formattedTagline = "<font><small>" + user.getTagline() + "</font></small>";
		tvTagline.setText(Html.fromHtml(formattedTagline));
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFollowingCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
		
		Log.d("DEBUG", "Tagline is " + user.getTagline());
		Log.d("DEBUG", "Followercount is " + user.getFollowersCount());
		Log.d("DEBUG", "Followingcount is " + user.getFollowingCount());
	}

}

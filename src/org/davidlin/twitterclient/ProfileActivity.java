package org.davidlin.twitterclient;

import org.davidlin.twitterclient.models.User;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileActivity extends SherlockFragmentActivity {

	private User currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		loadProfileInfo();
	}
	
	public void loadProfileInfo() {
		final Context context = this.getApplicationContext();
		TwitterApp.getRestClient().verifyAccountCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonAccount) {
				User user = new User(jsonAccount);
				currentUser = user;
				populateProfileHeader(user);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Toast.makeText(context, "Error getting user screen name", Toast.LENGTH_SHORT).show();
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
	}

}

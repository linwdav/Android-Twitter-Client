package org.davidlin.twitterclient.fragments;

import java.util.ArrayList;
import java.util.List;

import org.davidlin.twitterclient.EndlessScrollListener;
import org.davidlin.twitterclient.R;
import org.davidlin.twitterclient.TweetsAdapter;
import org.davidlin.twitterclient.TwitterApp;
import org.davidlin.twitterclient.models.Tweet;
import org.davidlin.twitterclient.models.User;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TweetsListFragment extends SherlockFragment {

	private TweetsAdapter adapter;
	private ListView lvTweets;
	private static User currentUser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
		getMyScreenName();
	}
	
	private void setupViews() {
		lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		List<Tweet> tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(adapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (isNetworkConnected()) {
					loadTweets(true);
				}
			}
		});
	}
	
	private void getMyScreenName() {
		TwitterApp.getRestClient().getMyProfileInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonAccount) {
				User user = new User(jsonAccount);
				currentUser = user;
				ActionBar actionBar = getSherlockActivity().getSupportActionBar();
				actionBar.setTitle("@" + user.getScreenName());
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.d("DEBUG", arg1.toString());
				Toast.makeText(getActivity(), "Error getting user screen name", Toast.LENGTH_SHORT).show();
				super.onFailure(arg0, arg1);
			}
		});
	}
	
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else {
			return true;
		}
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}

	public abstract void loadTweets(boolean isLoadingMore);
	
	public abstract void refreshTweets();
	
}

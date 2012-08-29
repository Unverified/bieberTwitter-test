package com.bootcamp.beibertwitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class RetrieveTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
	private Context context;
	private View view;
	private View waitQueue;

	public RetrieveTweetsTask(Context context, View view) {
		this.context = context;
		this.view = view;
		waitQueue = view.findViewById(R.id.filterWait);
		// waitQueue.setVisibility(0);
	}

	@Override
	protected ArrayList<Tweet> doInBackground(String... searchTerms) {

		InputStream is = null;
		JSONObject oTweets = null;
		JSONArray aTweets = null;

		String url = "http://search.twitter.com/search.json?q=@"
				+ searchTerms[0];
		String result = "";
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		try {
			HttpClient c = new DefaultHttpClient();
			HttpGet req = new HttpGet(url);
			HttpResponse res = c.execute(req);
			HttpEntity ent = res.getEntity();

			is = ent.getContent();
		} catch (Exception e) {
			Log.e("getTweets",
					"ERROR-problem with http connection: " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder jsonString = new StringBuilder();
			String sLine = null;

			while ((sLine = reader.readLine()) != null)
				jsonString.append(sLine + "\n");

			is.close();
			result = jsonString.toString();
		} catch (Exception e) {
			Log.e("getTweets", "ERROR-can't convert result: " + e.getMessage());
		}

		try {
			oTweets = new JSONObject(result);
			aTweets = oTweets.getJSONArray("results");

		} catch (Exception e) {
			Log.e("getTweets",
					"ERROR-can't parse JSON object: " + e.getMessage());
		}

		try {
			if (aTweets != null)
				for (int i = 0; i < aTweets.length(); i++) {
					JSONObject jsonObject = aTweets.getJSONObject(i);

					Tweet tweet = new Tweet(jsonObject.getString("text"),
							jsonObject.getString("created_at"),
							jsonObject.getString("from_user"),
							jsonObject.getString("profile_image_url"));
					tweets.add(tweet);
				}
		} catch (Exception e) {
			Log.e("getTweets",
					"ERROR-can't create Tweet object: " + e.getMessage());
		}

		return tweets;
	}

	@Override
	protected void onPostExecute(ArrayList<Tweet> result) {
		super.onPostExecute(result);

		ListView listView = (ListView) view;
		listView.setAdapter(new TweetAdapter(context, R.layout.tweet_row,
				result));
		waitQueue = ((View) view.getParent()).findViewById(R.id.filterWait);
		waitQueue.setVisibility(1);
	}
}

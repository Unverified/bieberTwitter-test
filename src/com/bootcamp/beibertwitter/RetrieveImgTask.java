package com.bootcamp.beibertwitter;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class RetrieveImgTask extends AsyncTask<Void, Void, Bitmap>{
	private Tweet tweet;
	protected TweetAdapter tweetAdapter;
		
	public RetrieveImgTask(Tweet tweet, TweetAdapter tweetAdapter){
		this.tweet = tweet;
		this.tweetAdapter = tweetAdapter;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		try{
			URL imgUrl = new URL(tweet.sImgUrl);
			return BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		tweet.bitUser =  result;
		tweetAdapter.notifyDataSetChanged();
	}

}

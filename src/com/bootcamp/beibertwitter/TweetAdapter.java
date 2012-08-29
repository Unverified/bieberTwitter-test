package com.bootcamp.beibertwitter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	private ArrayList<Tweet> mTweets;

	public TweetAdapter(Context context, int textViewResourceId,
			ArrayList<Tweet> tweets) {
		super(context, textViewResourceId, tweets);
		mTweets = tweets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.tweet_row, null);
		}

		Tweet tweet = mTweets.get(position);
		if (tweet != null) {
			TextView poster = (TextView) v.findViewById(R.id.tweet_poster);
			TextView content = (TextView) v.findViewById(R.id.tweet_text);
			TextView postTime = (TextView) v.findViewById(R.id.tweet_time);
			ImageView userImg = (ImageView) v.findViewById(R.id.tweet_image);

			if (poster != null)
				poster.setText(tweet.sPoster);
			if (content != null)
				content.setText(tweet.sContent);
			if (postTime != null)
				postTime.setText(tweet.sCreateTime);
			if (userImg != null) {
				if (tweet.imgFetched) {
					if (tweet.bitUser != null)
					userImg.setImageBitmap(tweet.bitUser);
				} else {
					RetrieveImgTask getImage = new RetrieveImgTask(tweet, this);
					getImage.execute();
					tweet.imgFetched = true;
				}
			}

		}
		return v;
	}

}

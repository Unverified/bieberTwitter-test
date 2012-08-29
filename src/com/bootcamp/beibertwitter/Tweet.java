package com.bootcamp.beibertwitter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.util.Log;

public class Tweet {
	public String sContent;
	public String sCreateTime;
	public String sPoster;
	public String sImgUrl;
	
	public Bitmap bitUser = null;
	public boolean imgFetched = false;
	
	public Tweet(String content, String createTime, String poster, String imgUrl) {
		try {
			sContent = content;
			sCreateTime = formatDate(createTime).toString();
			sPoster = poster;
			sImgUrl = imgUrl;
		} catch (Exception e) {
			Log.e("Tweet", e.getMessage());
		}
	}
	
 	public static Date formatDate(String date) throws java.text.ParseException {
		final String twitterFormat = "EEE, dd MMM yyyy HH:mm:ss Z";
		SimpleDateFormat sfDate = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sfDate.setLenient(true);
		
		return sfDate.parse(date);
	}
}
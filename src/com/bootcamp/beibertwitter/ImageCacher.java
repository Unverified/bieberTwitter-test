package com.bootcamp.beibertwitter;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageCacher {
	public interface OnImageLoadedListener {
		public void onImageLoadedListener(Bitmap bitmap);
	}

	private static LruCache<String, Bitmap> imageCache;

	protected ImageCacher() {/* No Instance Can Be Created */
	}

	public static void instanceCache(int cacheSize) {
		if (cacheSize == 0) {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			imageCache = new LruCache<String, Bitmap>(maxMemory / 4);
		} else {
			imageCache = new LruCache<String, Bitmap>(cacheSize);
		}
	}

	public static Bitmap getBitmap(String url, OnImageLoadedListener listener) {
		if (imageCache != null) {
			Bitmap bitmap = imageCache.get(url);

			if (bitmap == null) {
				new RetrieveImgTask(listener).execute(url);
				return null;
			} else {
				return bitmap;
			}
		}

		Log.e("ImageCacher", "getBitmap called before the ImageCacher is instanced");
		return null;
	}

	private static class RetrieveImgTask extends AsyncTask<String, Void, Bitmap> {
		OnImageLoadedListener onImageLoadedListener;

		public RetrieveImgTask(OnImageLoadedListener listener) {
			onImageLoadedListener = listener;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];

			if (imageCache.get(url) == null) {
				Bitmap bitmap = getBitmap(url);
				if (bitmap != null) {
					if (imageCache.get(url) == null) {
						imageCache.put(url, bitmap);
					}

					return bitmap;
				}
			} else {
				return imageCache.get(url);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null && onImageLoadedListener != null) {
				onImageLoadedListener.onImageLoadedListener(result);
			}
			
			super.onPostExecute(result);
		}

		private Bitmap getBitmap(String url) {
			try {
				URL imgUrl = new URL(url);
				return BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}

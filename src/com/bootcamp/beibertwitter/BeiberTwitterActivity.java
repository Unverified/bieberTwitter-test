package com.bootcamp.beibertwitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class BeiberTwitterActivity extends Activity {
	private boolean created = false;
	private RetrieveTweetsTask getTweets = null;
	private String tweetSearch = "bieber";

	ListView listView = null;
	ProgressBar progressBar = null;
	private Button searchButton = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (!created) {
			created = true;
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);

			listView = (ListView) findViewById(R.id.tweetFeed);
			searchButton = (Button) findViewById(R.id.filterButton);
			progressBar = (ProgressBar) findViewById(R.id.filterWait);
			ImageCacher.instanceCache(0);

			setTwitterLookup((String) tweetSearch);
		}
	}

	public void setTwitterLookup(String tweetSearch) {
		getTweets = new RetrieveTweetsTask(this, listView);
		getTweets.execute(tweetSearch);
		searchButton.setText(tweetSearch);
	}

	public void changeFilter(View view) {
		final EditText textEdit = new EditText(this);

		AlertDialog tweetDlog = new AlertDialog.Builder(this)
				.setTitle("Update Tag")
				.setMessage("Get twitter feed with what tag?")
				.setView(textEdit)
				.setPositiveButton("Search", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						tweetSearch = textEdit.getText().toString();
						setTwitterLookup(tweetSearch);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();
		tweetDlog.show();
	}

}
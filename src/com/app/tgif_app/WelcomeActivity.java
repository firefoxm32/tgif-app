package com.app.tgif_app;

import java.util.UUID;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import model.Session;

public class WelcomeActivity extends Activity {
	protected Session session;
	private static Context appContext;
	private UUID uniqueKey;

	public static Context getContext() {
		return appContext;
	}

	public static void setContext(Context context) {
		appContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		setContext(this);
		session = new Session(getContext());
		if (!session.getTransactionId().isEmpty()) {
			Intent mainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
			overridePendingTransition(0, 0);
			startActivity(mainActivity);
			finish();
			return;
		}
		Button order = (Button) findViewById(R.id.startOrder);
		order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uniqueKey = UUID.randomUUID();
				startOrder(uniqueKey.toString());
			}
		});
	}

	private void startOrder(String transactionId) {
		Ion.with(getContext()).load(EndPoints.UPDATE_TABLE_STATUS)
				.setBodyParameter("table_number", String.valueOf(session.getTableNumber()))
				.setBodyParameter("status", "O").setBodyParameter("transaction_id", transactionId).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							return;
						}
						session.setTransactionId(uniqueKey.toString());
						Intent mainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
						overridePendingTransition(0, 0);
						startActivity(mainActivity);
						finish();
					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.exit(0);
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

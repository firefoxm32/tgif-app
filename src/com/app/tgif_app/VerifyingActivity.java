package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import model.Session;

public class VerifyingActivity extends Activity {
	protected Session session;
	private static Context appContext;
	private ProgressDialog pDialog;
	private boolean isDone = false;

	public static Context getContext() {
		return appContext;
	}

	public static void setContext(Context context) {
		appContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verifying);
		setContext(this);
		session = new Session(getContext());
		System.out.println("transaction_id: "+session.getTransactionId());
		if (!session.getTransactionId().isEmpty()) {
			verifying();
		}
	}

	private void verifying() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (!isDone) {
					try {
						Ion.with(getContext())
								.load(EndPoints.TABLE_STATUS
										+ "?transaction_id="+session.getTransactionId())
								.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
									@Override
									public void onCompleted(Exception arg0, JsonObject json) {
										// TODO Auto-generated method stub
										if (json == null) {
											Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
											return;
										}
										String status = json.get("table_status").getAsString();
										if (status.equalsIgnoreCase("W")) {
											isDone = true;
											session.removeTransactionId();
											Intent welcomeActivity = new Intent(VerifyingActivity.this, WelcomeActivity.class);
											overridePendingTransition(0, 0);
											startActivity(welcomeActivity);
											finish();
										}
									}
								});
						Thread.sleep(3000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verifying, menu);
		return true;
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

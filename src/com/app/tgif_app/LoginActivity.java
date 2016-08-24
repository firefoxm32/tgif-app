package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import model.Session;

public class LoginActivity extends Activity {

	protected Session session;
	private EditText username;
	private EditText password;
	private static Context appContext;
	private ProgressDialog pDialog;

	public static Context getContext() {
		return appContext;
	}

	public static void setContext(Context context) {
		appContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setContext(this);

		session = new Session(getContext());

		username = (EditText) findViewById(R.id.Username);
		password = (EditText) findViewById(R.id.Password);
		Button login = (Button) findViewById(R.id.login);

		if (!session.getTransactionId().isEmpty()) {
			System.out.println("Tid: " + session.getTransactionId());
		}

		if (!session.getUsername().isEmpty()) {
			Intent welcomeActivity = new Intent(LoginActivity.this, WelcomeActivity.class);
			startActivity(welcomeActivity);
			return;
		}

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (username.getText().toString().isEmpty()) {
					Toast.makeText(getContext(), "Enter username", Toast.LENGTH_SHORT).show();
					return;
				}
				if (password.getText().toString().isEmpty()) {
					Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
					return;
				}

				checkWifiConnection();
			}
		});
	}

	private void checkWifiConnection() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

//		String ssid = wifiInfo.getSSID().replace(".", "").replace("\"", "");
		String ssid = wifiInfo.getSSID().replace("\"", "");
		System.out.println("ssid: " + ssid.toLowerCase());
		if (!ssid.toLowerCase().equals("tattoo")) {
			Toast.makeText(getContext(), "Not connected to tgif wifi", Toast.LENGTH_SHORT).show();
			return;
		}
		// Toast.makeText(getContext(), "Connected to " + ssid + " wifi",
		// Toast.LENGTH_LONG).show();
		auth();
	}

	private void auth() {
		pDialog = new ProgressDialog(getContext());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();

		Ion.with(getContext()).load(EndPoints.LOGIN).progress(new ProgressCallback() {
			@Override
			public void onProgress(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}
		}).setBodyParameter("username", username.getText().toString())
				.setBodyParameter("password", password.getText().toString()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();

						if(object == null) {
							return;
						}
						
						String status = object.get("status").getAsString();
						String message = object.get("message").getAsString();
						System.out.println("status: " + status);
						if (status.equalsIgnoreCase("error")) {
							Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
							pDialog.dismiss();
							return;
						}

						session.setUsername(username.getText().toString());
						session.setPassword(session.getPassword().toString());
						session.setTableNumber(object.get("table_number").getAsInt());

						username.setText("");
						password.setText("");

						// Toast.makeText(getContext(), message,
						// Toast.LENGTH_SHORT).show();

						pDialog.dismiss();
						Intent welcomeActivity = new Intent(LoginActivity.this, WelcomeActivity.class);
						overridePendingTransition(0, 0);
						startActivity(welcomeActivity);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.exit(0);
		super.onBackPressed();
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

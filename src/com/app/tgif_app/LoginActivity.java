package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
		if (!session.getUsername().isEmpty()) {
			Intent welcomeActivity = new Intent(LoginActivity.this, WelcomeActivity.class);
			startActivity(welcomeActivity);
			finish();
			// return;
		}

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!validate()) {
					Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
					return;
				}
				auth();
			}
		});
	}

	private boolean validate() {
		boolean valid = true;
		if (username.getText().toString().isEmpty()) {
			// Toast.makeText(getContext(), "Enter valid username",
			// Toast.LENGTH_SHORT).show();
			valid = false;
		}
		if (password.getText().toString().isEmpty()) {
			// Toast.makeText(getContext(), "Enter password",
			// Toast.LENGTH_SHORT).show();
			valid = false;
		}
		return valid;
	}

	private void auth() {
		showProgressDialog();
		Ion.with(getContext()).load(EndPoints.LOGIN).setBodyParameter("username", username.getText().toString())
				.setBodyParameter("password", password.getText().toString()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						System.out.println("response: " + response);
						if (response == null) {
							Toast.makeText(getContext(), "Unable to login check network", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();

						if (object == null) {
							Toast.makeText(getContext(), "Unable to login check network", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}

						String status = object.get("status").getAsString();
						String message = object.get("message").getAsString();
						if (status.equalsIgnoreCase("error")) {
							Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						session.setUsername(username.getText().toString());
						session.setPassword(session.getPassword().toString());
						session.setTableNumber(object.get("table_number").getAsInt());

						username.setText("");
						password.setText("");

						hideProgressDialog();
						Intent welcomeActivity = new Intent(LoginActivity.this, WelcomeActivity.class);
						startActivity(welcomeActivity);
						finish();
					}

				});
	}

	private void showProgressDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Authenticating...");
			pDialog.setIndeterminate(true);
			pDialog.setCanceledOnTouchOutside(false);
		}
		pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
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
		// System.exit(0);
		// super.onBackPressed();
		moveTaskToBack(true);
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

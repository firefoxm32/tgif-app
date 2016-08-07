package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import model.Session;

public class LoginActivity extends AppCompatActivity {
	
	protected Session session;
	private EditText username;
	private EditText password;
	private static Context appContext;
	private ProgressDialog pDialog;
	private Fragment welcomeFragment;
	FragmentTransaction fragmentTransaction;
	
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
		username = (EditText) findViewById(R.id.Username);
		password = (EditText) findViewById(R.id.Password);
		Button login = (Button) findViewById(R.id.login);
		FrameLayout container2 = (FrameLayout) findViewById(R.id.container2);
		TextView lblLogin = (TextView) findViewById(R.id.loginLabel);
		
		session = new Session(getContext());
		welcomeFragment = new WelcomeFragment();
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		System.out.println("uname: "+session.getUsername());
		System.out.println("pass: "+session.getPassword());
		if(!session.getUsername().equals("") && !session.getPassword().equals("")) {
			username.setVisibility(View.GONE);
			password.setVisibility(View.GONE);
			login.setVisibility(View.GONE);
			container2.setVisibility(View.VISIBLE);
			lblLogin.setVisibility(View.GONE);
			fragmentTransaction.replace(R.id.container2, welcomeFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			username.setVisibility(View.VISIBLE);
			password.setVisibility(View.VISIBLE);
			lblLogin.setVisibility(View.VISIBLE);
			container2.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
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
				
				pDialog = new ProgressDialog(getContext());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Loading.... Please wait...");
				pDialog.setIndeterminate(true);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.show();
				
				Ion.with(getContext())
				.load(EndPoints.LOGIN)
				.progress(new ProgressCallback() {
					@Override
					public void onProgress(long arg0, long arg1) {
						// TODO Auto-generated method stub
						
					}
				})
				.setBodyParameter("username", username.getText().toString())
				.setBodyParameter("password", password.getText().toString())
				.asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();
						
						if (object != null) {
							System.out.println("status: "+object.get("status").getAsString());
							System.out.println("sql: "+object.get("sql").getAsString());
						} else {
							System.out.println(object);
							return;
						}
						
						
						
						if(object.get("status").getAsString().equalsIgnoreCase("error")) {
							Toast.makeText(getContext(), object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
							pDialog.dismiss();
							return;
						}
						session.setUsername(username.getText().toString());
						session.setPassword(password.getText().toString());
						session.setTableNumber(object.get("table_number").getAsInt());
						username.setText("");
						username.setFocusable(true);
						password.setText("");
						pDialog.dismiss();
						
						fragmentTransaction.replace(R.id.container, welcomeFragment);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}
				});
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

package com.app.tgif_app;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import model.Session;

public class WelcomeActivity extends Activity {
	protected Session session;
	private static Context appContext;
	private UUID uniqueKey;
	private RelativeLayout relativeLayout;
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
		setContentView(R.layout.activity_welcome);
		setContext(this);
		relativeLayout = (RelativeLayout) findViewById(R.id.welcome_relative_layout);
		session = new Session(getContext());
//		showProgressDialog("WELCOME");
//		Intent verifying = new Intent(WelcomeActivity.this, VerifyingActivity.class);
//		overridePendingTransition(0, 0);
//		startActivity(verifying);
//		finish();
//		if (!session.getUsername().isEmpty()) {
//			
//			System.out.println("SESSION UNAME: " + session.getUsername());
//			System.out.println("SESSION PASS: " + session.getPassword());
//			System.out.println("SESSION TABLE NUM: " + session.getTableNumber());
//			session.clearPrefs();
//		}
		if (!session.getTransactionId().isEmpty()) {
			Intent mainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
			overridePendingTransition(0, 0);
			startActivity(mainActivity);
			finish();
			return;
		}
		msg();
		Button order = (Button) findViewById(R.id.startOrder);
		order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (session.getUsername().isEmpty() && session.getPassword().isEmpty()) {
					toastMessage("This tablet is not yet setup");
					return;
				}
				uniqueKey = UUID.randomUUID();
				startOrder(uniqueKey.toString());
			}
		});
		confirmation();
	}

	private void startOrder(String transactionId) {
		final String _transactionId = transactionId;
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.UPDATE_TABLE_STATUS)
				.setBodyParameter("table_number", String.valueOf(session.getTableNumber()))
				.setBodyParameter("status", "O").setBodyParameter("transaction_id", transactionId).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						session.setTransactionId(_transactionId);
						session.setTableStatus("O");
						Intent mainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
						// mainActivity.putExtra("tag", "default");
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

	private int i = 10;
	private int x = 3;

	private void confirmation() {
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i--;
				if (x == i && i != 0) {
					toastMessage(x + " more tap");
					x--;
				}
				if (i == 0) {
					getAdminPass();
					i = 0;
					x = 0;
					return;
				}
			}
		});
	}

	private void getAdminPass() {
		confirmAdmin("admin12345");
	}

	private void confirmAdmin(final String adminPass) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.confirm_alert_dialog, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		final EditText password = (EditText) dialogView.findViewById(R.id.confirm_password);
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String pass = password.getText().toString();
				if (!adminPass.equalsIgnoreCase(pass)) {
					toastMessage("You're not a ADMIN");
					return;
				}
				setup();
			}
		});
		alertBuilder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				resetSetupData();
				dialog.cancel();
				toastMessage("Confirmation cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CONFIRMATION");
		alertDialog.setCustomTitle(view);
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
		hideSoftKeyboard();
	}

	private void setup() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.setup_alert_dialog, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		final EditText ipAddress = (EditText) dialogView.findViewById(R.id.alert_dialog_ipaddress);
		final EditText username = (EditText) dialogView.findViewById(R.id.alert_dialog_username);
		final EditText password = (EditText) dialogView.findViewById(R.id.alert_dialog_password);
		if (!session.getUsername().isEmpty() && !session.getIpAddress().isEmpty()) {
			ipAddress.setText(session.getIpAddress());
			username.setText(session.getUsername());
			password.setText(session.getPassword());
		}
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String ip = ipAddress.getText().toString().trim();
				String uname = username.getText().toString().trim();
				String pass = password.getText().toString().trim();
				if (uname.trim().isEmpty() || pass.trim().isEmpty() || ip.trim().isEmpty()) {
					toastMessage("Incomplete input setup");
				} else {
					saveSetup(uname, pass, ip);
				}
			}
		});
		alertBuilder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				resetSetupData();
				toastMessage("Setup cancelled");
				dialog.cancel();
			}
		});
		alertBuilder.setNeutralButton("Remove Setup", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				removeSetup();
				resetSetupData();
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("SETUP");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}

	private void resetSetupData() {
		i = 10;
		x = 3;
	}

	private void saveSetup(String uname, String pass, String ip) {
		showProgressDialog("Autenticating");
		final String username = uname;
		final String password = pass;
		session.setIpAddress(ip);
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.LOGIN)
				.setBodyParameter("username", uname).setBodyParameter("password", pass).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						System.out.println("UNAME: " + username);
						System.out.println("PASS: " + password);
						System.out.println("response: " + response);
						if (response == null) {
							resetSetupData();
							toastMessage("Error setup");
							hideProgressDialog();
							return;
						}
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();

						if (object == null) {
							resetSetupData();
							toastMessage("Error setup");
							hideProgressDialog();
							return;
						}
						String status = object.get("status").getAsString();
						String message = object.get("message").getAsString();
						if (status.equalsIgnoreCase("error")) {
							// Toast.makeText(getContext(), message,
							// Toast.LENGTH_SHORT).show();
							resetSetupData();
							toastMessage(message);
							hideProgressDialog();
							return;
						}
						session.setTableStatus("W");
						session.setUsername(username);
						session.setPassword(password);
						session.setTableNumber(object.get("table_number").getAsInt());
						hideProgressDialog();
					}
				});
	}

	private void removeSetup() {
		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.LOGOUT)
				.setBodyParameter("username", session.getUsername())
				.setBodyParameter("table_number", String.valueOf(session.getTableNumber())).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();
						if (object.get("status").getAsString().equalsIgnoreCase("error")) {
							toastMessage(object.get("message").getAsString());
							hideProgressDialog();
							return;
						}
						session.removeTransactionId();
						session.remove("username", "password", "tableNumber");
						session.clearPrefs();
						toastMessage("Setup removed");
						hideProgressDialog();
					}
				});
	}

	private void showProgressDialog(String message) {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setIndeterminate(true);
			pDialog.setCanceledOnTouchOutside(false);
			// pDialog.setMessage("Authenticating...");
			SpannableString ss1 = new SpannableString(message);
			ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
			ss1.setSpan(new ForegroundColorSpan(R.color.black), 0, ss1.length(), 0);
			pDialog.setMessage(ss1);
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

	private void toastMessage(String message) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast_layout, null);
		TextView msg = (TextView) layout.findViewById(R.id.toast_message);
		msg.setText(message);
		Toast toast = new Toast(getContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
	private void msg() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_instruction, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		TextView message = (TextView) dialogView.findViewById(R.id.msg);
		message.setText("To order please tap button START ORDER.");
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("App Message");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}
	
	private void hideSoftKeyboard() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}

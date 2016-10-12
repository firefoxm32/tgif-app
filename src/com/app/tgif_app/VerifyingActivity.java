package com.app.tgif_app;

import com.google.gson.JsonObject;
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
import android.view.View;
import android.widget.TextView;
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
//		showProgressDialog("Processing Payments...");
		session = new Session(getContext());
		if (!session.getTransactionId().isEmpty()) {
			verifying();
		}
	}

	private void verifying() {
		showProgressDialog("Processing Payments...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (!isDone) {
					try {
						processing();
						Thread.sleep(3000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void processing() {
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.TABLE_STATUS
				+ "?transaction_id=" + session.getTransactionId()).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							toastMessage("Network error");
							return;
						}
						String status = json.get("table_status").getAsString();

						if (status.equalsIgnoreCase("W")) {
							isDone = true;
							session.removeTransactionId();
							session.setTableStatus("W");
							hideProgressDialog();
							msg();
						} else if (status.equalsIgnoreCase("O")) {
							isDone = true;
							session.setTableStatus("O");
							hideProgressDialog();
							toastMessage("Payment cancelled! Invalid credit card.");
							Intent mainActivity = new Intent(VerifyingActivity.this, MainActivity.class);
							overridePendingTransition(0, 0);
							startActivity(mainActivity);
							finish();
						}
					}
				});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.verifying, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
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
		message.setText("Please wait for your receipt or change.");
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				Intent welcomeActivity = new Intent(VerifyingActivity.this, WelcomeActivity.class);
				overridePendingTransition(0, 0);
				startActivity(welcomeActivity);
				finish();
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("App Message");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}
	
	private void showProgressDialog(String message) {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setIndeterminate(true);
			pDialog.setCanceledOnTouchOutside(false);
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
}

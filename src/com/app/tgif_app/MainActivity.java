package com.app.tgif_app;

import java.text.DecimalFormat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import model.Session;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	public static Toolbar mToolbar;
	private ProgressDialog pDialog;
	protected Session session;
	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;
	private static Context appContext;

	public static String[] menus = new String[] { "Home", "Food Menu", "My Order", "Check Out", "Feedback", "Logout" };

	// public static List<DrawerItem> list;
//	private EditText cash;
//	private double totalBill;

	public static Context getContext() {
		return appContext;
	}

	public static void setContext(Context context) {
		appContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setContext(this);
		session = new Session(getContext());
		String tag = "default";
		if (getIntent().getExtras() != null) {
			System.out.println("welcome");
			tag = getIntent().getStringExtra("tag");
			if (tag.equalsIgnoreCase("my-order")) {
				onSectionAttached(2);
			}
		}
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// mTitle = MainActivity.menus[0];// getString(MainActivity.menus[0]);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(mToolbar, R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		onSectionAttached(position);
	}

	public void onSectionAttached(int number) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Home home = new Home();
		switch (number) {
		case 0:
			mTitle = MainActivity.menus[0];// getString(R.string.Home);
			ft.replace(R.id.container, home);
			ft.addToBackStack(null);
			ft.commit();
			break;
		case 1:
			mTitle = MainActivity.menus[1] + "s";// getString(R.string.Food_Menu);
			FoodMenuFragment foodMenuFragment = new FoodMenuFragment();
			ft.replace(R.id.container, foodMenuFragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
		case 2:
			mTitle = MainActivity.menus[2] + "s";// getString(R.string.My_Order);
			Fragment myOrderFragment = new MyOrderFragment();
			ft.replace(R.id.container, myOrderFragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
		case 3:
			mTitle = MainActivity.menus[3];// getString(R.string.Check_Out);
			getTotalPrice();
			break;
		case 4:
			feedback();
			break;
		case 5:
			mTitle = MainActivity.menus[4];
			
			confirmAdmin("admin12345");
			break;
		default:
			mTitle = MainActivity.menus[0];// getString(R.string.Home);
			ft.replace(R.id.container, home);
			ft.addToBackStack(null);
			ft.commit();
			break;
		}
	}

	private void feedback() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.fragment_feedback, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		final EditText customerName = (EditText) dialogView.findViewById(R.id.feedback_user);
		final EditText msg = (EditText) dialogView.findViewById(R.id.feedback_messageg);
		alertBuilder.setPositiveButton("Send", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String customer = customerName.getText().toString();
				String message = msg.getText().toString();
				if (message.isEmpty()) {
					toastMessage("Cannot send feedback with empty message");
					msg.requestFocus();
				} else {
					toastMessage("Sent!");
					if (customer.isEmpty()) {
						customer = "Customer";
					} 
					sendFeedBack(customer, message);
				}
			}
		});
		alertBuilder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				toastMessage("Sending feedback cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("FEEDBACK");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}

	private void sendFeedBack(String customerName, String message) {
		Ion.with(getContext()).load(EndPoints.SEND_FEEDBACK)
				.setBodyParameter("transaction_id", session.getTransactionId())
				.setBodyParameter("customer_name", customerName).setBodyParameter("message", message).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject object = new JsonParser().parse(response).getAsJsonObject();
						if (object == null) {
							return;
						}
						if (object.get("status").getAsString().toLowerCase().equals("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}
						toastMessage(object.get("message").getAsString());
					}
				});
	}

	private void getTotalPrice() {
		Ion.with(getContext()).load(EndPoints.TOTAL_PRICE)
				.setBodyParameter("transaction_id", session.getTransactionId()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject json = new JsonParser().parse(response).getAsJsonObject();
						checkOut(json.get("total_price").getAsDouble());
					}
				});
	}

	private void checkOut(double _totalBill) {
		final DecimalFormat formatter = new DecimalFormat("#,##0.00");
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.check_out, null);
		alert.setTitle("Checking Out");
		alert.setCancelable(false);
		alert.setView(dialogView);
		final TextView total = (TextView) dialogView.findViewById(R.id.bill);
		final EditText cash = (EditText) dialogView.findViewById(R.id.cash);
//		final EditText memberCard = (EditText) dialogView.findViewById(R.id.member_card);
//		memberCard.setVisibility(View.GONE);
		final Double totalBill = _totalBill;
		total.setText("Total: "+formatter.format(totalBill));
		cash.requestFocus();
		alert.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				double cashEnter = 0.00;
				
				if (totalBill == cashEnter) {
					toastMessage("Not yet order");
					return;
				}
				if (cash.getText().toString().isEmpty()){
					toastMessage("Invalid input cash");
					return;
				}
				if (Double.valueOf(total.getText().toString()) > cashEnter) {
					toastMessage("Insufficient amount");
					return;
				}
				cashEnter = Double.valueOf(cash.getText().toString());
				toastMessage("here");
				insertCashHeader(session.getTransactionId(), session.getTableNumber(), cashEnter, 
						Double.valueOf(total.getText().toString()));
			}
		});
		alert.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				toastMessage("CANCELLED!");
			}
		});
		AlertDialog alertDialog = alert.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CHECK OUT");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}

	private void insertCashHeader(String transactionId, int tableNumber, double cashAmount, double total) {
		Ion.with(getContext()).load(EndPoints.INSERT_CASH_HEADER).setBodyParameter("transaction_id", transactionId)
				.setBodyParameter("cash_amount", String.valueOf(cashAmount))
				.setBodyParameter("total_price", String.valueOf(total)).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject object = new JsonParser().parse(response).getAsJsonObject();
						if (object == null) {
							return;
						}
						if (object.get("status").getAsString().toLowerCase().equals("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}
						toastMessage(object.get("message").getAsString());
						Intent verifying = new Intent(MainActivity.this, VerifyingActivity.class);
						overridePendingTransition(0, 0);
						startActivity(verifying);
						finish();
					}
				});
	}

	private void logout() {
		showProgressDialog();
		if (session.getUsername() != "") {
			Ion.with(getContext()).load(EndPoints.LOGOUT).progress(new ProgressCallback() {
				@Override
				public void onProgress(long arg0, long arg1) {
					// TODO Auto-generated method stub

				}
			}).setBodyParameter("username", session.getUsername())
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
							hideProgressDialog();
							Intent loginActivity = new Intent(getContext(), LoginActivity.class);
							startActivity(loginActivity);
							finish();
						}
					});
		} else {
			toastMessage("Error");
		}
	}

	public void restoreActionBar() {
		// Instead of ActionBar, work with Toolbar
		// if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
		// mToolbar.setTag(mTitle);
		// }
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState, persistentState);
	}

	private void showProgressDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Loading...");
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
	private void hideSoftKeyboard() {
//		if (getCurrentFocus() != null) {
//			InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		}
	}
	
	private void confirmAdmin(final String adminPass) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		alertBuilder.setTitle("Confirmation");
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
				logout();
			}
		});
		alertBuilder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				toastMessage("Confirmation cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		alertDialog.show();
	}
}

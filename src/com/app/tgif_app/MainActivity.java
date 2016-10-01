package com.app.tgif_app;

import java.text.DecimalFormat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

	public static String[] menus = new String[] { "Home", "Food Menu", "My Order", "Feedback", "Check Out", "Setup" };

	// public static List<DrawerItem> list;
	private EditText cash;
	private double totalBill;
	private EditText memberId;
	private EditText creditCardNumber;
	private EditText creditCardName;

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

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// mTitle = MainActivity.menus[0];// getString(MainActivity.menus[0]);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		if (getIntent().getExtras() != null) {
			tag = getIntent().getStringExtra("tag");
			if (tag.equalsIgnoreCase("my-order")) {
				getSupportActionBar().setTitle("My Orders");
				onSectionAttached(2);
			}
		}
		if (session.getTablestatus().equalsIgnoreCase("C")) {
			Intent verifying = new Intent(MainActivity.this, VerifyingActivity.class);
			overridePendingTransition(0, 0);
			startActivity(verifying);
			finish();
		}
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
			mToolbar.setTitle(mTitle);
			ft.replace(R.id.container, myOrderFragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
		case 3:
			mTitle = MainActivity.menus[3];// getString(R.string.Check_Out);
			feedback();
			break;
		case 4:
			getTotalPrice();
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
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.SEND_FEEDBACK)
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
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.TOTAL_PRICE)
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
						System.out.println("TOTAL PRICE:");
						checkOut(json.get("total_price").getAsDouble());
					}
				});
	}

	private String method;

	private void checkOut(double _totalBill) {
		System.out.println("CheckOut");
		final DecimalFormat formatter = new DecimalFormat("#,##0.00");
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.check_out, null);
		alert.setCancelable(false);
		alert.setView(dialogView);

		Button cashMethod = (Button) dialogView.findViewById(R.id.cash_method);
		Button creditCardMethod = (Button) dialogView.findViewById(R.id.credit_card_method);
		TextView total = (TextView) dialogView.findViewById(R.id.bill);
		cash = (EditText) dialogView.findViewById(R.id.cash);
		memberId = (EditText) dialogView.findViewById(R.id.member_id);
		creditCardName = (EditText) dialogView.findViewById(R.id.credit_card_name);
		creditCardNumber = (EditText) dialogView.findViewById(R.id.credit_card_number);
		
		creditCardName.setVisibility(View.GONE);
		creditCardNumber.setVisibility(View.GONE);
		cashMethod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				method = "cash_method";
				creditCardName.setVisibility(View.GONE);
				creditCardNumber.setVisibility(View.GONE);
				cash.setVisibility(View.VISIBLE);
				cash.setText("");
				memberId.setText("");
				cash.requestFocus();
			}
		});
		creditCardMethod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				method = "credit_card_method";
				creditCardName.setVisibility(View.VISIBLE);
				creditCardNumber.setVisibility(View.VISIBLE);
				cash.setVisibility(View.GONE);
				creditCardName.setText("");
				creditCardNumber.setText("");
				memberId.setText("");
				creditCardName.requestFocus();
			}
		});

		// double totalBill = _totalBill;
		totalBill = _totalBill;
		total.setText("Total: " + formatter.format(totalBill));
		cash.requestFocus();
		alert.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (totalBill == 0.00) {
					toastMessage("Not yet order");
					return;
				}
				if (method.equalsIgnoreCase("cash_method")) {
					if (cash.getText().toString().isEmpty()) {
						toastMessage("Invalid input cash");
						return;
					}

					if (totalBill > Double.valueOf(cash.getText().toString())) {
						toastMessage("Insufficient amount");
						return;
					}
					insertCashHeader(session.getTransactionId(), session.getTableNumber(),
							Double.valueOf(cash.getText().toString()), totalBill, 
							creditCardName.getText().toString(), 
							creditCardNumber.getText().toString(), memberId.getText().toString());
				} else {
					if (creditCardName.getText().toString() == "") {
						toastMessage("Please enter credit card holder name");
						return;
					}

					if (creditCardNumber.getText().toString() == "") {
						toastMessage("Please enter credit card number");
						return;
					}
					insertCashHeader(session.getTransactionId(), session.getTableNumber(),
							Double.valueOf(cash.getText().toString()), totalBill, 
							creditCardName.getText().toString(), 
							creditCardNumber.getText().toString(), memberId.getText().toString());
				}
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

	private void insertCashHeader(String transactionId, int tableNumber, 
			double cashAmount, double total, String cardName,String cardNumber,String id) {
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.INSERT_CASH_HEADER)
				.setBodyParameter("transaction_id", transactionId)
				.setBodyParameter("cash_amount", String.valueOf(cashAmount))
				.setBodyParameter("member_id", id)
				.setBodyParameter("total_price", String.valueOf(total))
				.setBodyParameter("credit_card_name", cardName)
				.setBodyParameter("credit_card_number", cardNumber).asString()
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
						System.out.println("STATUS: " + object.get("status").getAsString());
						if (object.get("status").getAsString().equalsIgnoreCase("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}

						if (object.get("status").getAsString().toLowerCase().equals("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}
						toastMessage(object.get("message").getAsString());
						session.setTableStatus("C");
						Intent verifying = new Intent(MainActivity.this, VerifyingActivity.class);
						overridePendingTransition(0, 0);
						startActivity(verifying);
						finish();
					}
				});
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
				toastMessage("Setup cancelled");
				dialog.cancel();
			}
		});
		alertBuilder.setNeutralButton("Remove Setup", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				removeSetup();
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
							toastMessage("Error setup");
							hideProgressDialog();
							return;
						}
						JsonParser jsonParser = new JsonParser();
						JsonObject object = jsonParser.parse(response).getAsJsonObject();

						if (object == null) {
							toastMessage("Error setup");
							hideProgressDialog();
							return;
						}
						String status = object.get("status").getAsString();
						String message = object.get("message").getAsString();
						if (status.equalsIgnoreCase("error")) {
							// Toast.makeText(getContext(), message,
							// Toast.LENGTH_SHORT).show();
							toastMessage(message);
							hideProgressDialog();
							return;
						}
						session.setUsername(username);
						session.setPassword(password);
						session.setTableNumber(object.get("table_number").getAsInt());
						toastMessage("Setup removed");
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
						hideProgressDialog();
						Intent welcomeActivity = new Intent(getContext(), WelcomeActivity.class);
						startActivity(welcomeActivity);
						finish();
					}
				});
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
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
				dialog.cancel();
				toastMessage("Confirmation cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CONFIRMATION");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}

	private void YesNo() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		alertBuilder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// getTotalPrice();
			}
		});
		alertBuilder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				toastMessage("Bill Out Cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CONFIRMATION");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
		hideSoftKeyboard();
	}
}

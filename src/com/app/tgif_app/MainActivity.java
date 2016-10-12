package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import model.Session;

//public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
public class MainActivity extends AppCompatActivity {
	public static Toolbar mToolbar;
	private ViewPager viewPager;
	private ImageView toolbarImage;
	private TextView help;
	private ProgressDialog pDialog;
	protected Session session;
//	private NavigationDrawerFragment mNavigationDrawerFragment;

	private String mTitle;
	private static Context appContext;

	public static String[] menus = new String[] { "Home", "Food Menu", "My Order", "Feedback", "Check Out" };

	// public static List<DrawerItem> list;
	
	public static FragmentTabHost mTabHost;
	public static Bundle orderDetailsBundle;
	public static Bundle subFoodMenuBundle;
	
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
		
		if (session.getTablestatus().equalsIgnoreCase("C")) {
			Intent verifying = new Intent(MainActivity.this, VerifyingActivity.class);
			overridePendingTransition(0, 0);
			startActivity(verifying);
			finish();
		}
		
		orderDetailsBundle = new Bundle();
		orderDetailsBundle.putBoolean("isNull", true);
		subFoodMenuBundle = new Bundle();
		subFoodMenuBundle.putBoolean("isNull", true);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mTitle = mToolbar.getTitle().toString() + session.getTableNumber().toString();
		getSupportActionBar().setTitle(mTitle);
		toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
		help = (TextView) findViewById(R.id.help);
		
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.main_realtabcontent);
		
        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("Home"),
                Home.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("menu").setIndicator("Menu"),
                FoodMenuFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("food_menu").setIndicator("Food Menu"),
        		SubFoodMenuFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("order_details").setIndicator("Order Details"),
        		OrderDetails.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("my_order").setIndicator("My Orders"),
                MyOrderFragment.class, null);
		
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {	
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (!tabId.equalsIgnoreCase("my orders")) {
					CookingOrder.stopCooking();
					ServedOrder.stopServe();
				}
			}
		});
        
        help.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msg();
			}
		});
		
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        
//		viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
//			@Override
//			public void onTabUnselected(Tab tab) {
//				// TODO Auto-generated method stub
//				viewPager.setCurrentItem(tab.getPosition());
//			}
//			
//			@Override
//			public void onTabSelected(Tab tab) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onTabReselected(Tab tab) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		
		confirmation();
	}
	
//	private void setupViewPager(ViewPager viewPager) {
//	      ViewPager adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//	      viewPager.setAdapter(adapter);
//	  }
	
	private void msg() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_instruction, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		TextView message = (TextView) dialogView.findViewById(R.id.msg);
		
		String instructions = "1. START ORDER \n" +
						"2. CHOOSE ORDER in ALL TIME FAVORITES OR PROMOS OR MENU TAB\n"+
						"3. CHOOSE CATEGORY TO VIEW EACH ITEMS\n"+
						"4. IN ORDER DETAILS YOU ADD ORDER\n"+
						"5. IF FINISH ADDING ORDER YOU CAN VIEW YOUR ORDERS IN MY ORDERS PENDING TAB\n"+
						"6. IF YOU WANT TO EDIT OR DELETE SPECIFIC ORDER YOU CAN SELECT ITEM\n"+
						"7. AFTER VERIFYING YOUR ORDERS YOU CAN SEND THEM TO THE KITCHEN BY CLICKING SEND ORDERS BUTTON\n"+
						"8. AFTER SENDING YOU CAN\'T CANCEL YOUR ORDERS\n"+
						"9. TO VIEW YOUR COOKING ORDER TAP MY ORDERS COOKING TAB\n"+
						"10. TO VIEW YOUR SERVED ORDER TAP MY ORDERS SERVED TAB\n"+
						"11. IF ALL YOUR ORDERS SERVED YOU CAN RATE SPECIFIC FOOD OR SEND FEEDBACK TO THE ADMIN\n"+
						"12. CHECK OUT TO PAY YOUR BILL\n"+
						"13. CHOOSE IF CASH METHOD OR VIA CREDIT CARD\n"+
						"14. WAIT FOR THE WAITER TO GET YOUR RECEIPT AND CHANGE\n"+
						"15. ";
		
		message.setText(instructions);
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
	}

	private void confirmation() {
		toolbarImage.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				confirmAdmin("admin12345");
				return true;
			}
		});
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
		showProgressDialog("Autenticating...");
		final String username = uname;
		final String password = pass;
		session.setIpAddress(ip);
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.LOGIN)
				.setBodyParameter("username", uname).setBodyParameter("password", pass).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
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

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//			finish();
//		} else {
//			super.onBackPressed();
//		}
//	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// if (!mNavigationDrawerFragment.isDrawerOpen()) {
	// // Only show items in the action bar relevant to this screen
	// // if the drawer is not showing. Otherwise, let the drawer
	// // decide what to show in the action bar.
	// getMenuInflater().inflate(R.menu.main, menu);
	// restoreActionBar();
	// return true;
	// }
	// return super.onCreateOptionsMenu(menu);
	// }

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

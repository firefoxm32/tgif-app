package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import model.Session;

public class EditOrderActivity extends AppCompatActivity {
	private static Toolbar mToolbar;
	protected Session session;
	private ImageView itemImage;
	private TextView itemName;
	private TextView serving;
	private TextView sauces;
	private TextView sideDish;
	private EditText quantity;
	private Button btnEdit;
	private Button btnDelete;
	private ProgressDialog pDialog;
	private static Context appContext;
	private Bundle b;

	public static Context getContext() {
		return appContext;
	}

	public static void setContext(Context context) {
		appContext = context;
	}

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceState);
		setContext(this);
		session = new Session(getContext());
		setContentView(R.layout.activity_edit_order);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mToolbar.setTitle("Edit / Delete Order");
//		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back2);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		
		b = getIntent().getExtras();
		if (b == null) {
			toastMessage("No data");
			return;
		}
		itemImage = (ImageView) findViewById(R.id.edit_order_image);
		itemName = (TextView) findViewById(R.id.edit_order_item_name);
		serving = (TextView) findViewById(R.id.edit_order_serving);
		sauces = (TextView) findViewById(R.id.edit_order_sauce);
		sideDish = (TextView) findViewById(R.id.edit_order_side_dish);
		quantity = (EditText) findViewById(R.id.edit_order_edittext_quantity);
		btnEdit = (Button) findViewById(R.id.edit_order_btn_edit);
		btnDelete = (Button) findViewById(R.id.edit_order_btn_delete);

		Picasso
		.with(getContext())
		.load(EndPoints.HTTP+session.getIpAddress()+EndPoints.PICASSO+b.getString("item_name").replace(" ", "%20").toLowerCase()+"/"+b.getString("item_image"))
		.error(R.drawable.not_found)
		.into(itemImage);
		
		if (b.getString("item_name").equals("")) {
			itemName.setVisibility(View.GONE);
		}
		itemName.setText("Item Name: " + b.getString("item_name"));
		if (b.getString("serving").equals("")) {
			serving.setVisibility(View.GONE);
		}
		serving.setText("Serving: " + b.getString("serving"));
		if (b.getString("sauces").equals("")) {
			sauces.setVisibility(View.GONE);
		}
		sauces.setText("Sauce/s: " + b.getString("sauces"));
		
		if (b.getString("side_dish").equals("")) {
			
			sideDish.setVisibility(View.GONE);
		}
		sideDish.setText("Side Dish: " + b.getString("side_dish"));
		quantity.setText(String.valueOf(b.getInt("qty")));

		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Integer.valueOf(quantity.getText().toString()) > 0) {
					editOrder();
				} else {
					toastMessage("Invalid input");
				}
			}
		});

		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msgBox();
			}
		});

		mToolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
	}

	private void editOrder() {
		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.EDIT_ORDER).setBodyParameter("id", String.valueOf(b.getInt("id")))
				.setBodyParameter("qty", quantity.getText().toString()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						// TODO Auto-generated method stub
						if (result == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						JsonParser parser = new JsonParser();
						JsonObject json = parser.parse(result).getAsJsonObject();
						if (json.get("status").getAsString().equalsIgnoreCase("error")) {
							toastMessage(json.get("message").getAsString());
							hideProgressDialog();
							return;
						}
						toastMessage(json.get("message").getAsString());
						hideProgressDialog();
						finishActivity();
					}
				});
	}

	private void msgBox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage("Are you sure?");
		builder.setIcon(null);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				showProgressDialog("Loading...");
				Ion.with(getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.DELETE_ORDER)
						.setBodyParameter("id", String.valueOf(b.getInt("id"))).asString()
						.setCallback(new FutureCallback<String>() {
							@Override
							public void onCompleted(Exception e, String result) {
								// TODO Auto-generated method stub
								if (result == null) {
									toastMessage("Network error");
									hideProgressDialog();
									return;
								}
								JsonParser parser = new JsonParser();
								JsonObject json = parser.parse(result).getAsJsonObject();
								if (json.get("status").getAsString().equalsIgnoreCase("error")) {
									toastMessage(json.get("message").getAsString());
									hideProgressDialog();
									return;
								}
								hideProgressDialog();
								toastMessage("Order deleted");
								finish();
							}
						});
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = builder.create();
		View view = getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("DELETE");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		getMenuInflater().inflate(R.menu.edit_order, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
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
	private void finishActivity(){
		Intent mainActivity = new Intent(EditOrderActivity.this, MainActivity.class);
		mainActivity.putExtra("tag", "my-order");
		startActivity(mainActivity);
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finishActivity();
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
}

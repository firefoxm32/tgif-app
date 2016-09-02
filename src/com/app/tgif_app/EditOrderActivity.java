package com.app.tgif_app;

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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditOrderActivity extends AppCompatActivity {
	private static Toolbar mToolbar;
	private ImageView image;
	private TextView menuName;
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
		setContentView(R.layout.activity_edit_order);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back2);
		mToolbar.setTitle("Edit Order");
		b = getIntent().getExtras();
		if (b == null) {
			Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
			return;
		}
		image = (ImageView) findViewById(R.id.editOrderImage);
		menuName = (TextView) findViewById(R.id.editOrderFoodName);
		serving = (TextView) findViewById(R.id.editOrderServing);
		sauces = (TextView) findViewById(R.id.editOrderSauce);
		sideDish = (TextView) findViewById(R.id.editOrderSideDish);
		quantity = (EditText) findViewById(R.id.editOrderQty);
		btnEdit = (Button) findViewById(R.id.editOrderbtnEdit);
		btnDelete = (Button) findViewById(R.id.editOrderbtnDelete);

		image.setImageResource(R.drawable.traditional_wings);
		System.out.println("id:" + b.getInt("id"));
		if (b.getString("menu_name").equals("")) {
			menuName.setVisibility(View.GONE);
		}
		menuName.setText("Menu Name: " + b.getString("menu_name"));
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
					Toast.makeText(getContext(), "Quantity is 0 or less than 0", Toast.LENGTH_SHORT).show();
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
				System.out.println("back!!");
				finish();
			}
		});
	}

	private void editOrder() {
		pDialog = new ProgressDialog(getContext());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
		Ion.with(getContext()).load(EndPoints.EDIT_ORDER).progress(new ProgressCallback() {
			@Override
			public void onProgress(long arg0, long arg1) {
				// TODO Auto-generated method stub
				System.out.println("On Que");
			}
		}).setBodyParameter("id", String.valueOf(b.getInt("id"))).setBodyParameter("qty", quantity.getText().toString())
				.asString().setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						// TODO Auto-generated method stub
						JsonParser parser = new JsonParser();
						JsonObject json = parser.parse(result).getAsJsonObject();
						if (json.get("status").getAsString().equalsIgnoreCase("error")) {
							Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(),
									Toast.LENGTH_SHORT).show();
							pDialog.dismiss();
							return;
						}
						Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT)
								.show();
						pDialog.dismiss();
						finish();
					}
				});
	}

	private void msgBox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Delete Order");
		builder.setMessage("Are you sure?");
		builder.setIcon(null);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				pDialog = new ProgressDialog(getContext());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Loading.... Please wait...");
				pDialog.setIndeterminate(true);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.show();
				Ion.with(getContext()).load(EndPoints.DELETE_ORDER).progress(new ProgressCallback() {
					@Override
					public void onProgress(long arg0, long arg1) {
						// TODO Auto-generated method stub
						System.out.println("On Que");
					}
				}).setBodyParameter("id", String.valueOf(b.getInt("id"))).asString()
						.setCallback(new FutureCallback<String>() {
							@Override
							public void onCompleted(Exception e, String result) {
								// TODO Auto-generated method stub
								JsonParser parser = new JsonParser();
								JsonObject json = parser.parse(result).getAsJsonObject();
								if (json.get("status").getAsString().equalsIgnoreCase("error")) {
									Toast.makeText(getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT)
											.show();
									pDialog.dismiss();
									return;
								}
								pDialog.dismiss();
								Toast.makeText(getContext(), "Order deleted", Toast.LENGTH_SHORT).show();
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
		AlertDialog dialog = builder.create();
		dialog.show();
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}

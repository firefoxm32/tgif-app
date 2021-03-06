package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;
import model.Order;
import model.Sauce;
import model.Serving;
import model.Session;
import model.SideDish;

public class OrderDetails extends Fragment {

	protected Session session;

	private CheckBox cb[];
	private RadioButton rdb[];
	private EditText qty;
	private RadioButton rdbSD[];
	private RadioGroup rg;
	private LinearLayout lSauce;
	private RadioGroup rgSD;

	private List<Serving> servings;
	private List<Sauce> sauces;
	private List<SideDish> sideDishes;

	private TextView textViewServing;
	private TextView textViewSauce;
	private TextView textViewSideDish;

	private List<Integer> servingId;
	private List<Integer> sauceId;
	private List<Integer> sideDishId;

	private TextView tvItemName;
	private TextView description;
	private Button btnAdd;
	private int itemId;
	private ProgressDialog pDialog;
	private ImageView image;
	private CheckBox orderType;
	private String strOrderType;

	public static Fragment newInstance(Context context) {
		OrderDetails orderDetails = new OrderDetails();
		return orderDetails;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	private ViewGroup rootView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// ViewGroup rootView = (ViewGroup)
		// inflater.inflate(R.layout.fragment_order_details, null);
		if(MainActivity.orderDetailsBundle.getBoolean("isNull")) {
			msg();
		} else {
		
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order_details, null);
		
		session = new Session(getContext());
		tvItemName = (TextView) rootView.findViewById(R.id.order_detail_item_name);
		description = (TextView) rootView.findViewById(R.id.order_details_description);
		btnAdd = (Button) rootView.findViewById(R.id.btn_add);
		image = (ImageView) rootView.findViewById(R.id.order_details_image);

		rg = (RadioGroup) rootView.findViewById(R.id.rd_group);
		qty = (EditText) rootView.findViewById(R.id.qty);
		lSauce = (LinearLayout) rootView.findViewById(R.id.l_sauce);
		rgSD = (RadioGroup) rootView.findViewById(R.id.rd_group_sd);

		textViewServing = (TextView) rootView.findViewById(R.id.servings);
		textViewSauce = (TextView) rootView.findViewById(R.id.sauce);
		textViewSideDish = (TextView) rootView.findViewById(R.id.side_dish);
		orderType = (CheckBox) rootView.findViewById(R.id.takeout);

		textViewServing.setVisibility(View.GONE);
		textViewSauce.setVisibility(View.GONE);
		textViewSideDish.setVisibility(View.GONE);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		Bundle bundle = MainActivity.orderDetailsBundle.getBundle("order_details");
		
/*		String itemName = getArguments().getString("item_name");
		String itemImage = getArguments().getString("image");
		itemId = getArguments().getInt("item_id");*/
		String itemName = bundle.getString("item_name");
		String itemImage = bundle.getString("image");
		itemId = bundle.getInt("item_id");
		strOrderType = "DI";
		orderType();
		tvItemName.setText(itemName);
		Picasso.with(MainActivity.getContext())
				.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
						+ itemName.replace(" ", "%20").toLowerCase() + "/" + itemImage)
				.error(R.drawable.not_found).fit().centerInside().into(image);
		orderDetails(itemId);
		qty.requestFocus();

		addOrder();
		}
		return rootView;
	}

	private void addOrder() {
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validation();
			}

		});
	}

	private void orderType() {
		orderType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (orderType.isChecked()) {
					strOrderType = "TO";
				} else {
					strOrderType = "DI";
				}
			}
		});
	}

	private void orderDetails(int param) {
		showProgressDialog("Loading...");
		Ion.with(MainActivity.getContext())
				.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.ORDER_DETAILS + "?param=" + param)
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							hideProgressDialog();
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						FoodItem foodItem = fmd.getOrderDetails(json);
						description.setText(foodItem.getDescription());

						servings = foodItem.getServings();

						if (servings.size() > 0) {
							textViewServing.setVisibility(View.VISIBLE);
							rdb = new RadioButton[servings.size()];
							servingId = new ArrayList<>();
							for (int i = 0; i < servings.size(); i++) {
								Serving serving = servings.get(i);
								rdb[i] = new RadioButton(getActivity());
								rg.addView(rdb[i]);
								rdb[i].setText(
										serving.getServingName() + " Php " + serving.getServingPrice().getPrice());
								rdb[i].setTextSize(23f);
								servingId.add(serving.getServingId());
							}
							rdb[0].setChecked(true);
						}

						sauces = foodItem.getSauces();
						if (sauces.size() > 0) {
							textViewSauce.setVisibility(View.VISIBLE);
							sauceId = new ArrayList<>();
							cb = new CheckBox[sauces.size()];
							for (int i = 0; i < sauces.size(); i++) {
								Sauce sauce = sauces.get(i);
								cb[i] = new CheckBox(getActivity());
								cb[i].setText(sauce.getSauceName());
								cb[i].setTextSize(23f);
								sauceId.add(sauce.getSauceId());
								lSauce.addView(cb[i]);
							}
						}

						sideDishes = foodItem.getSideDishes();
						if (sideDishes.size() > 0) {
							textViewSideDish.setVisibility(View.VISIBLE);
							sideDishId = new ArrayList<>();
							rdbSD = new RadioButton[sideDishes.size()];
							for (int i = 0; i < sideDishes.size(); i++) {
								SideDish sideDish = sideDishes.get(i);
								sideDish.displayData();
								rdbSD[i] = new RadioButton(getActivity());
								rdbSD[i].setText(sideDish.getSideDishName());
								rdbSD[i].setTextSize(23f);
								sideDishId.add(sideDish.getSideDishId());
								rgSD.addView(rdbSD[i]);
							}
						}
						hideProgressDialog();
					}
				});
	}
	
	private void msg() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_instruction, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		TextView message = (TextView) dialogView.findViewById(R.id.msg);
		message.setText("Please select item in HOME or MENU.");
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				MainActivity.mTabHost.setCurrentTab(0);
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("TGIF APP");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}
	
	private void validation() {
		Order order = new Order();
		int servingCtr = 0;
		int sauceCtr = 0;
		int sideDishCtr = 0;

		for (int i = 0; i < servings.size(); i++) {
			if (rdb[i].isChecked()) {
				servingCtr++;
				order.setServingId(servingId.get(i));
			}
		}
		String strSauce = "";
		if (sauces.size() > 0) {
			for (int i = 0; i < sauces.size(); i++) {
				if (cb[i].isChecked()) {
					sauceCtr++;
					strSauce += sauceId.get(i) + ",";
				}
			}
		}
		if (strSauce != "") {
			order.setSauce(strSauce.substring(0, strSauce.length() - 1));
		}

		int x = session.getTableNumber();
		order.setTableNumber(x);
		if (sideDishes.size() > 0) {
			for (int i = 0; i < sideDishes.size(); i++) {
				if (rdbSD[i].isChecked()) {
					sideDishCtr++;
					order.setSideDishId(sideDishId.get(i));
				}
			}
		}

		if (servingCtr == 0) {
			toastMessage("Select at least 1 serving");
		} else if (0 == sauceCtr && sauces.size() > 0) {
			toastMessage("Select at least 1 sauce");
		} else if (0 == sideDishCtr && sideDishes.size() != 0) {
			toastMessage("Select at least 1 side dish");
		} else if (qty.getText().toString().isEmpty()) {
			toastMessage("Input quantity");
			qty.setFocusable(true);
		} else if (((Integer.valueOf(qty.getText().toString()) == 0)
				|| (Integer.valueOf(qty.getText().toString()) > 10))) {
			toastMessage("Enter only 1 - 10 quantity.");
			qty.setFocusable(true);
		} else {
			order.setQty(Integer.valueOf(qty.getText().toString()));
			order.setItemId(itemId);
			order.setOrderType(strOrderType);
			System.out.println("TYPE: " + order.getOrderType());
			addOrder(order);
		}
	}

	private void addOrder(Order order) {
		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.ADD_ORDER)
				.setBodyParameter("table_number", String.valueOf(order.getTableNumber()))
				.setBodyParameter("transaction_id", session.getTransactionId())
				.setBodyParameter("item_id", String.valueOf(order.getItemId()))
				.setBodyParameter("serving_id", String.valueOf(order.getServingId()))
				.setBodyParameter("sauces", order.getSauce())
				.setBodyParameter("side_dish_id", String.valueOf(order.getSideDishId()))
				.setBodyParameter("qty", String.valueOf(order.getQty()))
				.setBodyParameter("order_type", order.getOrderType()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						JsonParser parser = new JsonParser();
						JsonObject json = parser.parse(response).getAsJsonObject();
						if (json == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						if (json.get("status").getAsString().equalsIgnoreCase("error")) {
							toastMessage(json.get("message").getAsString());
							hideProgressDialog();
							return;
						}
						toastMessage(json.get("message").getAsString());
						clearData();
						hideProgressDialog();
						hideSoftKeyboard();

						MainActivity.subFoodMenuBundle.putBoolean("isNull", true);
						MainActivity.orderDetailsBundle.putBoolean("isNull", true);
						MainActivity.mTabHost.setCurrentTab(1);
						System.out.println("ADAPTER: "+SubFoodMenuFragment.subFoodMenuList);
						if (SubFoodMenuFragment.subFoodMenuList != null) {
							SubFoodMenuFragment.subFoodMenuList.setAdapter(null);
						}
						rootView = null;
					}
				});
	}

	private void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	}

	private void clearData() {
		if (servings.size() > 0) {
			rdb[0].setChecked(true);
		}
		if (sauces.size() > 0) {
			for (int i = 0; i < sauces.size(); i++) {
				cb[i].setChecked(false);
			}
		}
		if (sideDishes.size() > 0) {
			for (int i = 0; i < sideDishes.size(); i++) {
				rdbSD[i].setChecked(false);
			}
		}
		qty.setText("");
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
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast_layout, null);
		TextView msg = (TextView) layout.findViewById(R.id.toast_message);
		msg.setText(message);
		Toast toast = new Toast(getContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
}

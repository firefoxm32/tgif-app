package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

	private Button btnAdd;
	private int itemId;
	private ProgressDialog pDialog;
	private ImageView image;

	public static Fragment newInstance(Context context) {
		OrderDetails orderDetails = new OrderDetails();
		return orderDetails;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// ViewGroup rootView = (ViewGroup)
		// inflater.inflate(R.layout.fragment_order_details, null);
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order_details, container);
		session = new Session(getContext());
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		image = (ImageView) rootView.findViewById(R.id.orderDetailsImage);

		rg = (RadioGroup) rootView.findViewById(R.id.rdbGroup);
		qty = (EditText) rootView.findViewById(R.id.qty);
		lSauce = (LinearLayout) rootView.findViewById(R.id.lSauce);
		rgSD = (RadioGroup) rootView.findViewById(R.id.rdbGroupSD);

		textViewServing = (TextView) rootView.findViewById(R.id.servings);
		textViewSauce = (TextView) rootView.findViewById(R.id.sauce);
		textViewSideDish = (TextView) rootView.findViewById(R.id.sideDish);

		textViewServing.setVisibility(View.GONE);
		textViewSauce.setVisibility(View.GONE);
		textViewSideDish.setVisibility(View.GONE);

		String itemName = getArguments().getString("menu_name");
		String itemImage = getArguments().getString("image");
		itemId = getArguments().getInt("item_id");
		MainActivity.mToolbar.setTitle(itemName);

		Picasso.with(MainActivity.getContext())
				.load(EndPoints.PICASSO + itemName.replace(" ", "%20").toLowerCase() + "/" + itemImage)
				.error(R.drawable.not_found).into(image);

		orderDetails(itemId);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validation();
			}

		});
		return rootView;
	}

	private void orderDetails(int param) {
		showProgressDialog();
		Ion.with(MainActivity.getContext()).load(EndPoints.ORDER_DETAILS + "?param=" + param)
				.progress(new ProgressCallback() {
					@Override
					public void onProgress(long arg0, long arg1) {
						// TODO Auto-generated method stub
						System.out.println("On Que");
					}
				}).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
						// itemId = foodItem.getItemId();
						servings = foodItem.getServings();

						if (servings.size() > 0) {
							rdb = new RadioButton[servings.size()];
							servingId = new ArrayList<>();
							for (int i = 0; i < servings.size(); i++) {
								Serving serving = servings.get(i);
								rdb[i] = new RadioButton(getActivity());
								rg.addView(rdb[i]);
								rdb[i].setText(
										serving.getServingName() + " Php " + serving.getServingPrice().getPrice());
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
								sideDishId.add(sideDish.getSideDishId());
								rgSD.addView(rdbSD[i]);
							}
						}
						hideProgressDialog();
					}
				});
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

		order.setSauce(strSauce.substring(0, strSauce.length() - 1));

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
			Toast.makeText(getActivity(), "Select at least 1 serving.", Toast.LENGTH_SHORT).show();
		} else if (0 == sauceCtr && sauces.size() != 0) {
			Toast.makeText(getActivity(), "Select at least 1 sauce.", Toast.LENGTH_SHORT).show();
		} else if (0 == sideDishCtr && sideDishes.size() != 0) {
			Toast.makeText(getActivity(), "Select at least 1 side dish.", Toast.LENGTH_SHORT).show();
		} else if (qty.getText().toString().isEmpty()) {
			Toast.makeText(getActivity(), "Input quantity.", Toast.LENGTH_SHORT).show();
			qty.setFocusable(true);
		} else if (!(Integer.valueOf(qty.getText().toString()) > 0)) {
			Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
			qty.setFocusable(true);
		} else {
			order.setQty(Integer.valueOf(qty.getText().toString()));
			order.setItemId(itemId);
			addOrder(order);
		}
	}

	private void addOrder(Order order) {
		showProgressDialog();
		Ion.with(getContext()).load(EndPoints.ADD_ORDER)
				.setBodyParameter("table_number", String.valueOf(order.getTableNumber()))
				.setBodyParameter("transaction_id", session.getTransactionId())
				.setBodyParameter("item_id", String.valueOf(order.getItemId()))
				.setBodyParameter("serving_id", String.valueOf(order.getServingId()))
				.setBodyParameter("sauces", order.getSauce())
				.setBodyParameter("side_dish_id", String.valueOf(order.getSideDishId()))
				.setBodyParameter("qty", String.valueOf(order.getQty())).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						JsonParser parser = new JsonParser();
						JsonObject json = parser.parse(response).getAsJsonObject();
						if (json == null) {
							Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						if (json.get("status").getAsString().equalsIgnoreCase("error")) {
							Toast.makeText(getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						Toast.makeText(getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
						clearData();
						hideProgressDialog();
					}
				});
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
}

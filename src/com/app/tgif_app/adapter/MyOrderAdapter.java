package com.app.tgif_app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.MainActivity;
import com.app.tgif_app.R;
import com.app.tgif_app.ServedOrder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.JsonAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class MyOrderAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Order> orders;
	private String tab;
//	private RatingBar rateMe;
	private Button btnRate;
	private EditText rateMe;
	protected Session session;
	private int pos;
	private List<Integer> rates;
	
	public MyOrderAdapter(Context context, String tabName, List<Order> orders) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.orders = orders;
		this.tab = tabName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orders.size();
	}

	@Override
	public Order getItem(int position) {
		// TODO Auto-generated method stub
		return orders.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
//	private View convertView;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.populate_my_order, null);
		}
		session = new Session(parent.getContext());
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.myOrderItemImage);
		TextView itemName = (TextView) convertView.findViewById(R.id.myOrderItemName);
		TextView serving = (TextView) convertView.findViewById(R.id.myOrderServing);
		TextView sauce = (TextView) convertView.findViewById(R.id.myOrderSauce);
		TextView sideDish = (TextView) convertView.findViewById(R.id.myOrderSideDish);
		TextView qty = (TextView) convertView.findViewById(R.id.myOrderQty);
		TextView tvRateMe = (TextView) convertView.findViewById(R.id.rateMe);
		rateMe = (EditText) convertView.findViewById(R.id.rate_me);
		btnRate = (Button) convertView.findViewById(R.id.btn_rate);
//		rateMe = (RatingBar) convertView.findViewById(R.id.rate_me);
//		rateMe.setOnRatingBarChangeListener(onRatingBarChangeListener(rateMe, position));
		
		Order order = orders.get(position);
		
		if (order.getFoodItem().getItemName().equals("")) {
			itemName.setVisibility(View.GONE);
		} else {
			itemName.setVisibility(View.VISIBLE);
			itemName.setText("Food Name: " + order.getFoodItem().getItemName());
		}

		if (order.getFoodItem().getServings().size() > 0) {
			for (int j = 0; j < order.getFoodItem().getServings().size(); j++) {
				if (!order.getFoodItem().getServings().get(j).getServingName().equals("")) {
					serving.setVisibility(View.VISIBLE);
					serving.setText("Serving: " + order.getFoodItem().getServings().get(j).getServingName());
				} else {
					serving.setVisibility(View.GONE);
				}

			}
		} else {
			serving.setVisibility(View.GONE);
		}
		String strSauce = "";
		if (order.getFoodItem().getSauces().size() > 0) {
			for (int j = 0; j < order.getFoodItem().getSauces().size(); j++) {
				strSauce += order.getFoodItem().getSauces().get(j).getSauceName() + ", ";
			}
			if (!strSauce.equals("")) {
				String subStrSauce = strSauce.substring(0, strSauce.length() - 2);
				sauce.setVisibility(View.VISIBLE);
				sauce.setText("Sauce/s: " + subStrSauce);
			} else {
				sauce.setVisibility(View.GONE);
			}
		} else {
			sauce.setVisibility(View.GONE);
		}
		if (order.getFoodItem().getSideDishes().size() > 0) {

			for (int j = 0; j < order.getFoodItem().getSideDishes().size(); j++) {
				if (!order.getFoodItem().getSideDishes().get(j).getSideDishName().equals("")) {
					sideDish.setVisibility(View.VISIBLE);
					sideDish.setText("Side Dish: " + order.getFoodItem().getSideDishes().get(j).getSideDishName());
				} else {
					sideDish.setVisibility(View.GONE);
				}

			}
		} else {
			sideDish.setVisibility(View.GONE);
		}
		qty.setText("Quantity: " + order.getQty());

		if (this.tab.equalsIgnoreCase("pending")) {
			tvRateMe.setVisibility(View.GONE);
			rateMe.setVisibility(View.GONE);
			btnRate.setVisibility(View.GONE);
		} else if (this.tab.equalsIgnoreCase("cooking")) {
			tvRateMe.setVisibility(View.GONE);
			rateMe.setVisibility(View.GONE);
			btnRate.setVisibility(View.GONE);
		} else {
			tvRateMe.setVisibility(View.VISIBLE);
			rateMe.setVisibility(View.VISIBLE);
			btnRate.setVisibility(View.VISIBLE);
		}
		ServedOrder.isRated = new boolean[getCount()];
		
		rates = new ArrayList<>();
		btnRate.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("ORDERR: "+getItem(position).getFoodItem().getItemId());
				System.out.println("ORDER: "+getItem(position).getFoodItem().getItemName());
				btnRate.setTag(position);
				for (int i = 0; i < getCount(); i++) {
					v = ServedOrder.myOrderListView.getChildAt(i);
					rateMe = (EditText) v.findViewById(R.id.rate_me);
					if (Integer.valueOf(btnRate.getTag().toString()) == i) {
						int r = Integer.valueOf(rateMe.getText().toString());
						if (r < 1 || r > 5) {
							toastMessage("Invalid input rate");
						} else {
							System.out.println("RATE: "+rateMe.getText().toString() + " " + i);
							ServedOrder.isRated[i] = true;
							setRating(position, r);
						}
					}
					if (ServedOrder.isRated[i]) {
						rateMe.setEnabled(false);
					}
				}
				
				hideSoftKeyboard();
			}
		});
		
		Picasso.with(MainActivity.getContext())
				.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
						+ order.getFoodItem().getItemName().replace(" ", "%20").toLowerCase() + "/"
						+ order.getFoodItem().getImage())
				.error(R.drawable.not_found).into(itemImage);
		return convertView;
	}
	
	private void setRating(int position, int rate) {
		Ion.with(context).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.RATING)
		.setBodyParameter("item_id", String.valueOf(getItem(position).getFoodItem().getItemId()))
		.setBodyParameter("rating", String.valueOf(rate)).asString()
		.setCallback(new FutureCallback<String>() {
			@Override
			public void onCompleted(Exception arg0, String response) {
				// TODO Auto-generated method stub
				if(response == null) {
					return;
				}
				JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
				System.out.println("MESSAGE: "+jsonObject.get("message").toString());
				toastMessage("RATED");
			}
		});
	}
	
	private void toastMessage(String message) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast_layout, null);
		TextView msg = (TextView) layout.findViewById(R.id.toast_message);
		msg.setText(message);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
	private void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) ((Activity)context)
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
	}
	
	class GetEdittextValue {
		public GetEdittextValue(int size) {
			btnRate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					System.out.println("RATE: "+rateMe.getText().toString());
				}
			});
		}
	}
}

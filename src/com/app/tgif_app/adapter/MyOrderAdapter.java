package com.app.tgif_app.adapter;

import java.util.List;

import com.app.tgif_app.MainActivity;
import com.app.tgif_app.R;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import model.Order;

public class MyOrderAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Order> orders;

	public MyOrderAdapter(Context context, List<Order> orders) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.orders = orders;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orders.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orders.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.populate_my_order, null);
		}
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.myOrderItemImage);
		TextView itemName = (TextView) convertView.findViewById(R.id.myOrderItemName);
		TextView serving = (TextView) convertView.findViewById(R.id.myOrderServing);
		TextView sauce = (TextView) convertView.findViewById(R.id.myOrderSauce);
		TextView sideDish = (TextView) convertView.findViewById(R.id.myOrderSideDish);
		TextView qty = (TextView) convertView.findViewById(R.id.myOrderQty);

		Order order = orders.get(position);
		Picasso.with(MainActivity.getContext())
				.load(EndPoints.PICASSO + order.getFoodItem().getMenuName().replace(" ", "%20").toLowerCase() + "/"
						+ order.getFoodItem().getImage())
				.error(R.drawable.not_found).into(itemImage);

		if (order.getFoodItem().getMenuName().equals("")) {
			itemName.setVisibility(View.GONE);
		} else {
			itemName.setVisibility(View.VISIBLE);
			itemName.setText("Food Name: " + order.getFoodItem().getMenuName());
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

		return convertView;
	}	
}

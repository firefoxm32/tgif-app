package com.app.tgif_app.adapter;

import java.util.List;

import com.app.tgif_app.MainActivity;
import com.app.tgif_app.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import model.FoodItem;
import model.Session;

public class SubFoodMenuAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FoodItem> foodMenuItems;
	protected Session session;

	public SubFoodMenuAdapter(Context context, List<FoodItem> foodMenuItems) {
		this.context = context;
		this.foodMenuItems = foodMenuItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return foodMenuItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return foodMenuItems.get(position);
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
			convertView = inflater.inflate(R.layout.populate_sub_food_menu, null);
		}
		session = new Session(parent.getContext());
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		TextView description = (TextView) convertView.findViewById(R.id.description);
		TextView category = (TextView) convertView.findViewById(R.id.menuLabel);
		category.setVisibility(View.GONE);
		FoodItem fmi = foodMenuItems.get(position);
		Picasso.with(MainActivity.getContext())
				.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
						+ fmi.getItemName().replace(" ", "%20").toLowerCase() + "/" + fmi.getImage())
				.error(R.drawable.not_found).fit().centerCrop().into(itemImage);
		// .into(itemImage);
		itemName.setText(fmi.getItemName());
		category.setText("Category: " + fmi.getFoodMenu().getMenuName());
		description.setText("Description: " + fmi.getDescription().replace("\"", ""));
		return convertView;
	}
}

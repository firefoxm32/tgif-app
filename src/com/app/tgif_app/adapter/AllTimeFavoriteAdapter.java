package com.app.tgif_app.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.app.tgif_app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import model.FoodItem;

public class AllTimeFavoriteAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FoodItem> foodMenuItems;
	public AllTimeFavoriteAdapter(Context context, List<FoodItem> foodMenuItems) {
		// TODO Auto-generated constructor stub
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
		
		if (convertView == null){
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.populate_sub_food_menu, null);
		}
		
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		TextView timesOrdered = (TextView) convertView.findViewById(R.id.timesOrdered);
		//Button btnAdd = (Button) convertView.findViewById(R.id.btnAdd);
		price.setVisibility(convertView.GONE);
		/*itemName.setVisibility(convertView.GONE);
		itemImage.setVisibility(convertView.GONE);*/
		timesOrdered.setVisibility(convertView.GONE);
		itemImage.setImageResource(R.drawable.traditional_wings);
		for (int i = 0; i < foodMenuItems.size(); i++) {
			FoodItem fmi = foodMenuItems.get(position);
			itemName.setText(fmi.getMenuName());
			timesOrdered.setText("Ordered "+String.valueOf(fmi.getOrderCtr()) + " times");
		}
		
		return convertView;
	}
}

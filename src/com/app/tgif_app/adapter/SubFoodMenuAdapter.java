package com.app.tgif_app.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.app.tgif_app.MainActivity;
import com.app.tgif_app.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import model.FoodItem;

public class SubFoodMenuAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FoodItem> foodMenuItems;
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
		
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		TextView timesOrdered = (TextView) convertView.findViewById(R.id.timesOrdered);
		/*itemImage.setVisibility(View.GONE);*/
		timesOrdered.setVisibility(View.GONE);
		price.setVisibility(View.GONE);
		itemImage.setImageResource(R.drawable.traditional_wings);
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		for (int i = 0; i < foodMenuItems.size(); i++) {
			FoodItem fmi = foodMenuItems.get(position);
			itemName.setText(fmi.getMenuName());
//			price.setText("Php " +formatter.format(fmi.getPrice()));
			timesOrdered.setText("Ordered "+String.valueOf(fmi.getOrderCtr()) + " times");
		}
		
		return convertView;
	}
}

package com.app.tgif_app.adapter;


import com.app.tgif_app.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {
	public TextView itemName;
	public TextView category;
	public TextView price;
	public ImageView image;
	
	public MyViewHolder(View view) {
		super(view);
		// TODO Auto-generated constructor stub
		itemName = (TextView) view.findViewById(R.id.promo_item_name);
		category = (TextView) view.findViewById(R.id.promo_category);
		price = (TextView) view.findViewById(R.id.promo_price);
		image = (ImageView) view.findViewById(R.id.promo_image);
		
		
	}
}

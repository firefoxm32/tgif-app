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
import model.FoodMenu;
import model.Session;

public class FoodMenuAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FoodMenu> foodMenus;
	protected Session session;
	public FoodMenuAdapter(Context context, List<FoodMenu> foodMenus) {
		this.context = context;
		this.foodMenus = foodMenus;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return foodMenus.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return foodMenus.get(position);
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
			convertView = inflater.inflate(R.layout.populate_food_menu, null);
		}
		session = new Session(parent.getContext());
		
		ImageView menuImage = (ImageView) convertView.findViewById(R.id.foodMenuImage);
		TextView menuName = (TextView) convertView.findViewById(R.id.menu_name);
		TextView qty = (TextView) convertView.findViewById(R.id.qty);

		FoodMenu fm = foodMenus.get(position);
		System.out.println("PIC: "+EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
				+ fm.getMenuName().replace(" ", "%20").toLowerCase() + "/" + fm.getMenuImage());
		menuName.setText(fm.getMenuName());
		qty.setText(fm.getQty() + " Choices");
		Picasso.with(parent.getContext())
		.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
				+ fm.getMenuName().replace(" ", "%20").toLowerCase() + "/" + fm.getMenuImage())
		.error(R.drawable.not_found).fit().centerCrop().into(menuImage);
		return convertView;
	}
}

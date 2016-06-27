package com.app.tgif_app.adapter;

import java.util.List;

import com.app.tgif_app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import model.DrawerItem;

public class DrawerAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<DrawerItem> drawerItems;
	public DrawerAdapter(Context context, List<DrawerItem>drawerItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.drawerItems = drawerItems;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawerItems.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return drawerItems.get(position);
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
			convertView = inflater.inflate(R.layout.populate_drawer, null);
		}
		
		
		ImageView menuImage = (ImageView) convertView.findViewById(R.id.drawerImage);
		TextView menuName = (TextView) convertView.findViewById(R.id.drawerMenu);
		
		menuImage.setImageResource(R.drawable.traditional_wings);
		for (int i = 0; i < drawerItems.size(); i++) {
			DrawerItem DI = drawerItems.get(position)
					;
			menuName.setText(DI.getDrawerMenus());
		}
		
		return convertView;
	}
}

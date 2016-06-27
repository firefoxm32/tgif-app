package com.app.tgif_app;

import com.tgif.dao.FoodMenuDAO;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditOrderFragment extends Fragment {
	
	private ImageView image;
	private TextView menuName;
	private TextView serving;
	private TextView sauces;
	private TextView sideDish;
	private EditText qty;
	private Button btnEdit;
	private Button btnDelete;
	
	public static Fragment newInstance(Context context){
		EditOrderFragment editOrderFragment = new EditOrderFragment();
		return editOrderFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_order, null);
		MainActivity.mToolbar.setTitle("Edit Order");
		image = (ImageView) rootView.findViewById(R.id.editOrderImage);
		menuName = (TextView) rootView.findViewById(R.id.editOrderFoodName);
		serving = (TextView) rootView.findViewById(R.id.editOrderServing);
		sauces = (TextView) rootView.findViewById(R.id.editOrderSauce);
		sideDish = (TextView) rootView.findViewById(R.id.editOrderSideDish);
		qty = (EditText) rootView.findViewById(R.id.editOrderQty);
		btnEdit = (Button) rootView.findViewById(R.id.editOrderbtnEdit);
		btnDelete = (Button) rootView.findViewById(R.id.editOrderbtnDelete);
		
		image.setImageResource(R.drawable.traditional_wings);
		System.out.println("id:"+getArguments().getInt("id"));
		if (getArguments().getString("menu_name").equals("")) {
			menuName.setVisibility(View.GONE);
		}
		menuName.setText("Menu Name: "+getArguments().getString("menu_name"));
		if (getArguments().getString("serving").equals("")) {
			serving.setVisibility(View.GONE);
		}
		serving.setText("Serving: " + getArguments().getString("serving"));
		if (getArguments().getString("sauces").equals("")) {
			sauces.setVisibility(View.GONE);
		}
		sauces.setText("Sauce/s: "+getArguments().getString("sauces"));
		if (getArguments().getString("side_dish").equals("")) {
			sideDish.setVisibility(View.GONE);
		}
		sideDish.setText("Side Dish: "+getArguments().getString("side_dish"));
		qty.setText(String.valueOf(getArguments().getInt("qty")));
		
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FoodMenuDAO fmd = new FoodMenuDAO();
				fmd.editOrder(qty.getText().toString(), String.valueOf(getArguments().getInt("id")));
			}
		});
		
		return rootView;
	}
}

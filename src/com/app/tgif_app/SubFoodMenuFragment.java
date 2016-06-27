package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.SubFoodMenuAdapter;
import com.tgif.dao.FoodMenuDAO;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import model.FoodItem;

public class SubFoodMenuFragment extends Fragment {
	
	private ListView subFoodMenuList;
	private SubFoodMenuAdapter subFoodMenuAdapter;
	private List<FoodItem> foodMenuItems;
	private List<String> itemName;
	public static Fragment newInstance(Context context){
		SubFoodMenuFragment subFoodMenuFragment = new SubFoodMenuFragment();
		return subFoodMenuFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sub_food_menu_fragment, null);
		subFoodMenuList = (ListView) rootView.findViewById(R.id.ListSubFoodMenu);
		FoodMenuDAO fmd = new FoodMenuDAO();
		itemName = new ArrayList<>();
		int menuId = getArguments().getInt("menuId");
		
		foodMenuItems = fmd.getFoodMenuItems(menuId);
		
		for(FoodItem fmi : foodMenuItems) {
			itemName.add(fmi.getMenuName());
		}
		for (int i = 0; i < itemName.size(); i++) {
			System.out.println("item_name1: "+itemName.get(i));
		}
		
		subFoodMenuAdapter = new SubFoodMenuAdapter(getActivity(), foodMenuItems);
		subFoodMenuList.setAdapter(subFoodMenuAdapter);
		
	
		subFoodMenuList.setOnItemClickListener(new OnItemClickListener() {
		
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Bundle orderBundle = new Bundle();
				Fragment orderFragment = new OrderFragment();
				
				orderBundle.putString("choice", itemName.get(position));
				orderFragment.setArguments(orderBundle);
				
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				
				fragmentTransaction.replace(R.id.container, orderFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		
		});
		
		return rootView;
				
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}

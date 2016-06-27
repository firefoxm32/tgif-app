package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.AllTimeFavoriteAdapter;
import com.tgif.dao.FoodMenuDAO;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import model.FoodItem;

public class Home extends Fragment {
	
	private ListView allTimeFavoriteList;
	private ListView promoList;
	private AllTimeFavoriteAdapter allTimeFavoriteAdapter;
	private List<FoodItem> foodMenuItems;
	public static Fragment newIntance(Context context) {
		Home home = new Home();
		return home;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		//return inflater.inflate(R.layout.food_menu_fragment, null, false);
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
		allTimeFavoriteList = (ListView) rootView.findViewById(R.id.allTimeFavoriteList);
		promoList = (ListView) rootView.findViewById(R.id.promoList);
		FoodMenuDAO fmd = new FoodMenuDAO();
		
		foodMenuItems = fmd.getAllTimeFavorites();
		allTimeFavoriteAdapter = new AllTimeFavoriteAdapter(getActivity(), foodMenuItems);
		
		allTimeFavoriteList.setAdapter(allTimeFavoriteAdapter);
		
		promoList.setAdapter(allTimeFavoriteAdapter);
		return rootView;
	}
	
}

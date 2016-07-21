package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.SubFoodMenuAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
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
	private ProgressDialog pDialog;
	public static Fragment newInstance(Context context){
		SubFoodMenuFragment subFoodMenuFragment = new SubFoodMenuFragment();
		return subFoodMenuFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sub_food_menu_fragment, null);
		subFoodMenuList = (ListView) rootView.findViewById(R.id.ListSubFoodMenu);
//		int menuId = getArguments().getInt("menuId");
		
		getFoodItems(getArguments().getInt("menuId"));
		
	
		subFoodMenuList.setOnItemClickListener(new OnItemClickListener() {
		
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){				
				Bundle odBundle = new Bundle();
				
				odBundle.putString("menu_name", foodMenuItems.get(position).getMenuName());
				odBundle.putInt("item_id", foodMenuItems.get(position).getItemId());
				
				Fragment orderDetails = new OrderDetails();
				orderDetails.setArguments(odBundle);
				
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.container, orderDetails);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		
		});
		
		return rootView;
				
	}
	
	private void getFoodItems(int menuId) {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
		Ion.with(MainActivity
		.getContext())
		.load(EndPoints.FOOD_MENU_ITEMS+"?params="+menuId)
		.progress(new ProgressCallback() {
			@Override
			public void onProgress(long arg0, long arg1) {
				// TODO Auto-generated method stub
				System.out.println("On Que");
			}
		})
		.asJsonObject()
		.setCallback(new FutureCallback<JsonObject>() {
			
			@Override
			public void onCompleted(Exception arg0, JsonObject json) {
				// TODO Auto-generated method stub
				FoodMenuDAO fmd = new FoodMenuDAO();
				itemName = new ArrayList<>();
		
				foodMenuItems = fmd.getFoodMenuItems(json);
				
				subFoodMenuAdapter = new SubFoodMenuAdapter(getActivity(), foodMenuItems);
				subFoodMenuList.setAdapter(subFoodMenuAdapter);
				pDialog.dismiss();
			}
		});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}

package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.AllTimeFavoriteAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
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
import android.widget.Toast;
import model.FoodItem;

public class Home extends Fragment {

	private ListView allTimeFavoriteList;
	private ListView promoList;
	private AllTimeFavoriteAdapter allTimeFavoriteAdapter;
	private List<FoodItem> foodMenuItems;
	private ProgressDialog pDialog;

	public static Fragment newIntance(Context context) {
		Home home = new Home();
		return home;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// return inflater.inflate(R.layout.food_menu_fragment, null, false);
		MainActivity.mToolbar.setTitle("Home");
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container);
		allTimeFavoriteList = (ListView) rootview.findViewById(R.id.allTimeFavoriteList);
		promoList = (ListView) rootview.findViewById(R.id.promoList);

		allTimeFavorites();

		allTimeFavoriteList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				orderDetails(position);
			}
		});

		return rootview;
	}

	private void allTimeFavorites() {
		showProgressDialog();
		Ion.with(MainActivity.getContext()).load(EndPoints.ALL_TIME_FAVORITES).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();

						foodMenuItems = fmd.getAllTimeFavorites(json);

						allTimeFavoriteAdapter = new AllTimeFavoriteAdapter(getActivity(), foodMenuItems);

						allTimeFavoriteList.setAdapter(allTimeFavoriteAdapter);
						hideProgressDialog();
					}
				});
	}

	private void orderDetails(int position) {
		Bundle odBundle = new Bundle();
		odBundle.putString("menu_name", foodMenuItems.get(position).getMenuName());
		odBundle.putInt("item_id", foodMenuItems.get(position).getItemId());
		odBundle.putString("image", foodMenuItems.get(position).getImage());
		Fragment orderDetails = new OrderDetails();
		orderDetails.setArguments(odBundle);

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container, orderDetails);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	private void showProgressDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCanceledOnTouchOutside(false);
		}
		pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
}

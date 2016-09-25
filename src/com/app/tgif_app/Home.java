package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.AllTimeFavoriteAdapter;
import com.app.tgif_app.adapter.AllTimeFavoriteAndPromoAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;

public class Home extends Fragment {

	private ListView allTimeFavoriteList;
	private AllTimeFavoriteAdapter allTimeFavoriteAdapter;
	private List<FoodItem> foodMenuItems;
	private ProgressDialog pDialog;
	private RecyclerView rv;
	private LinearLayoutManager llm;
	public static int pos;
	public static Fragment newIntance(Context context) {
		Home home = new Home();
		return home;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// return inflater.inflate(R.layout.food_menu_fragment, null, false);
//		MainActivity.mToolbar.setTitle("Home");
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
//		allTimeFavoriteList = (ListView) rootview.findViewById(R.id.allTimeFavoriteList);
		llm = new LinearLayoutManager(getContext());
		rv = (RecyclerView) rootview.findViewById(R.id.rv);
		rv.setHasFixedSize(true);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		allTimeFavorites();
	
		return rootview;
	}

	public void initList() {
//		listItems.clear();
//		for(int i = 0; i < itemName.length; i++) {
//		}
	}
	
	private void allTimeFavorites() {
		showProgressDialog();
		Ion.with(getContext()).load(EndPoints.ALL_TIME_FAVORITES).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();

						foodMenuItems = fmd.getAllTimeFavorites(json);
						
						rv.setAdapter(new AllTimeFavoriteAndPromoAdapter(foodMenuItems, getContext()));
						rv.setLayoutManager(llm);
						
						hideProgressDialog();
					}
				});
	}
	
	private void promos() {
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
	private void toastMessage(String message) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast_layout, null);
		TextView msg = (TextView) layout.findViewById(R.id.toast_message);
		msg.setText(message);
		Toast toast = new Toast(getContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}

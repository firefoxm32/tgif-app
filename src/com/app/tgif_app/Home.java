package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.AllTimeFavoriteAdapter;
import com.app.tgif_app.adapter.PromoAdapter;
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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;
import model.Session;

public class Home extends Fragment {
	protected Session session;
	private List<FoodItem> foodMenuItems;
	private ProgressDialog pDialog;
	private RecyclerView allTimeFavoritesRV;
	private LinearLayoutManager allTimeFavoriteLlm;
	private LinearLayoutManager promoLlm;
	private RecyclerView promoRV;

	public static int pos;

	public static Fragment newIntance(Context context) {
		Home home = new Home();
		return home;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// return inflater.inflate(R.layout.food_menu_fragment, null, false);
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
		session = new Session(getContext());
		MainActivity.mToolbar.setTitle("Home");
		allTimeFavoriteLlm = new LinearLayoutManager(getContext());
		allTimeFavoritesRV = (RecyclerView) rootview.findViewById(R.id.all_time_favorite_rv);
		allTimeFavoritesRV.setHasFixedSize(true);
		allTimeFavoriteLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
		allTimeFavorites();

		promoLlm = new LinearLayoutManager(getContext());
		promoRV = (RecyclerView) rootview.findViewById(R.id.promo_rv);
		promoRV.setHasFixedSize(true);
		promoLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
		promos();
		return rootview;
	}

	private void allTimeFavorites() {
//		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.ALL_TIME_FAVORITES)
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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

						allTimeFavoritesRV.setAdapter(new AllTimeFavoriteAdapter(foodMenuItems, getContext()));
						allTimeFavoritesRV.setLayoutManager(allTimeFavoriteLlm);

//						hideProgressDialog();
					}
				});
	}

	private void promos() {
//		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.PROMOS).asJsonObject()
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

						foodMenuItems = fmd.getPromos(json);

						promoRV.setAdapter(new PromoAdapter(foodMenuItems, getContext()));
						promoRV.setLayoutManager(promoLlm);

//						hideProgressDialog();
					}
				});
	}

	private void showProgressDialog(String message) {
		if (pDialog == null) {
			pDialog = new ProgressDialog(getContext());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setIndeterminate(true);
			pDialog.setCanceledOnTouchOutside(false);
			SpannableString ss1 = new SpannableString(message);
			ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
			ss1.setSpan(new ForegroundColorSpan(R.color.black), 0, ss1.length(), 0);
			pDialog.setMessage(ss1);
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

package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.SubFoodMenuAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;
import model.Session;

public class SubFoodMenuFragment extends Fragment {

	public static ListView subFoodMenuList;
	private SubFoodMenuAdapter subFoodMenuAdapter;
	private List<FoodItem> foodMenuItems;
	private ProgressDialog pDialog;
	protected Session session;
	private ViewGroup rootView;

	public static Fragment newInstance(Context context) {
		SubFoodMenuFragment subFoodMenuFragment = new SubFoodMenuFragment();
		return subFoodMenuFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

		if (MainActivity.subFoodMenuBundle.getBoolean("isNull")) {
			msg();
		} else {
			rootView = (ViewGroup) inflater.inflate(R.layout.sub_food_menu_fragment, null);

			subFoodMenuList = (ListView) rootView.findViewById(R.id.ListSubFoodMenu);
			session = new Session(getContext());
			// MainActivity.mToolbar.setTitle(getArguments().getString("choice"));
			// getFoodItems(getArguments().getInt("menuId"));
			System.out.println("MENU ID: " + MainActivity.subFoodMenuBundle.getInt("menu_id"));
			getFoodItems(MainActivity.subFoodMenuBundle.getInt("menu_id"));

			subFoodMenuList.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					orderDetails(position);
				}

			});
		}
		return rootView;
	}

	private void msg() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_instruction, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		TextView message = (TextView) dialogView.findViewById(R.id.msg);
		message.setText("Please select category in MENU tab.");
		alertBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				MainActivity.mTabHost.setCurrentTab(1);
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("TGIF APP");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}

	private void getFoodItems(int menuId) {
		showProgressDialog("Loading...");
		Ion.with(MainActivity.getContext())
				.load(EndPoints.HTTP + session.getIpAddress() + EndPoints.FOOD_MENU_ITEMS + "?params=" + menuId)
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						foodMenuItems = fmd.getFoodMenuItems(json);

						subFoodMenuAdapter = new SubFoodMenuAdapter(getContext(), foodMenuItems);
						subFoodMenuList.setAdapter(subFoodMenuAdapter);
						hideProgressDialog();
					}
				});
	}

	private void orderDetails(int position) {
		Bundle odBundle = new Bundle();

		odBundle.putString("item_name", foodMenuItems.get(position).getItemName());
		odBundle.putString("image", foodMenuItems.get(position).getImage());
		odBundle.putInt("item_id", foodMenuItems.get(position).getItemId());

		MainActivity.orderDetailsBundle.putBoolean("isNull", false);
		MainActivity.orderDetailsBundle.putBundle("order_details", odBundle);
		MainActivity.mTabHost.setCurrentTab(3);
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

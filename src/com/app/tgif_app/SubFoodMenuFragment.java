package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.SubFoodMenuAdapter;
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
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;
import model.Session;

public class SubFoodMenuFragment extends Fragment {

	private ListView subFoodMenuList;
	private SubFoodMenuAdapter subFoodMenuAdapter;
	private List<FoodItem> foodMenuItems;
	private ProgressDialog pDialog;
	protected Session session;

	public static Fragment newInstance(Context context) {
		SubFoodMenuFragment subFoodMenuFragment = new SubFoodMenuFragment();
		return subFoodMenuFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sub_food_menu_fragment, null);
		subFoodMenuList = (ListView) rootView.findViewById(R.id.ListSubFoodMenu);
		MainActivity.mToolbar.setTitle(getArguments().getString("choice"));
		getFoodItems(getArguments().getInt("menuId"));

		subFoodMenuList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				orderDetails(position);
			}

		});
		return rootView;
	}
	
	private void getFoodItems(int menuId) {
		showProgressDialog();
		Ion.with(MainActivity.getContext()).load(EndPoints.FOOD_MENU_ITEMS + "?params=" + menuId).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if(json == null) {
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

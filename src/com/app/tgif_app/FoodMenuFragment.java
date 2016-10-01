package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.FoodMenuAdapter;
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
import model.FoodMenu;
import model.Session;

public class FoodMenuFragment extends Fragment {
	protected Session session;
	private ListView FoodMenuList;
	private List<String> menuName;
	private FoodMenuAdapter foodMenuAdapter;
	private List<FoodMenu> list;
	private ProgressDialog pDialog;

	public static Fragment newInstance(Context context) {
		FoodMenuFragment foodMenuFragment = new FoodMenuFragment();
		return foodMenuFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		// return inflater.inflate(R.layout.food_menu_fragment, null, false);
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.food_menu_fragment, null);
		MainActivity.mToolbar.setTitle("Food Menus");
		session = new Session(getContext());
		FoodMenuList = (ListView) rootView.findViewById(R.id.list_menu);

		getMenus();

		FoodMenuList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				foodItems(position);
			}

		});
		return rootView;
	}

	private void getMenus() {
		showProgressDialog("Loading...");
		Ion.with(MainActivity.getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.FOOD_MENUS).asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							toastMessage("Network error");
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						list = fmd.getFoodMenus(json);
						menuName = new ArrayList<>();
						for (FoodMenu fm : list) {
							menuName.add(fm.getMenuName());
						}

						foodMenuAdapter = new FoodMenuAdapter(getActivity(), list);

						FoodMenuList.setAdapter(foodMenuAdapter);
						hideProgressDialog();
					}
				});
	}

	private void foodItems(int position) {
		Bundle sfmfbundle = new Bundle();

		// SubFoodMenuFragment SFMF = new SubFoodMenuFragment();
		Fragment mfragment = new SubFoodMenuFragment();
		String choice = menuName.get(position);

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		sfmfbundle.putInt("menuId", position + 1);
		sfmfbundle.putString("choice", choice);

		mfragment.setArguments(sfmfbundle);
		fragmentTransaction.replace(R.id.container, mfragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
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

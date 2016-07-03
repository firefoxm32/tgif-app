package com.app.tgif_app;


import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.FoodMenuAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import model.FoodMenu;
public class FoodMenuFragment extends Fragment {
	private ListView FoodMenuList;
	private List<String> menuName;
	private FoodMenuAdapter foodMenuAdapter;
	private List<FoodMenu> list;
	private ProgressDialog pDialog;
	public static Fragment newInstance(Context context){
			FoodMenuFragment foodMenuFragment = new FoodMenuFragment();
			return foodMenuFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		//return inflater.inflate(R.layout.food_menu_fragment, null, false);
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.food_menu_fragment, null);
		
		FoodMenuList = (ListView) rootView.findViewById(R.id.listMenu);
		
		getMenus();
		
		FoodMenuList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id){

					Bundle sfmfbundle = new Bundle();
					
					//SubFoodMenuFragment SFMF = new SubFoodMenuFragment();
					Fragment mfragment = new SubFoodMenuFragment();
					String choice = menuName.get(position);
					
					FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
					
					sfmfbundle.putInt("menuId", position + 1);
//					System.out.println("putInt");
					sfmfbundle.putString("choice", choice);
//					System.out.println("putString");
//					System.out.println(choice); 	
						
					mfragment.setArguments(sfmfbundle);
//					System.out.println("setArguments");
					fragmentTransaction.replace(R.id.container, mfragment);
//					System.out.println("replace");
					fragmentTransaction.addToBackStack(null);
//					System.out.println("addBackstack");
					fragmentTransaction.commit();
				}
				
		});
		
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	private void getMenus() {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
		Ion.with(MainActivity.getContext())
		.load(EndPoints.FOOD_MENUS)
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
			public void onCompleted(Exception e, JsonObject json) {
				// TODO Auto-generated method stub
				FoodMenuDAO fmd = new FoodMenuDAO();
				list = fmd.getFoodMenus(json);
				menuName = new ArrayList<>();
				for(FoodMenu fm : list) {
					menuName.add(fm.getLabel());
				}
				System.out.println("Menu Name: "+menuName);
			
				foodMenuAdapter = new FoodMenuAdapter(getActivity(), list);

				FoodMenuList.setAdapter(foodMenuAdapter);
				pDialog.dismiss();
			}
		});
	}
}










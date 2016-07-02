package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.MyOrderAdapter;
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
import android.widget.Button;
import android.widget.ListView;
import model.Order;

public class MyOrderFragment extends Fragment {
	private ListView myOrderListView;
	private Button btnSend;
	private MyOrderAdapter myOrderAdapter;
	private List<Order> orders;
	private ProgressDialog pDialog;
	public static Fragment newInstance(Context context){
		MyOrderFragment myOrderFragment = new MyOrderFragment();
		return myOrderFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		//return inflater.inflate(R.layout.food_menu_fragment, null, false);
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, null);
	
		myOrderListView = (ListView) rootView.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootView.findViewById(R.id.btnSendOrders);
		btnSend.setVisibility(View.INVISIBLE);
		System.out.println("Table Number: "+getString(R.string.table_number));
		System.out.println("Yes: "+getString(R.string.yes));
		myOrder(Integer.valueOf(getString(R.string.table_number)));
		
		myOrderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TO DO Auto-generated method stub
				Order order = orders.get(position);
				String menuName="";
				String serving="";
				String sauces="";
				String sideDish="";
				int qty;
				System.out.println("menu_name: "+order.getFoodItem().getMenuName());
				menuName = menuName + order.getFoodItem().getMenuName();
				if(order.getFoodItem().getServings().size() > 0) {
					for (int i = 0; i < order.getFoodItem().getServings().size(); i++) {
						System.out.println("serving: "+order.getFoodItem().getServings().get(i).getServingName());
						serving = serving + order.getFoodItem().getServings().get(i).getServingName();
					}
				}
				if(order.getFoodItem().getSauces().size() > 0) {
					for (int i = 0; i < order.getFoodItem().getSauces().size(); i++) {
						System.out.println("sauces " + i + " : " +order.getFoodItem().getSauces().get(i).getSauceName());
						sauces += order.getFoodItem().getSauces().get(i).getSauceName() + ", ";
					}
				}
				System.out.println("side_dish: "+order.getFoodItem().getSideDishes().size());
				if (order.getFoodItem().getSideDishes().size() > 0) {
					for (int i = 0; i < order.getFoodItem().getSideDishes().size(); i++) {
						System.out.println("side_dish: "+order.getFoodItem().getSideDishes().get(i).getSideDishName());
						sideDish = sideDish + order.getFoodItem().getSideDishes().get(i).getSideDishName();
					}
				}
				System.out.println("Quantity: "+order.getQty());
				qty = order.getQty();
				
				Bundle odBundle = new Bundle();
				
				odBundle.putString("menu_name", menuName);
				odBundle.putString("serving", serving);
				odBundle.putString("side_dish", sideDish);
				odBundle.putInt("qty", qty);
				odBundle.putInt("id", order.getId());
				odBundle.putString("sauces", sauces.substring(0, sauces.length() - 2));
				
				Fragment editOrderFragment = new EditOrderFragment();
				editOrderFragment.setArguments(odBundle);
				
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.container, editOrderFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		return rootView;
	}
	public void myOrder(int tableNumber) {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
		Ion.with(MainActivity.getContext())
		.load(EndPoints.MY_ORDERS+"?table_number="+tableNumber)
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
				orders = fmd.getMyOrders(json);
				myOrderAdapter = new MyOrderAdapter(getActivity(), orders);
				myOrderListView.setAdapter(myOrderAdapter);
				System.out.println("Complete");
				pDialog.dismiss();
				btnSend.setVisibility(View.VISIBLE);
			}
		});
		
	}
}

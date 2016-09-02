package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.MyOrderAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class PendingOrder extends Fragment {
	private ListView myOrderListView;
	private Button btnSend;
	private MyOrderAdapter myOrderAdapter;
	private List<Order> orders;
	private ProgressDialog pDialog;
	protected Session session;

	public static Fragment newInstance(Context context) {
		PendingOrder pendingOrder = new PendingOrder();
		return pendingOrder;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, container);
		session = new Session(getContext());
		myOrderListView = (ListView) rootView.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootView.findViewById(R.id.btnSendOrders);
		btnSend.setVisibility(View.GONE);

		myOrder(session.getTransactionId());

		myOrderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TO DO Auto-generated method stub
				editOrder(position);
			}
		});
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendOrders(session.getTransactionId());
			}
		});
		return rootView;
	}

	private void myOrder(String transactionId) {
		showProgressDialog();
		Ion.with(MainActivity.getContext()).load(EndPoints.MY_ORDERS + "?transaction_id=" + transactionId)
				.progress(new ProgressCallback() {
					@Override
					public void onProgress(long arg0, long arg1) {
						// TODO Auto-generated method stub
						System.out.println("On Que");
					}
				}).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						orders = fmd.getMyOrders(json);
						myOrderAdapter = new MyOrderAdapter(getActivity(), orders);
						myOrderListView.setAdapter(myOrderAdapter);
						hideProgressDialog();
						btnSend.setVisibility(View.VISIBLE);
					}
				});
	}

	private void sendOrders(String param) {
		showProgressDialog();
		Ion.with(getContext()).load(EndPoints.SEND_ORDERS)
				.setBodyParameter("transaction_id", param).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
							hideProgressDialog();
							return;
						}
						JsonObject json = new JsonParser().parse(response).getAsJsonObject();
						Toast.makeText(getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
						hideProgressDialog();
						myOrderListView.setAdapter(null);
					}
				});
	}

	private void editOrder(int position) {
		Order order = orders.get(position);
		String menuName = "";
		String serving = "";
		String sauces = "";
		String sideDish = "";
		int qty;

		menuName = menuName + order.getFoodItem().getMenuName();
		if (order.getFoodItem().getServings().size() > 0) {
			for (int i = 0; i < order.getFoodItem().getServings().size(); i++) {
				serving = serving + order.getFoodItem().getServings().get(i).getServingName();
			}
		}
		if (order.getFoodItem().getSauces().size() > 0) {
			for (int i = 0; i < order.getFoodItem().getSauces().size(); i++) {
				sauces += order.getFoodItem().getSauces().get(i).getSauceName() + ", ";
			}
		}
		System.out.println("side_dish: " + order.getFoodItem().getSideDishes().size());
		if (order.getFoodItem().getSideDishes().size() > 0) {
			for (int i = 0; i < order.getFoodItem().getSideDishes().size(); i++) {
				sideDish = sideDish + order.getFoodItem().getSideDishes().get(i).getSideDishName();
			}
		}
		qty = order.getQty();

		Bundle odBundle = new Bundle();

		odBundle.putString("menu_name", menuName);
		odBundle.putString("serving", serving);
		odBundle.putString("side_dish", sideDish);
		odBundle.putInt("qty", qty);
		odBundle.putInt("id", order.getId());
		odBundle.putString("sauces", sauces.substring(0, sauces.length() - 2));

		Intent editOrderActivity = new Intent(getContext(), EditOrderActivity.class);
		editOrderActivity.putExtras(odBundle);
		startActivity(editOrderActivity);
		getActivity().finish();
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

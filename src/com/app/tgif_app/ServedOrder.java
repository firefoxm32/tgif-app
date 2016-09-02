package com.app.tgif_app;

import java.util.List;

import com.app.tgif_app.adapter.MyOrderAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class ServedOrder extends Fragment {
	protected Session session;
	private ListView myOrderListView;
	private Button btnSend;
	private MyOrderAdapter myOrderAdapter;
	private List<Order> orders;
	private ProgressDialog pDialog;

	public static Fragment newInstance(Context context) {
		ServedOrder servedOrder = new ServedOrder();
		return servedOrder;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, container);
		session = new Session(getContext());
		myOrderListView = (ListView) rootview.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootview.findViewById(R.id.btnSendOrders);
		btnSend.setVisibility(View.GONE);

		myOrder(session.getTransactionId());

		return rootview;
	}

	private void myOrder(String transactionId) {
		showProgressDialog();
		Ion.with(MainActivity.getContext()).load(EndPoints.MY_ORDERS + "?table_number=" + transactionId + "&status=S")
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
					}
				});
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

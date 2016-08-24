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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, null);
		session = new Session(getContext());
		myOrderListView = (ListView) rootView.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootView.findViewById(R.id.btnSendOrders);
		btnSend.setVisibility(View.GONE);
		
		myOrder(session.getTransactionId());
		
		return rootView;
	}
	private void myOrder(String transactionId) {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading.... Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
		Ion.with(MainActivity.getContext())
		.load(EndPoints.MY_ORDERS+"?table_number="+transactionId+"&status=S")
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
			}
		});
		
	}
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
}

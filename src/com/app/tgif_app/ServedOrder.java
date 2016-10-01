package com.app.tgif_app;

import java.util.ArrayList;
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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class ServedOrder extends Fragment {
	protected Session session;
	private ListView myOrderListView;
	private ImageButton imgbtnSend;
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
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, null);
		session = new Session(getContext());
		myOrderListView = (ListView) rootview.findViewById(R.id.myOrderListView);
		imgbtnSend = (ImageButton) rootview.findViewById(R.id.img_btn_send_orders);
		imgbtnSend.setVisibility(View.GONE);
		threading();
		
		return rootview;
	}
	private void threading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						myOrder(session.getTransactionId());
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void myOrder(String transactionId) {
//		showProgressDialog("Loading...");
		Ion.with(MainActivity.getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.MY_ORDERS + "?transaction_id=" + transactionId + "&status=S")
				.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						List<Order> orderList = new ArrayList<>();
						FoodMenuDAO fmd = new FoodMenuDAO();
						orders = fmd.getMyOrders(json, orderList);
						
						if (myOrderListView.getAdapter() != null) {
							for (int i = 0; i < myOrderListView.getAdapter().getCount(); i++) {
								Order orderObj = (Order) myOrderListView.getAdapter().getItem(i);
								orderList.add(orderObj);
							}
						}
						
						myOrderAdapter = new MyOrderAdapter(getActivity(), "served", orders);
						myOrderListView.setAdapter(myOrderAdapter);
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

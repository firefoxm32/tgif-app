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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class CookingOrder extends Fragment {

	private ListView myOrderListView;
	private Button btnSend;
	private MyOrderAdapter myOrderAdapter;
	private List<Order> orders;
	private ProgressDialog pDialog;
	protected Session session;

	public static Fragment newInstance(Context context) {
		CookingOrder cookingOrder = new CookingOrder();
		return cookingOrder;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, null);
		session = new Session(getContext());
		myOrderListView = (ListView) rootview.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootview.findViewById(R.id.btn_send_orders);
		btnSend.setVisibility(View.GONE);
//		Button feedback = (Button) rootview.findViewById(R.id.feedback);
//		Button checkOut = (Button) rootview.findViewById(R.id.checkout);
//		feedback.setVisibility(View.GONE);
//		checkOut.setVisibility(View.GONE);

		startCooking();
		return rootview;
	}
	private Thread thread;
	private void startCooking() {
		isRunning = true;
		thread = new Thread(new CookingThread());
		thread.start();
	}
	public static void stopCooking() {
		isRunning = false;
		System.out.println("Stop THREAD: "+isRunning);
	}
	private static boolean isRunning = true;
	protected class CookingThread implements Runnable {
		
		@Override
		public void run() {
			if(!isRunning) {
				thread = null;
			}
			while (isRunning) {
				System.out.println("interrut THREAD: "+isRunning);
				try {
					myOrder(session.getTransactionId());
					Thread.sleep(5000);
				} catch (Exception e) {
					// TODO: handle exception
//					e.printStackTrace();
				}
			}
		}
		
	}

	private void myOrder(String transactionId) {
		Ion.with(MainActivity.getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.MY_ORDERS
				+ "?transaction_id=" + transactionId + "&status=C").asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
//						System.out.println("JSON: "+json);
						if (json == null) {
//							toastMessage("Network error");
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						orders = fmd.getMyOrders(json);
						myOrderAdapter = new MyOrderAdapter(getActivity(), "cooking", orders);
						int index = myOrderListView.getFirstVisiblePosition();
						System.out.println("INDEX: "+index);
						myOrderListView.setAdapter(myOrderAdapter);
						myOrderListView.smoothScrollToPosition(index);
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

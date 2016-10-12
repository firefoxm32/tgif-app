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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, null);
		session = new Session(getContext());
		myOrderListView = (ListView) rootView.findViewById(R.id.myOrderListView);
		btnSend = (Button) rootView.findViewById(R.id.btn_send_orders);
//		btnSend.setVisibility(View.GONE);
//		Button feedback = (Button) rootView.findViewById(R.id.feedback);
//		Button checkOut = (Button) rootView.findViewById(R.id.checkout);
//		feedback.setVisibility(View.GONE);
//		checkOut.setVisibility(View.GONE);
		
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
				if(orders.size() == 0) {
					toastMessage("No order to send");
					return;
				}
				YesNo();
			}
		});
		return rootView;
	}

	private void myOrder(String transactionId) {
		showProgressDialog("Loading...");
		Ion.with(MainActivity.getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.MY_ORDERS + "?transaction_id=" + transactionId)
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
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						FoodMenuDAO fmd = new FoodMenuDAO();
						orders = fmd.getMyOrders(json);
						myOrderAdapter = new MyOrderAdapter(getActivity(), "pending", orders);
						myOrderListView.setAdapter(myOrderAdapter);
						hideProgressDialog();
						btnSend.setVisibility(View.VISIBLE);
					}
				});
	}

	private void sendOrders(String param) {
		showProgressDialog("Loading...");
		Ion.with(getContext()).load(EndPoints.HTTP+session.getIpAddress()+EndPoints.SEND_ORDERS)
		.setBodyParameter("transaction_id", param).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							hideProgressDialog();
							return;
						}
						JsonObject json = new JsonParser().parse(response).getAsJsonObject();
						toastMessage(json.get("message").getAsString());
						hideProgressDialog();
						myOrderListView.setAdapter(null);
					}
				});
	}

	private void editOrder(int position) {
		Order order = orders.get(position);
		String itemName = "";
		String serving = "";
		String sauces = "";
		String sideDish = "";
		String itemImage = "";
		int qty;

		itemName = order.getFoodItem().getItemName();
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
		if (order.getFoodItem().getSideDishes().size() > 0) {
			for (int i = 0; i < order.getFoodItem().getSideDishes().size(); i++) {
				sideDish = sideDish + order.getFoodItem().getSideDishes().get(i).getSideDishName();
			}
		}
		qty = order.getQty();
		itemImage = order.getFoodItem().getImage();
		String subSauces = "";
		if (sauces != "") {
			subSauces = sauces.substring(0, sauces.length() - 2);
		}

		Bundle odBundle = new Bundle();

		odBundle.putString("item_name", itemName);
		odBundle.putString("serving", serving);
		odBundle.putString("side_dish", sideDish);
		odBundle.putInt("qty", qty);
		odBundle.putInt("id", order.getId());
		odBundle.putString("sauces", subSauces);
		odBundle.putString("item_image", itemImage);

		Intent editOrderActivity = new Intent(getContext(), EditOrderActivity.class);
		editOrderActivity.putExtras(odBundle);
		startActivity(editOrderActivity);
		getActivity().finish();
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
	
	private void YesNo() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		alertBuilder.setMessage("Are you sure you want to send orders? Cannot cancel after sending.");
		alertBuilder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				getTotalPrice();
					sendOrders(session.getTransactionId());
			}
		});
		alertBuilder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				toastMessage("Sending orders cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CONFIRMATION");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}
}

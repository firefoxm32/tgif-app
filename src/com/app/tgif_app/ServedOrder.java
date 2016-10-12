package com.app.tgif_app;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.app.tgif_app.adapter.MyOrderAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.Order;
import model.Session;

public class ServedOrder extends Fragment {
	protected Session session;
	public static ListView myOrderListView;
//	private Button btnSend;
	private MyOrderAdapter myOrderAdapter;
	private List<Order> orders;
	private ProgressDialog pDialog;
	public static boolean[] isRated;
	
	private EditText cash;
	private double totalBill;
	private EditText memberId;
	private EditText creditCardNumber;
	private EditText creditCardName;

	public static Fragment newInstance(Context context) {
		ServedOrder servedOrder = new ServedOrder();
		return servedOrder;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_served_orders, null);
		session = new Session(getContext());
		myOrderListView = (ListView) rootview.findViewById(R.id.served_order_list);
//		btnSend = (Button) rootview.findViewById(R.id.btn_send_orders);
//		btnSend.setVisibility(View.GONE);
		Button feedback = (Button) rootview.findViewById(R.id.feedback);
		Button checkOut = (Button) rootview.findViewById(R.id.checkout);
//		startServe();
		myOrderListView.setItemsCanFocus(true);
		myOrderListView.setFocusable(false);
		myOrderListView.setFocusableInTouchMode(false);
		myOrderListView.setClickable(false);
		myOrder(session.getTransactionId());
		myOrderListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				Order order = orders.get(position);
//				System.out.println("ITEM ID: " + order.getItemId());
//				System.out.println("ITEM NAME: "+order.getFoodItem().getItemName());
				
			}
		});

		checkOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkAllCookingOrder();
				getTotalPrice();
			}
		});

		feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				feedback();
			}
		});
		return rootview;
	}

	private Thread thread;

	private void startServe() {
		isRunning = true;
		thread = new Thread(new CookingThread());
		thread.start();
	}

	public static void stopServe() {
		isRunning = false;
	}

	private static boolean isRunning = true;

	protected class CookingThread implements Runnable {

		@Override
		public void run() {
			if (!isRunning) {
				thread = null;
			}
			while (isRunning) {
				try {
					myOrder(session.getTransactionId());
					Thread.sleep(5000);
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
				}
			}
		}
	}

	private void myOrder(String transactionId) {
		Ion.with(MainActivity.getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.MY_ORDERS
				+ "?transaction_id=" + transactionId + "&status=S").asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception arg0, JsonObject json) {
						// TODO Auto-generated method stub
						if (json == null) {
//							toastMessage("Network error");
//							hideProgressDialog();
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
//						myOrderAdapter.setCustomButtonListner(MainActivity.);
						myOrderListView.setAdapter(myOrderAdapter);
					}
				});
	}

	private void rating(int rating, int id) {
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.MY_ORDERS)
				.setBodyParameter("item_id", String.valueOf(rating)).setBodyParameter("rating", String.valueOf(id))
				.asString().setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub

						if (response == null) {
							toastMessage("Network Error");
							return;
						}
						toastMessage("Rated");
					}
				});
	}

	private int row;
	private void checkAllCookingOrder() {
		row = 0;
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.GET_ALL_COOKING)
				.setBodyParameter("param", session.getTransactionId()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						System.out.println("RESPONSE: " + response);
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject json = new JsonParser().parse(response).getAsJsonObject();
						if (!json.get("rows").toString().replace("\"", "").equalsIgnoreCase("null")) {
							int rows = json.get("rows").getAsInt();
							row = rows;
						}
					}
				});
	}

	private void getTotalPrice() {
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.TOTAL_PRICE)
				.setBodyParameter("transaction_id", session.getTransactionId()).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception arg0, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject json = new JsonParser().parse(response).getAsJsonObject();
						checkOut(json.get("total_price").getAsDouble());
					}
				});
	}

	private String method;

	private void checkOut(double _totalBill) {
		final DecimalFormat formatter = new DecimalFormat("#,##0.00");
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.check_out, null);
		alert.setCancelable(false);
		alert.setView(dialogView);

		Button cashMethod = (Button) dialogView.findViewById(R.id.cash_method);
		Button creditCardMethod = (Button) dialogView.findViewById(R.id.credit_card_method);
		TextView total = (TextView) dialogView.findViewById(R.id.bill);
		cash = (EditText) dialogView.findViewById(R.id.cash);
		memberId = (EditText) dialogView.findViewById(R.id.member_id);
		creditCardName = (EditText) dialogView.findViewById(R.id.credit_card_name);
		creditCardNumber = (EditText) dialogView.findViewById(R.id.credit_card_number);

		cash.setVisibility(View.GONE);
		creditCardName.setVisibility(View.GONE);
		creditCardNumber.setVisibility(View.GONE);
		method = "cash_method";
		cashMethod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				method = "cash_method";
				creditCardName.setVisibility(View.GONE);
				creditCardNumber.setVisibility(View.GONE);
				cash.setVisibility(View.GONE);
				cash.setText("");
				memberId.setText("");
				memberId.requestFocus();
//				cash.requestFocus();
			}
		});
		creditCardMethod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				method = "credit_card_method";
				creditCardName.setVisibility(View.VISIBLE);
				creditCardNumber.setVisibility(View.VISIBLE);
				cash.setVisibility(View.GONE);
				creditCardName.setText("");
				creditCardNumber.setText("");
				memberId.setText("");
				creditCardName.requestFocus();
			}
		});
		totalBill = _totalBill;
		totalBill = totalBill + (totalBill * .03);
		total.setText("Total: " + formatter.format(totalBill));
		cash.requestFocus();
		alert.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (totalBill == 0.00) {
					toastMessage("Not yet order");
					return;
				}

//				if ((row != 0 && !cash.getText().toString().isEmpty())
//						|| (row != 0 && !creditCardName.getText().toString().isEmpty()
//								&& !creditCardNumber.getText().toString().isEmpty())) {
//					toastMessage("Please check out after all your orders served.");
//					return;
//				}
				
				if (row != 0) {
					toastMessage("Please check out after all your orders served.");
					return;
				}

				if (method.equalsIgnoreCase("cash_method")) {
//					if (cash.getText().toString().trim().isEmpty()) {
//						toastMessage("Invalid input cash");
//						return;
//					}

//					if (cash.getText().toString().length() > 9) {
//						toastMessage("Invalid input cash");
//						return;
//					}

//					if (totalBill > Double.valueOf(cash.getText().toString())) {
//						toastMessage("Insufficient amount");
//						return;
//					}
					insertCashHeader(session.getTransactionId(), totalBill, "", "", 
							memberId.getText().toString().toString().trim());
				} else {
					if (creditCardName.getText().toString().trim().isEmpty()) {
						toastMessage("Please enter credit card holder name");
						return;
					}

					if (creditCardNumber.getText().toString().trim().isEmpty()) {
						toastMessage("Please enter credit card number");
						return;
					}

					if (creditCardNumber.getText().toString().length() > 16) {
						toastMessage("Invalid Input");
						return;
					}

					insertCashHeader(session.getTransactionId(), totalBill,
							creditCardName.getText().toString().trim(), creditCardNumber.getText().toString().trim(),
							memberId.getText().toString().toString().trim());
				}
			}
		});
		alert.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				toastMessage("CANCELLED!");
			}
		});
		AlertDialog alertDialog = alert.create();
		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("CHECK OUT");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}

	private void insertCashHeader(String transactionId, double total, String cardName,
			String cardNumber, String id) {
		System.out.println("TID: " + transactionId);
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.INSERT_CASH_HEADER)
				.setBodyParameter("transaction_id", transactionId)
				.setBodyParameter("member_id", id)
				.setBodyParameter("total_price", String.valueOf(total)).setBodyParameter("credit_card_name", cardName)
				.setBodyParameter("credit_card_number", cardNumber).asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						System.out.println("RESPONSE: " + response);
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject object = new JsonParser().parse(response).getAsJsonObject();
						if (object == null) {
							return;
						}
						System.out.println("STATUS: " + object.get("status").getAsString());
						if (object.get("status").getAsString().equalsIgnoreCase("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}

						if (object.get("status").getAsString().toLowerCase().equals("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}
						toastMessage(object.get("message").getAsString());
						session.setTableStatus("C");
						 Intent verifying = new Intent(getActivity(),
						 VerifyingActivity.class);
						 getActivity().overridePendingTransition(0, 0);
						 startActivity(verifying);
						 getActivity().finish();
					}
				});
	}

	private void feedback() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.fragment_feedback, null);
		alertBuilder.setView(dialogView);
		alertBuilder.setCancelable(false);
		final EditText customerName = (EditText) dialogView.findViewById(R.id.feedback_user);
		final EditText msg = (EditText) dialogView.findViewById(R.id.feedback_messageg);
		alertBuilder.setPositiveButton("Send", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (row != 0) {
					toastMessage("Please send feedback after all your orders served.");
					return;
				}
				String customer = customerName.getText().toString();
				String message = msg.getText().toString();
				if (message.isEmpty()) {
					toastMessage("Cannot send feedback with empty message");
					msg.requestFocus();
				} else {
					toastMessage("Sent!");
					if (customer.isEmpty()) {
						customer = "Customer";
					}
					sendFeedBack(customer, message);
				}
			}
		});
		alertBuilder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				toastMessage("Sending feedback cancelled");
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_dialog_title, null);
		TextView title = (TextView) view.findViewById(R.id.custom_title);
		title.setText("FEEDBACK");
		alertDialog.setCustomTitle(view);
		alertDialog.show();
	}

	private void sendFeedBack(String customerName, String message) {
		Ion.with(getContext()).load(EndPoints.HTTP + session.getIpAddress() + EndPoints.SEND_FEEDBACK)
				.setBodyParameter("transaction_id", session.getTransactionId())
				.setBodyParameter("customer_name", customerName).setBodyParameter("message", message).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception e, String response) {
						// TODO Auto-generated method stub
						if (response == null) {
							toastMessage("Network error");
							return;
						}
						JsonObject object = new JsonParser().parse(response).getAsJsonObject();
						if (object == null) {
							return;
						}
						if (object.get("status").getAsString().toLowerCase().equals("error")) {
							toastMessage(object.get("message").getAsString());
							return;
						}
						toastMessage(object.get("message").getAsString());
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

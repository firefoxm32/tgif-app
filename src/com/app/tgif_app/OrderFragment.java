package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;

import com.tgif.dao.FoodMenuDAO;
import com.tgif.http.Dispatcher;
import com.tgif.http.TGIFRequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import model.Order;

public class OrderFragment extends Fragment {
	
	ImageView imageViewFoodImg;
	Button buttonAddOrder;
	Button buttonViewOrder;
	WebView webViewLayout;
	
//	FragmentTransaction ft = getFragmentManager().beginTransaction();
	
	private ArrayList<String> orderList;
		
	public static Fragment newInstance(Context context){
		OrderFragment orderFragment = new OrderFragment();
		return orderFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order, null);
		imageViewFoodImg = (ImageView) rootView.findViewById(R.id.imageViewFoodImg);
		System.out.println(getArguments().getString("choice"));
		
		//textViewFoodName = (TextView) rootView.findViewById(R.id.textViewFoodName);
		
		buttonAddOrder = (Button) rootView.findViewById(R.id.buttonAddOrder);
		
		buttonViewOrder = (Button) rootView.findViewById(R.id.buttonViewOrders);
		
		webViewLayout = (WebView) rootView.findViewById(R.id.webViewLayout);
		
		String imageUrl = "http://192.168.1.101/tgif/images/traditional_wings.jpg";
		
		webViewLayout.setWebViewClient(new WebViewClient());
		webViewLayout.loadUrl("http://192.168.1.100/tgif/views/menu-item-info.php");
		
		buttonAddOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle odBundle = new Bundle();
				String menuName = getArguments().getString("choice");
				odBundle.putString("menu_name", menuName);
				odBundle.putString("action", "add");
				Fragment orderDetails = new OrderDetails();
				orderDetails.setArguments(odBundle);
				
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.container, orderDetails);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		
		buttonViewOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		return rootView;
	}
}

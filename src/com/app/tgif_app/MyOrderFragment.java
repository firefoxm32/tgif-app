package com.app.tgif_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyOrderFragment extends Fragment {
	
	private FragmentTabHost mTabHost;
	public static Fragment newInstance(Context context){
		MyOrderFragment myOrderFragment = new MyOrderFragment();
		return myOrderFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order_tabhost, null);
		MainActivity.mToolbar.setTitle("My Orders");
		mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
		
        mTabHost.addTab(mTabHost.newTabSpec("pending").setIndicator("Pending"),
                PendingOrder.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("cooking").setIndicator("Cooking"),
                CookingOrder.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("served").setIndicator("Served"),
                ServedOrder.class, null);
        
        
		return rootView;
	}
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
}

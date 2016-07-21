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
//	private FrameLayout frameLayout;
	public static Fragment newInstance(Context context){
		MyOrderFragment myOrderFragment = new MyOrderFragment();
		return myOrderFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_order_tabhost, null);
		mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
	 mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
//		mTabHost.setup();
//		mTabHost.setup(getActivity(), getChildFragmentManager());
        mTabHost.addTab(mTabHost.newTabSpec("pending").setIndicator("Pending"),
                PendingOrder.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Sent Order"),
                ServedOrder.class, null);
//		mTabHost.addTab(mTabHost.newTabSpec("pending").setIndicator("Pending"));
		return rootView;
	}
	
/*	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.remove(MyOrderFragment.this);
		mTabHost.removeAllViews();
	}*/
}

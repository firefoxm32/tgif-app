package com.app.tgif_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragment extends Fragment {
	
	public static Fragment newInstance(Context context) {
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		return welcomeFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreateView(inflater, container, savedInstanceState);
		container = (ViewGroup) inflater.inflate(R.layout.fragment_welcome, null);
		Button order = (Button) container.findViewById(R.id.startOrder);
		System.out.println("welcome");
		order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainActivity = new Intent(getContext(), MainActivity.class);
				 
				startActivity(mainActivity);
			}
		});
		return container;
	}
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
}

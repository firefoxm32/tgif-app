package com.app.tgif_app;

import android.support.v4.app.Fragment;

public class WelcomeFragment extends Fragment {
	
//	public static Fragment newInstance(Context context) {
//		WelcomeFragment welcomeFragment = new WelcomeFragment();
//		return welcomeFragment;
//	}
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
////		super.onCreateView(inflater, container, savedInstanceState);
//		container = (ViewGroup) inflater.inflate(R.layout.fragment_welcome, null);
//		Button order = (Button) container.findViewById(R.id.startOrder);
//		System.out.println("welcome");
//		order.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
////				Calendar c = Calendar.getInstance();
////		        System.out.println("Current time => "+c.getTime());
////
////		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////		        String formattedDate = df.format(c.getTime());
////				
////				Session session = new Session(getContext());
////				System.out.println("transaction_id: " + formattedDate + session.getUsername());
////				String transactionId = formattedDate + session.getUsername();
////				session.setTransactionId(transactionId);
//				
//				Intent mainActivity = new Intent(getContext(), MainActivity.class);
//				startActivity(mainActivity);
//			}
//		});
//		return container;//	}
//	@Override
//	public void onViewStateRestored(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onViewStateRestored(savedInstanceState);
//	}
}

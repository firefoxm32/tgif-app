package com.app.tgif_app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.http.EndPoints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditOrderFragment extends Fragment {
	
	private ImageView image;
	private TextView menuName;
	private TextView serving;
	private TextView sauces;
	private TextView sideDish;
	private EditText qty;
	private Button btnEdit;
	private Button btnDelete;
	private Boolean bool=false;
	public String message;
	public static Fragment newInstance(Context context){
		EditOrderFragment editOrderFragment = new EditOrderFragment();
		return editOrderFragment;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_order, null);
		MainActivity.mToolbar.setTitle("Edit Order");
		image = (ImageView) rootView.findViewById(R.id.editOrderImage);
		menuName = (TextView) rootView.findViewById(R.id.editOrderFoodName);
		serving = (TextView) rootView.findViewById(R.id.editOrderServing);
		sauces = (TextView) rootView.findViewById(R.id.editOrderSauce);
		sideDish = (TextView) rootView.findViewById(R.id.editOrderSideDish);
		qty = (EditText) rootView.findViewById(R.id.editOrderQty);
		btnEdit = (Button) rootView.findViewById(R.id.editOrderbtnEdit);
		btnDelete = (Button) rootView.findViewById(R.id.editOrderbtnDelete);
		
		image.setImageResource(R.drawable.traditional_wings);
		System.out.println("id:"+getArguments().getInt("id"));
		if (getArguments().getString("menu_name").equals("")) {
			menuName.setVisibility(View.GONE);
		}
		menuName.setText("Menu Name: "+getArguments().getString("menu_name"));
		if (getArguments().getString("serving").equals("")) {
			serving.setVisibility(View.GONE);
		}
		serving.setText("Serving: " + getArguments().getString("serving"));
		if (getArguments().getString("sauces").equals("")) {
			sauces.setVisibility(View.GONE);
		}
		sauces.setText("Sauce/s: "+getArguments().getString("sauces"));
		if (getArguments().getString("side_dish").equals("")) {
			sideDish.setVisibility(View.GONE);
		}
		sideDish.setText("Side Dish: "+getArguments().getString("side_dish"));
		qty.setText(String.valueOf(getArguments().getInt("qty")));
		
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Integer.valueOf(qty.getText().toString()) > 0) {
					editOrder();
				} else {
					Toast.makeText(getActivity(), "Quantity is 0 or less than 0", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msgBox();
			}
		});
		
		return rootView;
	}
	
	private void editOrder() {
		Ion
		.with(MainActivity
				.getContext())
        .load(EndPoints.EDIT_ORDER)
        .progress(new ProgressCallback() {
			@Override
			public void onProgress(long arg0, long arg1) {
				// TODO Auto-generated method stub
				System.out.println("On Que");
			}
		})
        .setBodyParameter("id", String.valueOf(getArguments().getInt("id")))
        .setBodyParameter("qty", qty.getText().toString())
        .asString()
        .setCallback(new FutureCallback<String>() {
			@Override
			public void onCompleted(Exception e, String result) {
				// TODO Auto-generated method stub
				JsonParser parser = new JsonParser();
				System.out.println("Complete");
				JsonObject json = parser.parse(result).getAsJsonObject();
				System.out.println("message: "+json.get("message").getAsString());
				System.out.println("sql: "+json.get("sql").getAsString());
				System.out.println("status: "+json.get("status").getAsString());
				if (!json.get("status").getAsString().equalsIgnoreCase("error")) {
					Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
					Fragment myOrder = new MyOrderFragment();
					
					FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.container, myOrder);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				} else {
					Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void msgBox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Delete Order");
		builder.setMessage("Are you sure?");
		builder.setIcon(null);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "Yes", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "No", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}

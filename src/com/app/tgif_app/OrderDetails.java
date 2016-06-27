package com.app.tgif_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tgif.dao.FoodMenuDAO;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import model.FoodItem;
import model.Order;
import model.Sauce;
import model.Serving;
import model.SideDish;

public class OrderDetails extends Fragment {
	
	private CheckBox cb[];
	private RadioButton rdb[];
	private EditText qty;
	private RadioButton rdbSD[];
	private RadioGroup rg;
	private LinearLayout lSauce;
	private RadioGroup rgSD;
	
	private List<Serving> servings;
	private List<Sauce> sauces;
	private List<SideDish> sideDishes;
	
	private List<Integer> servingId;
	private List<Integer> sauceId;
	private List<Integer> sideDishId;
	
	private FoodItem fmItem;
	private FoodMenuDAO fmd;
	
	public static Fragment newInstance(Context context){
		OrderDetails orderDetails = new OrderDetails();
		return orderDetails;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order_details, null);
		
		rg = (RadioGroup) rootView.findViewById(R.id.rdbGroup);
		qty = (EditText) rootView.findViewById(R.id.qty);
		lSauce = (LinearLayout) rootView.findViewById(R.id.lSauce);
		rgSD = (RadioGroup) rootView.findViewById(R.id.rdbGroupSD);
		
		TextView textViewServing = (TextView) rootView.findViewById(R.id.servings);
		TextView textViewSauce = (TextView) rootView.findViewById(R.id.sauce);
		TextView textViewSideDish = (TextView) rootView.findViewById(R.id.sideDish);
		
		textViewServing.setVisibility(View.GONE);
		textViewSauce.setVisibility(View.GONE);
		textViewSideDish.setVisibility(View.GONE);
		
		fmd = new FoodMenuDAO();
        String menuName = getArguments().getString("menu_name");
        MainActivity.mToolbar.setTitle(menuName);
        fmItem = fmd.getOrderDetails(menuName.replace(" ", "%20"));
        
        servings = fmItem.getServings();
       
        if (servings.size() > 0) {
        	rdb = new RadioButton[servings.size()];
        	servingId = new ArrayList<>();
        	for(int i = 0; i < servings.size(); i++) {
            	Serving serving = servings.get(i);	
            	rdb[i] = new RadioButton(getActivity());
    			rg.addView(rdb[i]);
    			rdb[i].setText(serving.getServingName() + " Php " + serving.getServingPrice().getPrice());
    			servingId.add(serving.getServingId());
            }
        	rdb[0].setChecked(true);
		}
        
        sauces = fmItem.getSauces();
		if (sauces.size() > 0) {
			textViewSauce.setVisibility(View.VISIBLE);
			sauceId = new ArrayList<>();
			cb = new CheckBox[sauces.size()];
			for (int i = 0; i < sauces.size(); i++) {
				Sauce sauce = sauces.get(i);
				cb[i] = new CheckBox(getActivity());
				cb[i].setText(sauce.getSauceName());
				sauceId.add(sauce.getSauceId());
				lSauce.addView(cb[i]);
			}
		}
		
		sideDishes = fmItem.getSideDishes();
		if (sideDishes.size() > 0) {
			textViewSideDish.setVisibility(View.VISIBLE);
			sideDishId = new ArrayList<>();
			rdbSD = new RadioButton[sideDishes.size()];
			for (int i = 0; i < sideDishes.size(); i++) {
				SideDish sideDish = sideDishes.get(i);
				sideDish.displayData();
				rdbSD[i] = new RadioButton(getActivity());
				rdbSD[i].setText(sideDish.getSideDishName());
				sideDishId.add(sideDish.getSideDishId());
				rgSD.addView(rdbSD[i]);
			}
		}
		
		Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					validation();
			}
			private void validation() {
				Order order = new Order();
				int servingCtr = 0;
				int sauceCtr = 0;
				int sideDishCtr = 0;
				for (int i = 0; i < servings.size(); i++) {
					
					if (rdb[i].isChecked() == false) {
						servingCtr++;
					} else {
						System.out.println(servingId.get(i));
						order.setServingId(servingId.get(i));
						System.out.println(rdb[i].getText());
					}
				}
				String strSauce="";
				for (int i = 0; i < sauces.size(); i++) {
					if (cb[i].isChecked()) {
						sauceCtr++;
						System.out.println(sauceId.get(i));
						strSauce += sauceId.get(i) + ",";
						System.out.println(cb[i].getText());
					}
				}

				order.setSauce(strSauce.substring(0, strSauce.length() - 1));
				
				Random rand = new Random();
				int x = rand.nextInt(10);
				for (int i = 0; i < sideDishes.size(); i++) {
					if (rdbSD[i].isChecked()) {
						sideDishCtr++;
						System.out.println(sideDishId.get(i));
						order.setSideDishId(sideDishId.get(i));
						System.out.println(rdbSD[i].getText());
					}
				}
				if (servingCtr == servings.size()) {
					Toast.makeText(getActivity(), "Select at least 1 serving.",Toast.LENGTH_SHORT).show();
				} else if (0 == sauceCtr && sauces.size() != 0) {
					Toast.makeText(getActivity(), "Select at least 1 sauce.",Toast.LENGTH_SHORT).show();
				} else if (0 == sideDishCtr && sideDishes.size() != 0) {
					Toast.makeText(getActivity(), "Select at least 1 side dish.",Toast.LENGTH_SHORT).show();
				} else if (qty.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(), "Input quantity.",Toast.LENGTH_SHORT).show();
					qty.setFocusable(true);
				} else {
						order.setQty(Integer.valueOf(qty.getText().toString()));
						order.setTableNumber(x);
						order.setItemId(fmItem.getItemId());
						fmd.addOrder(order);
						clearData();
				}
			}
			
			private void clearData() {
				if (servings.size() > 0) {
					rdb[0].setChecked(true);
				}
				if (sauces.size() > 0) {
					for (int i = 0; i < sauces.size(); i++) {
						cb[i].setChecked(false);
					}
				}
				if (sideDishes.size() > 0) {
					for (int i = 0; i < sideDishes.size(); i++) {
						rdbSD[i].setChecked(false);
					}
				}
				qty.setText("");
			}
		});
		
		
		
		return rootView;
				
	}
}

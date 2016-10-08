package com.app.tgif_app.adapter;

import java.util.List;

import com.app.tgif_app.OrderDetails;
import com.app.tgif_app.R;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import model.FoodItem;
import model.Session;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder> {
	private List<FoodItem> list;
	private Context appContext;
	protected Session session;

	public PromoAdapter(List<FoodItem> data, Context context) {
		// TODO Auto-generated constructor stub
		list = data;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (list.get(position).getPromoStatus().equalsIgnoreCase("A")) {
			String imageUrl = EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
					+ list.get(position).getItemName().replace(" ", "%20").toLowerCase() + "/"
					+ list.get(position).getImage();
			Picasso.with(appContext).load(imageUrl).placeholder(R.drawable.placeholder_pic).error(R.drawable.not_found)
					.fit().centerCrop().into(holder.image);
			holder.itemName.setText(list.get(position).getItemName());
			holder.category.setText(list.get(position).getFoodMenu().getMenuName());
			holder.description.setText(list.get(position).getDescription().replace("\"", ""));
			holder.serving.setText(list.get(position).getServings().get(0).getServingName());
		}
	}

	@Override
	public PromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_card_viewer, parent, false);
		appContext = parent.getContext();
		session = new Session(parent.getContext());
		// MyViewHolder holder = new MyViewHolder(view);
		return new ViewHolder(view);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView itemName;
		TextView category;
		TextView description;
		TextView serving;
		ImageView image;

		public ViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			itemName = (TextView) view.findViewById(R.id.promo_item_name);
			category = (TextView) view.findViewById(R.id.promo_category);
			description = (TextView) view.findViewById(R.id.promo_description);
			serving = (TextView) view.findViewById(R.id.promo_serving);
			image = (ImageView) view.findViewById(R.id.promo_image);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("POSITION: " + getLayoutPosition());
			orderDetails(getLayoutPosition());
		}

		public void orderDetails(int position) {
			Bundle odBundle = new Bundle();
			odBundle.putString("item_name", list.get(position).getItemName());
			odBundle.putInt("item_id", list.get(position).getItemId());
			odBundle.putString("image", list.get(position).getImage());
			Fragment orderDetails = new OrderDetails();
			orderDetails.setArguments(odBundle);

//			FragmentTransaction fragmentTransaction = ((AppCompatActivity) appContext).getSupportFragmentManager()
//					.beginTransaction();
//			fragmentTransaction.replace(R.id.container, orderDetails).addToBackStack(null).commit();
		}
	}
}

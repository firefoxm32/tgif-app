package com.app.tgif_app.adapter;

import java.util.List;

import com.app.tgif_app.Home;
import com.app.tgif_app.OrderDetails;
import com.app.tgif_app.R;
import com.squareup.picasso.Picasso;
import com.tgif.http.EndPoints;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import model.FoodItem;

public class AllTimeFavoriteAndPromoAdapter extends RecyclerView.Adapter<AllTimeFavoriteAndPromoAdapter.ViewHolder> {
	private List<FoodItem> list;
	private Context appContext;

	public AllTimeFavoriteAndPromoAdapter(List<FoodItem> data, Context context) {
		list = data;
		// appContext = context;
	}

	@Override
	public AllTimeFavoriteAndPromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewer, parent, false);
		appContext = parent.getContext();
		// MyViewHolder holder = new MyViewHolder(view);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		
		// holder.image.setImageResource(R.drawable.not_found);

		if (list.get(position).getPromoStatus().equalsIgnoreCase("I")) {
			String imageUrl = EndPoints.PICASSO + list.get(position).getItemName().replace(" ", "%20").toLowerCase() + "/"
					+ list.get(position).getImage();
			Picasso.with(appContext)
					.load(imageUrl)
					.placeholder(R.drawable.placeholder_pic)
					.error(R.drawable.not_found).fit().centerCrop().into(holder.image);
			holder.itemName.setText(list.get(position).getItemName());
			holder.category.setText(list.get(position).getFoodMenu().getMenuName());
			holder.description.setText(list.get(position).getDescription().replace("\"", ""));
			holder.price.setVisibility(View.GONE);
		}
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
		TextView price;
		ImageView image;

		public ViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			itemName = (TextView) view.findViewById(R.id.promo_item_name);
			category = (TextView) view.findViewById(R.id.promo_category);
			description = (TextView) view.findViewById(R.id.promo_description);
			price = (TextView) view.findViewById(R.id.promo_price);
			image = (ImageView) view.findViewById(R.id.promo_image);
			view.setOnClickListener(this);
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("POSITION: "+getLayoutPosition());
			orderDetails(getLayoutPosition());
		}
		
		public void orderDetails(int position) {
			Bundle odBundle = new Bundle();
			System.out.println("image: " + list.get(position).getImage());
			odBundle.putString("item_name", list.get(position).getItemName());
			odBundle.putInt("item_id", list.get(position).getItemId());
			odBundle.putString("image", list.get(position).getImage());
			Fragment orderDetails = new OrderDetails();
			orderDetails.setArguments(odBundle);
			
			FragmentTransaction fragmentTransaction = ((AppCompatActivity)appContext).getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container, orderDetails).addToBackStack(null).commit();
		}
	}
}

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
import android.widget.RatingBar;
import android.widget.TextView;
import model.FoodItem;
import model.Session;

public class AllTimeFavoriteAdapter extends RecyclerView.Adapter<AllTimeFavoriteAdapter.ViewHolder> {
	private List<FoodItem> list;
	private Context appContext;
	protected Session session;

	public AllTimeFavoriteAdapter(List<FoodItem> data, Context context) {
		list = data;
		// appContext = context;
	}

	@Override
	public AllTimeFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_time_favorite_card_view, parent,
				false);
		appContext = parent.getContext();
		session = new Session(parent.getContext());
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (list.get(position).getPromoStatus().equalsIgnoreCase("I")) {
			String imageUrl = EndPoints.HTTP + session.getIpAddress() + EndPoints.PICASSO
					+ list.get(position).getItemName().replace(" ", "%20").toLowerCase() + "/"
					+ list.get(position).getImage();
			System.out.println("IMAGE: "+imageUrl);
			Picasso.with(appContext).load(imageUrl).placeholder(R.drawable.placeholder_pic).error(R.drawable.not_found)
					.fit().centerCrop().into(holder.allTimeFavoriteImage);
			holder.itemName.setText(list.get(position).getItemName());
			holder.category.setText(list.get(position).getFoodMenu().getMenuName());
			holder.description.setText(list.get(position).getDescription().replace("\"", ""));
			holder.ratingBar.setRating(2f);
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
		ImageView allTimeFavoriteImage;
		RatingBar ratingBar;

		public ViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			allTimeFavoriteImage = (ImageView) view.findViewById(R.id.atf_image);
			itemName = (TextView) view.findViewById(R.id.atf_item_name);
			category = (TextView) view.findViewById(R.id.atf_category);
			description = (TextView) view.findViewById(R.id.atf_description);
			ratingBar = (RatingBar) view.findViewById(R.id.atf_rating_bar);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			orderDetails(getLayoutPosition());
		}

		public void orderDetails(int position) {
			Bundle odBundle = new Bundle();
			odBundle.putString("item_name", list.get(position).getItemName());
			odBundle.putInt("item_id", list.get(position).getItemId());
			odBundle.putString("image", list.get(position).getImage());
			Fragment orderDetails = new OrderDetails();
			orderDetails.setArguments(odBundle);

			FragmentTransaction fragmentTransaction = ((AppCompatActivity) appContext).getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.container, orderDetails).addToBackStack(null).commit();
		}
	}
}

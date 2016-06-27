package com.tgif.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.app.tgif_app.FoodMenuFragment;
import com.app.tgif_app.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tgif.http.EndPoints;

import android.R.array;
import android.widget.Toast;
import model.FoodItem;
import model.FoodMenu;
import model.Order;
import model.Sauce;
import model.Serving;
import model.ServingPrice;
import model.SideDish;

public class FoodMenuDAO {
	public List<FoodMenu> getFoodMenus() {
		List<FoodMenu> list = new ArrayList<>();
		Future<JsonObject> json = Ion.with(MainActivity.getContext())
				.load(EndPoints.FOOD_MENUS)
				.uploadProgressBar(FoodMenuFragment.progressBar).asJsonObject();
		try {
			JsonArray menuItems = json.get().get("items").getAsJsonArray();
			for(int i = 0; i < menuItems.size(); i++) {
				JsonObject jsonObject = menuItems.get(i).getAsJsonObject();
				String label = jsonObject.get("label").getAsString();
				int id = jsonObject.get("menu_id").getAsInt();
				int qty = jsonObject.get("quantity").getAsInt();
	
				FoodMenu fm = new FoodMenu();
				fm.setMenuId(id);
				fm.setLabel(label);
				fm.setQty(qty);
				list.add(fm);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
			
		return list;
	}
	
	public List<FoodItem> getFoodMenuItems(int id) {
		List<FoodItem> list = new ArrayList<>();
		Future<JsonObject> json = Ion.with(MainActivity
				.getContext())
				.load(EndPoints.FOOD_MENU_ITEMS+"?params="+id)
				.asJsonObject();
		try {
			JsonArray menuItems = json.get().get("items").getAsJsonArray();
			for(int i = 0; i < menuItems.size(); i++) {
				JsonObject jsonObject = menuItems.get(i).getAsJsonObject();
				
				int itemId = jsonObject.get("item_id").getAsInt();
				int menuId = jsonObject.get("menu_id").getAsInt();
				String menuName = jsonObject.get("menu_name").getAsString();
				String image = jsonObject.get("image").getAsString();
				int orderCtr = jsonObject.get("order_ctr").getAsInt();
				
				FoodItem fmi = new FoodItem();
				fmi.setMenuId(menuId);
				fmi.setItemId(itemId);
				fmi.setMenuName(menuName);
				fmi.setImage(image);
				fmi.setOrderCtr(orderCtr);
				list.add(fmi);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
			
		return list;
	}
	
	public List<FoodItem> getAllTimeFavorites() {
		List<FoodItem> list = new ArrayList<>();
		Future<JsonObject> json = Ion.with(MainActivity
				.getContext())
				.load(EndPoints.ALL_TIME_FAVORITES)
				.asJsonObject();
		
		try {
			JsonArray menuItems = json.get().get("items").getAsJsonArray();

			for(int i = 0; i < menuItems.size(); i++) {
				JsonObject jsonObject = menuItems.get(i).getAsJsonObject();
				
				int itemId = jsonObject.get("item_id").getAsInt();
				int menuId = jsonObject.get("menu_id").getAsInt();
				String menuName = jsonObject.get("menu_name").getAsString();
				String image = jsonObject.get("image").getAsString();
				int orderCtr = jsonObject.get("order_ctr").getAsInt();
				
				FoodItem fmi = new FoodItem();
				fmi.setMenuId(menuId);
				fmi.setItemId(itemId);
				fmi.setMenuName(menuName);
				fmi.setImage(image);
				fmi.setOrderCtr(orderCtr);
				list.add(fmi);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		return list;
	}
	
	public FoodItem getOrderDetails(String itemName) {
		FoodItem fmi = new FoodItem();
		Future<JsonObject> json = Ion.with(MainActivity
				.getContext())
				.load(EndPoints.ORDER_DETAILS+"?param="+itemName)
				.asJsonObject();
		try {
			JsonObject jsonItem = json.get().get("item").getAsJsonObject();
			
			int itemId = jsonItem.get("id").getAsInt();
			fmi.setItemId(itemId);
			
			JsonArray jsonServings = jsonItem.get("servings").getAsJsonArray();			
			List<Serving> servings = new ArrayList<>();
			for (int i = 0; i < jsonServings.size(); i++) {
				JsonObject jsonObject = jsonServings.get(i).getAsJsonObject();
				String servingName = jsonObject.get("serving_name").getAsString();
				int servingId = jsonObject.get("serving_id").getAsInt();
				Double servingPrice = jsonObject.get("price").getAsDouble();
				
				ServingPrice price = new ServingPrice();
				price.setPrice(servingPrice);
				Serving serving = new Serving();
				serving.setServingId(servingId);
				serving.setServingName(servingName);
				serving.setServingPrice(price);
				
				servings.add(serving);
			}
			
			JsonArray jsonSauce = jsonItem.get("sauces").getAsJsonArray();
			List<Sauce> sauces = new ArrayList<>();
			for (int i = 0; i < jsonSauce.size(); i++) {
				JsonObject jsonObject = jsonSauce.get(i).getAsJsonObject();
				String sauceName = jsonObject.get("sauce_name").getAsString();
				int sauceId = jsonObject.get("sauce_id").getAsInt();
				Sauce sauce = new Sauce();
				sauce.setSauceId(sauceId);
				sauce.setSauceName(sauceName);
				sauces.add(sauce);
			}
			
			JsonArray jsonSideDish = jsonItem.get("side_dishes").getAsJsonArray();
			List<SideDish> sideDishes = new ArrayList<>();
			for (int i = 0; i < jsonSideDish.size(); i++) {
				JsonObject jsonObject = jsonSideDish.get(i).getAsJsonObject();
				String sideDishName = jsonObject.get("side_dish_name").getAsString();
				
				int sdId = jsonObject.get("side_dish_id").getAsInt();
				SideDish sideDish = new SideDish();
				sideDish.setSideDishId(sdId);
				sideDish.setSideDishName(sideDishName);
				sideDishes.add(sideDish);
			}
			fmi.setServings(servings);
			fmi.setSauces(sauces);
			fmi.setSideDishes(sideDishes);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		return fmi;
	}
	
	public void addOrder(Order order) {
		//Future<JsonObject> json = 
		Ion
		.with(MainActivity
				.getContext())
        .load(EndPoints.ADD_ORDER)
        .setBodyParameter("table_number", String.valueOf(order.getTableNumber()))
        .setBodyParameter("item_id", String.valueOf(order.getItemId()))
        .setBodyParameter("serving_id", String.valueOf(order.getServingId()))
        .setBodyParameter("sauces", order.getSauce())
        .setBodyParameter("side_dish_id", String.valueOf(order.getSideDishId()))
        .setBodyParameter("qty", String.valueOf(order.getQty()))
        .asString()
        .setCallback(new FutureCallback<String>() {
			@Override
			public void onCompleted(Exception e, String result) {
				// TODO Auto-generated method stub
				JsonParser parser = new JsonParser();

				JsonObject json = parser.parse(result).getAsJsonObject();
				
				System.out.println("sql: "+json.get("sql").getAsString());
			}
		});
	}
	

	public List<Order> getMyOrders(int tableNumber) {
		List<Order> orders = new ArrayList<>();
		Future<JsonObject> json = Ion.with(MainActivity.getContext())
		.load(EndPoints.MY_ORDERS+"?table_number="+tableNumber)
		.asJsonObject();
		
		try {
			String status = json.get().get("statuss").getAsString();
			System.out.println("status: "+status);
			JsonArray items = json.get().get("items").getAsJsonArray();
			for (int i = 0; i < items.size(); i++) {
				JsonObject jsonItem = items.get(i).getAsJsonObject();
				Order order = new Order();
				order.setQty(jsonItem.get("qty").getAsInt());
				order.setStatus(jsonItem.get("status").getAsString());
				order.setId(jsonItem.get("id").getAsInt());
				
				List<Serving> servings = new ArrayList<>();
				if (!jsonItem.get("serving").toString().equalsIgnoreCase("null")) {
					Serving serving = new Serving();
					serving.setServingName(jsonItem.get("serving").getAsString());
					servings.add(serving);
				}
				JsonArray arrSauces = jsonItem.get("sauces").getAsJsonArray();
				List<Sauce> sauces = new ArrayList<>();
				if (arrSauces.size() > 0) {
					for (int j = 0; j < arrSauces.size(); j++) {
						JsonObject jsonSauce = arrSauces.get(j).getAsJsonObject();
						Sauce sauce = new Sauce();
						sauce.setSauceName(jsonSauce.get("sauce_name").getAsString());
						sauces.add(sauce);
					}
				}
				List<SideDish> sideDishes = new ArrayList<>();
				if (!jsonItem.get("side_dish").toString().equalsIgnoreCase("null")) {
					SideDish sideDish = new SideDish();
					sideDish.setSideDishName(jsonItem.get("side_dish").getAsString());
					sideDishes.add(sideDish);
				}
				
				FoodItem foodItem = new FoodItem();
				foodItem.setMenuName(jsonItem.get("item_name").getAsString());
				foodItem.setServings(servings);
				foodItem.setSauces(sauces);
				foodItem.setSideDishes(sideDishes);
				
				order.setFoodItem(foodItem);
				orders.add(order);
				System.out.println("fetch");
				System.out.println("DAO:"+orders.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(MainActivity.getContext(), "Error Connection", Toast.LENGTH_SHORT).show();
		}
		
		return orders;
	}
	
	public void editOrder(String qty, String id) {
		Ion
		.with(MainActivity
				.getContext())
        .load(EndPoints.EDIT_ORDER)
        .setBodyParameter("id", id)
        .setBodyParameter("qty", qty)
        .asString()
        .setCallback(new FutureCallback<String>() {
			@Override
			public void onCompleted(Exception e, String result) {
				// TODO Auto-generated method stub
				JsonParser parser = new JsonParser();

				JsonObject json = parser.parse(result).getAsJsonObject();
				System.out.println("message: "+json.get("message").getAsString());
				System.out.println("sql: "+json.get("sql").getAsString());
				System.out.println("status: "+json.get("status").getAsString());
				if (!json.get("status").getAsString().equalsIgnoreCase("error")) {
					Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.getContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
		
}


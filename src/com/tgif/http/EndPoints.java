package com.tgif.http;

public class EndPoints {
	private static String ip = "192.168.1.103";
	
	public static final String FOOD_MENUS = "http://"+ip+"/tgif/api/get-food-menus.php";
	
	public static final String FOOD_MENU_ITEMS = "http://"+ip+"/tgif/api/get-food-menu-items.php";
	
	public static final String ALL_TIME_FAVORITES = "http://"+ip+"/tgif/api/get-all-time-favorites.php";
	
	public static final String ORDER_DETAILS = "http://"+ip+"/tgif/api/get-item-details.php";
	
	public static final String MY_ORDERS = "http://"+ip+"/tgif/api/get-my-order.php";
	
	public static final String ADD_ORDER = "http://"+ip+"/tgif/api/add-order.php";
	
	public static final String EDIT_ORDER = "http://"+ip+"/tgif/api/edit-my-order.php";
	
	public static final String DELETE_ORDER = "http://"+ip+"/tgif/api/delete-order.php";
	
	public static final String SEND_ORDERS = "http://"+ip+"/tgif/api/send-orders.php";
	
	public static final String PICASSO = "http://"+ip+"/tgif/images/";
	
	public final static String RECENT_API_ENDPOINT = "http://marsweather.ingenology.com/v1/latest/";
}

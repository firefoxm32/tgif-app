package com.tgif.http;

public class EndPoints {
	private static String ip = "192.168.1.104";
	
	public static final String FOOD_MENUS = "http://"+ip+"/tgif-api/api/get-food-menus.php";
	
	public static final String FOOD_MENU_ITEMS = "http://"+ip+"/tgif-api/api/get-food-menu-items.php";
	
	public static final String ALL_TIME_FAVORITES = "http://"+ip+"/tgif-api/api/get-all-time-favorites.php";
	
	public static final String ORDER_DETAILS = "http://"+ip+"/tgif-api/api/get-item-details.php";
	
	public static final String MY_ORDERS = "http://"+ip+"/tgif-api/api/get-my-order.php";
	
	public static final String ADD_ORDER = "http://"+ip+"/tgif-api/api/add-order.php";
	
	public static final String EDIT_ORDER = "http://"+ip+"/tgif-api/api/edit-my-order.php";
	
	public static final String DELETE_ORDER = "http://"+ip+"/tgif-api/api/delete-order.php";
	
	public static final String SEND_ORDERS = "http://"+ip+"/tgif-api/api/send-orders.php";
	
	public static final String UPDATE_TABLE_STATUS = "http://"+ip+"/tgif-api/api/update-table-status.php";
	
	public static final String LOGIN = "http://"+ip+"/tgif-api/api/login.php";
	
	public static final String LOGOUT = "http://"+ip+"/tgif-api/api/logout.php";
	
	public static final String INSERT_CASH_HEADER = "http://"+ip+"/tgif-api/api/insert-cash-header.php";

	public static final String TOTAL_PRICE = "http://"+ip+"/tgif-api/api/get-total-price.php";
	
	public static final String TABLE_STATUS = "http://"+ip+"/tgif-api/api/get-table-status.php";
	
	public static final String PICASSO = "http://"+ip+"/tgif-api/images/";
	
	public final static String RECENT_API_ENDPOINT = "http://marsweather.ingenology.com/v1/latest/";
}

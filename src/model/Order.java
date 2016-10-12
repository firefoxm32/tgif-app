package model;

public class Order {
	private int id;
	private int tableNumber;
	private int itemId;
	private String sauce;
	private int qty;
	private int servingId;
	private int sideDishId;
	private String status;
	private FoodItem foodItem;
	private String orderType;
	private float rating;
	
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTableNumber() {
		return tableNumber;
	}
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getSauce() {
		return sauce;
	}
	public void setSauce(String sauce) {
		this.sauce = sauce;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getServingId() {
		return servingId;
	}
	public void setServingId(int servingId) {
		this.servingId = servingId;
	}
	public int getSideDishId() {
		return sideDishId;
	}
	public void setSideDishId(int sideDishId) {
		this.sideDishId = sideDishId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public FoodItem getFoodItem() {
		return foodItem;
	}
	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}
	
	public void displayData() {
		/*for(foodItem.displayData()) {
			
		}*/
	}
}

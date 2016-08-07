package model;

import java.util.List;

public class FoodItem {
	private int itemId;
	private String menuName;
	private String image;
	private String description;
	private int orderCtr;
	private List<Serving> servings;
	private List<Sauce> sauces;
	private List<SideDish> sideDishes;
	private FoodMenu foodMenu;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrderCtr() {
		return orderCtr;
	}
	public void setOrderCtr(int orderCtr) {
		this.orderCtr = orderCtr;
	}
	public List<Serving> getServings() {
		return servings;
	}
	public void setServings(List<Serving> servings) {
		this.servings = servings;
	}
	public List<Sauce> getSauces() {
		return sauces;
	}
	public void setSauces(List<Sauce> sauces) {
		this.sauces = sauces;
	}
	public List<SideDish> getSideDishes() {
		return sideDishes;
	}
	public void setSideDishes(List<SideDish> sideDishes) {
		this.sideDishes = sideDishes;
	}
	public FoodMenu getFoodMenu() {
		return foodMenu;
	}
	public void setFoodMenu(FoodMenu foodMenu) {
		this.foodMenu = foodMenu;
	}
}

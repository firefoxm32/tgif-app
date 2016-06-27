package model;

import java.util.List;

public class FoodItem {
	private int itemId;
	private int menuId;
	private String menuName;
	private String image;
	private int orderCtr;
	private List<Serving> servings;
	private List<Sauce> sauces;
	private List<SideDish> sideDishes;

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
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
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
	public int getOrderCtr() {
		return orderCtr;
	}
	public void setOrderCtr(int orderCtr) {
		this.orderCtr = orderCtr;
	}

	public void displayData() {
		System.out.println("item ID: " + itemId);
		System.out.println("menu ID: " + menuId);
		System.out.println("name: " + menuName);
//		System.out.println("price: " + price);
//		System.out.println("image: " + image);
		
		if (sauces != null) {
			System.out.println("sauces");
			for(Sauce sauce : sauces) {
				sauce.displayData();
			}
		}
		System.out.println("Servings: ");
		if (servings != null) {
			for(Serving serving : servings) {
				serving.displayData();
			}
		}
		System.out.println("SideDishes: ");
		if (sideDishes != null) {
			for(SideDish sideDish : sideDishes) {
				sideDish.displayData();
			}
		}
	}
}

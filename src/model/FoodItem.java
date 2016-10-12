package model;

import java.util.List;

public class FoodItem {
	private int itemId;
	private String itemName;
	private String image;
	private String description;
	private Double promoPrice;
	private String promoStatus;
	private float rating;
	private List<Serving> servings;
	private List<Sauce> sauces;
	private List<SideDish> sideDishes;
	private FoodMenu foodMenu;
	
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getPromoPrice() {
		return promoPrice;
	}
	public void setPromoPrice(Double promoPrice) {
		this.promoPrice = promoPrice;
	}
	public String getPromoStatus() {
		return promoStatus;
	}
	public void setPromoStatus(String promoStatus) {
		this.promoStatus = promoStatus;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
	
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
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

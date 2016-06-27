package model;

public class ServingPrice {
	private int servingId;
	private int itemId;
	private Double price;
	
	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void displayData() {
		System.out.println("serving_id: "+servingId);
		System.out.println("item_id: "+itemId);
		System.out.println("prices: "+price);
	}
}
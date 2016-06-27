package model;

public class Sauce {
	private int sauceId;
	private String sauceName;
	private int itemId;
	public int getSauceId() {
		return sauceId;
	}
	public void setSauceId(int sauceId) {
		this.sauceId = sauceId;
	}
	public String getSauceName() {
		return sauceName;
	}
	public void setSauceName(String sauceName) {
		this.sauceName = sauceName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public void displayData() {
		System.out.println("sauce_id: "+sauceId);
		System.out.println("sauce_name: "+sauceName);
		System.out.println("item_id: "+itemId);
	}
}

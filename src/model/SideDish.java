package model;

public class SideDish {
	private int sideDishId;
	private String sideDishName;
	private int itemId;
	public int getSideDishId() {
		return sideDishId;
	}
	public void setSideDishId(int sideDishId) {
		this.sideDishId = sideDishId;
	}
	public String getSideDishName() {
		return sideDishName;
	}
	public void setSideDishName(String sideDishName) {
		this.sideDishName = sideDishName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public void displayData() {
		System.out.println("side_dish_id: "+sideDishId);
		System.out.println("side_dish_name: "+sideDishName);
		System.out.println("item_id: "+itemId);
	}
}

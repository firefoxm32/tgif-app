package model;

public class FoodMenu {
	private int menuId;
	private String label;
	private int qty;
	
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public void displayData() {
		System.out.println(menuId);
		System.out.println(label);
	}
}

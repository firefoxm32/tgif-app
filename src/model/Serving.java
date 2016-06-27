package model;

public class Serving {
	private int servingId;
	private String servingName;
	private ServingPrice servingPrice;
	
	public ServingPrice getServingPrice() {
		return servingPrice;
	}
	public void setServingPrice(ServingPrice servingPrice) {
		this.servingPrice = servingPrice;
	}
	public int getServingId() {
		return servingId;
	}
	public void setServingId(int servingId) {
		this.servingId = servingId;
	}
	public String getServingName() {
		return servingName;
	}
	public void setServingName(String servingName) {
		this.servingName = servingName;
	}
	
	public void displayData() {
		System.out.println("serving_id: "+servingId);
		System.out.println("serving_name: "+servingName);
	}
}

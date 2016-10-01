package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
	private SharedPreferences prefs;

	public Session(Context context) {
		// TODO Auto-generated constructor stub
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	public void setUsername(String username) {
		prefs.edit().putString("username", username).commit();
	}
	public String getUsername() {
		String username = prefs.getString("username", "");
		return username;
	}
	
	public void setPassword(String password) {
		prefs.edit().putString("password", password).commit();
	}
	public String getPassword() {
		String password = prefs.getString("password", "");
		return password;
	}
	
	public void setTableNumber(int tableNumber) {
		prefs.edit().putInt("table_number", tableNumber).commit();
	}
	public Integer getTableNumber() {
		int tableNumber = prefs.getInt("table_number", 0);
		return tableNumber;
	}
	
	public void setTransactionId(String transactionId) {
		prefs.edit().putString("transaction_id", transactionId).commit();
	}
	public String getTransactionId() {
		String trasactionId = prefs.getString("transaction_id", "");
		return trasactionId;
	}
	public void setIpAddress(String ipaddress) {
		prefs.edit().putString("ip_address", ipaddress).commit();
	}
	public String getIpAddress() {
		String ipAdress = prefs.getString("ip_address", "");
		return ipAdress;
	}
	public void setTableStatus(String tableStatus) {
		prefs.edit().putString("table_status", tableStatus).commit();
	}
	public String getTablestatus() {
		String tableStatus = prefs.getString("table_status", "");
		return tableStatus;
	}
	
	public void remove(String username, String password, String tableNumber) {
		prefs.edit().remove(username).commit();
		prefs.edit().remove(password).commit();
		prefs.edit().remove(tableNumber).commit();
	}
	public void removeTransactionId() {
		prefs.edit().remove("transaction_id").commit();
	}
	public void clearPrefs() {
		prefs.edit().clear().commit();
	}
}

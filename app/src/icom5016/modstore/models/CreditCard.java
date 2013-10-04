package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCard {	
	
	public int type; /* visa, mastercard, americanexpress, discover, ebay, googlecheckout, paypal */
	public String number;
	public String name;
	public String expire;
	
	public CreditCard() {
		super();
	}
	
	public CreditCard(int type, String number, String name, String expire) {
		this.type = type;
		this.number = number;
		this.name = name;
		this.expire = expire;
	}
	
	public CreditCard(JSONObject json) {
		try {
			this.type = json.getInt("type");
			this.number = json.getString("number");
			this.name = json.getString("name");
			this.expire = json.getString("expirationDate");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

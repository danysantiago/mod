package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCard {	
	public int cid;
	public int type; /* visa, mastercard, americanexpress, discover, ebay, googlecheckout, paypal */
	public String number;
	public String name;
	public String expire;
	public String securityCode;
	
	public CreditCard() {
		super();
	}
	
	public CreditCard(int cid, int type, String number, String name, String expire, String securityCode) {
		this.cid = cid;
		this.type = type;
		this.number = number;
		this.name = name;
		this.expire = expire;
		this.securityCode = securityCode;
	}
	
	public CreditCard(JSONObject json) {
		try {
			this.cid = json.getInt("ccid");
			this.type = json.getInt("type");
			this.number = json.getString("number");
			this.name = json.getString("name");
			this.expire = json.getString("expirationDate");
			this.securityCode = json.getString("scode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

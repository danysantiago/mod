package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCard {	
	private int cid;
	private int aid;
	private int type;
	private String number;
	private String name;
	private String expire;
	private String securityCode;
	private boolean isDefault;
	
	public CreditCard() {
		super();
	}
	
	public CreditCard(int cid, int aid, int type, String number, String name, String expire, String securityCode, boolean isDefault) {
		this.cid = cid;
		this.type = type;
		this.number = number;
		this.name = name;
		this.expire = expire;
		this.securityCode = securityCode;
		this.isDefault = isDefault;
	}
	
	public CreditCard(JSONObject json) {
		try {
			this.cid = json.getInt("ccid");
			this.aid = json.getInt("aid");
			this.type = json.getInt("type");
			this.number = json.getString("number");
			this.name = json.getString("name");
			this.expire = json.getString("expirationDate");
			this.securityCode = json.getString("scode");
			this.isDefault = json.getBoolean("isDefault");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getCid() {
		return cid;
	}

	public int getAid() {
		return aid;
	}

	public int getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getExpire() {
		return expire;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public boolean isDefault() {
		return isDefault;
	}
	
	
}

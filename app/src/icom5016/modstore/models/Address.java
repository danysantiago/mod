package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {	
	public int id;
	public String line1;
	public String line2;
	public String city;
	public String state;
	public String country;
	public String zipcode;
	public boolean isDefault;
	
	public Address() {
		super();
	}
	
	public Address(int id, String line1, String line2, String city, String state, String country, String zipcode, boolean isDefault) {
		super();
		this.id = id;
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.isDefault = isDefault;
	}

	public Address(JSONObject json) {
		super();
		try {
			this.id = json.getInt("aid");
			this.line1 = json.getString("line1");
			this.line2 = json.getString("line2");
			this.city = json.getString("city");
			this.state = json.getString("state");
			this.country = json.getString("country");
			this.zipcode = json.getString("zipcode");
			this.isDefault = json.getBoolean("isDefault");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getLocation() {
		String location = zipcode + " " + city;
		
		if (!state.equals("")) {
			location += ", " + state;
		}
		
		return location;
	}
	
	public String toString() {
		String out = this.line1;
		
		if (!line2.isEmpty())
			out += "\n" + this.line2;
		
		out += "\n" + this.getLocation() + "\n" + this.country;
		
		return out;
	}
}
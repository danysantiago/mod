package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {	
	private int id;
	private int userId;
	private String line1;
	private String line2;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	private boolean isDefault;
	private String createdTs;
	
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

	public Address(JSONObject json) throws JSONException {
		super();
		
		
			this.id = json.getInt("address_id");
			this.userId = json.getInt("user_id");
			this.line1 = json.getString("line1");
			this.line2 = json.getString("line2");
			this.city = json.getString("city");
			this.state = json.getString("state");
			this.country = json.getString("country");
			this.zipcode = json.getString("zipcode");
			
			int is_default = json.getInt("is_primary");
			this.isDefault = is_default == 1;
			this.createdTs = json.getString("created_ts");
			
	}
	
	public String getLocation() {
		String location = zipcode + " " + city;
		
		if (!state.equals("")) {
			location += ", " + state;
		}
		
		return location;
	}
	
	@Override
	public String toString() {
		String out = this.line1;
		
		if (!line2.isEmpty())
			out += "\n" + this.line2;
		
		out += "\n" + this.getLocation() + "\n" + this.country;
		
		return out;
	}

	public int getId() {
		return id;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public String getLine1() {
		return line1;
	}

	public String getLine2() {
		return line2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getZipcode() {
		return zipcode;
	}
	
}
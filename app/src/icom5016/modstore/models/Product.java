package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
	
	private String pid;
	private String name;
	private String description;
	private String price;
	private String brand;
	private String model;
	
	public Product(String pid, String name, String description, String price, String brand, String model) {
		super();
		this.pid = pid;
		this.name = name;
		this.description = description;
		this.price = price;
		this.brand = brand;
		this.model = model;
	}

	public Product(JSONObject jsonObject) {
		try {
			this.pid = jsonObject.getString("pid");
			this.name = jsonObject.getString("name");
			this.description = jsonObject.getString("description");
			this.price = jsonObject.getString("price");
			this.brand = jsonObject.getString("brand");
			this.model = jsonObject.getString("model");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPid() {
		return pid;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPrice() {
		return price;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

}

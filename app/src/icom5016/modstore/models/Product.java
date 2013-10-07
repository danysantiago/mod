package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Product {
	private int pid;
	private int uid;
	private int cid;
	private String name;
	private String description;
	private String brand;
	private String model;
	private String dimensions;
	private double buyout_price;
	private int quantity;
	private double bid_price;
	private String auction_ends;
	private String image_src;
	
	public Product(int pid, int uid, int cid, String name, String description,
			String brand, String model, String dimensions, double buyout_price,
			int quantity, double bid_price, String auction_ends,
			String image_src) {
		super();
		this.pid = pid;
		this.uid = uid;
		this.cid = cid;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.model = model;
		this.dimensions = dimensions;
		this.buyout_price = buyout_price;
		this.quantity = quantity;
		this.bid_price = bid_price;
		this.auction_ends = auction_ends;
		this.image_src = image_src;
	}

	public Product(JSONObject jsonObject) {
		try {
			this.pid = jsonObject.getInt("pid");
			this.uid = jsonObject.getInt("uid");
			this.cid = jsonObject.getInt("cid");
			this.name = jsonObject.getString("name");
			this.description = jsonObject.getString("description");
			this.brand = jsonObject.getString("brand");
			this.model = jsonObject.getString("model");
			this.dimensions = jsonObject.getString("dimensions");
			this.buyout_price = jsonObject.getDouble("buyout_price");
			this.quantity = jsonObject.getInt("quantity");
			this.bid_price = jsonObject.getDouble("bid_price");
			this.auction_ends = jsonObject.getString("auction_ends");
			this.image_src = jsonObject.getString("image_src");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("json", e.getMessage());
		}
	}

	public int getPid() {
		return pid;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPrice() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(buyout_price);
	}
	
	public double getPriceNumber() {
		return buyout_price;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getBid_price() {
		return bid_price;
	}
	
	public String getBid() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(bid_price);
	}

	public void setBid_price(double bid_price) {
		this.bid_price = bid_price;
	}

	public String getAuction_ends() {
		return auction_ends;
	}

	public void setAuction_ends(String auction_ends) {
		this.auction_ends = auction_ends;
	}

	public String getImage_src() {
		return image_src;
	}

	public void setImage_src(String image_src) {
		this.image_src = image_src;
	}

}

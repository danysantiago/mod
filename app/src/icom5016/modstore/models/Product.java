package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Product {
	protected int pid;
	protected User user; //Seller Cambiar por el user
	protected Category category; //Category Object
	protected String name;
	protected String description;
	protected String brand;
	protected String model;
	protected String dimensions;
	protected double buyitnow_price;
	protected int quantity;
	protected double starting_bid_price; 
	protected String auction_ends;
	protected String image_src;
	private String created_Ts
	
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
			this.pid = jsonObject.getInt("product_id");
			if(jsonObject.has("user")) {
				this.user = new User(jsonObject.getJSONObject("user"));
			}
			this.cid = jsonObject.getInt("category_id");
			//category
			this.name = jsonObject.getString("name");
			this.description = jsonObject.getString("description");
			this.brand = jsonObject.getString("brand");
			this.model = jsonObject.getString("model");
			this.dimensions = jsonObject.getString("dimensions");
			this.buyout_price = jsonObject.getDouble("buy_price");
			this.quantity = jsonObject.getInt("quantity");
			this.bid_price = jsonObject.getDouble("starting_bid_price");
			this.auction_ends = jsonObject.getString("auction_end_ts");
			this.image_src = jsonObject.getString("image_src");
			//created_ts
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

	public double getPrice(){
		return this.buyout_price;
	}
	public String getPriceString() {
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
	
	public static Product[] getaProductArrayFromJSON(JSONArray jsonArr) throws JSONException{
		Product[] lp = new Product[jsonArr.length()];
		for(int i = 0; i < jsonArr.length(); i++) {
			lp[i] = new Product(jsonArr.getJSONObject(i));
		}
		return lp;
	}
}

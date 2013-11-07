package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Product {
	protected int id;
	protected int userId; //Seller
	protected int categoryId;
	protected String name;
	protected String description;
	protected String brand;
	protected String model;
	protected String dimensions;
	protected double buyItNowPrice;
	protected int quantity;
	protected double startingBidPrice; 
	protected String auctionEndsTs;
	protected String imageSrcUrl;
	private String createdTs;

	//Optional Values
	protected User user; //Seller
	protected Category category; //Category Object
	
	public Product(int product_id, int user_id, int category_id, String name,
			String description, String brand, String model, String dimensions,
			double buyItNowPrice, int quantity, double startingBidPrice,
			String auctionEndsTs, String imageSrcUrl, String createdTs,
			User user, Category category) {
		super();
		this.id = product_id;
		this.userId = user_id;
		this.categoryId = category_id;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.model = model;
		this.dimensions = dimensions;
		this.buyItNowPrice = buyItNowPrice;
		this.quantity = quantity;
		this.startingBidPrice = startingBidPrice;
		this.auctionEndsTs = auctionEndsTs;
		this.imageSrcUrl = imageSrcUrl;
		this.createdTs = createdTs;
		this.user = user;
		this.category = category;
	}

	public Product(JSONObject json) {
		this.id = json.getInt("product_id");
		this.u
		
	}

//	public int getPid() {
//		return pid;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public String getPrice() {
//		NumberFormat nf = NumberFormat.getInstance();
//		nf.setMinimumFractionDigits(2);
//		// Some logic must be there to decide what price will be shown. 
//		return "$" + nf.format(buyout_price);
//	}
//	
//	public double getPriceNumber() {
//		return buyout_price;
//	}
//
//	public String getBrand() {
//		return brand;
//	}
//
//	public String getModel() {
//		return model;
//	}
//
//	public int getUid() {
//		return uid;
//	}
//
//	public void setUid(int uid) {
//		this.uid = uid;
//	}
//
//	public int getCid() {
//		return cid;
//	}
//
//	public void setCid(int cid) {
//		this.cid = cid;
//	}
//
//	public String getDimensions() {
//		return dimensions;
//	}
//
//	public void setDimensions(String dimensions) {
//		this.dimensions = dimensions;
//	}
//
//	public int getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}
//
//	public double getBid_price() {
//		return bid_price;
//	}
//	
//	public String getBid() {
//		NumberFormat nf = NumberFormat.getInstance();
//		nf.setMinimumFractionDigits(2);
//		// Some logic must be there to decide what price will be shown. 
//		return "$" + nf.format(bid_price);
//	}
//
//	public void setBid_price(double bid_price) {
//		this.bid_price = bid_price;
//	}
//
//	public String getAuction_ends() {
//		return auction_ends;
//	}
//
//	public void setAuction_ends(String auction_ends) {
//		this.auction_ends = auction_ends;
//	}
//
//	public String getImage_src() {
//		return image_src;
//	}
//
//	public void setImage_src(String image_src) {
//		this.image_src = image_src;
//	}

}

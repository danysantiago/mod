package icom5016.modstore.models;

import icom5016.modstore.resources.AndroidResourceFactory;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Serializable {
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
	protected String createdTs;
	
	private Date createdTsDate;
	private Date auctionEndsTsDate;

	//Optional Values
	protected User user; //Seller
	protected Category category; //Category Object
	
	//Optional Values for Buying
	
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
		
		this.createdTsDate = AndroidResourceFactory.ISODateToDate(createdTs);
		this.auctionEndsTsDate = AndroidResourceFactory.ISODateToDate(auctionEndsTs);
	}

	public Product(JSONObject json) throws JSONException {
		this.id = json.getInt("product_id");
		this.userId = json.getInt("user_id");
		this.categoryId = json.getInt("category_id");
		this.description = json.getString("description");
		this.name = json.getString("name");
		this.brand = json.getString("brand");
		this.model = json.getString("model");
		this.dimensions = json.getString("dimensions");
		
		if(json.getString("buy_price").equals("null")){
			this.buyItNowPrice = -1.0;
		}
		else{
			this.buyItNowPrice = json.getDouble("buy_price");
		}
		
		this.quantity = json.getInt("quantity");
		
		if(json.getString("starting_bid_price").equals("null")){
			this.startingBidPrice = -1.0;
		}
		else{
			this.startingBidPrice = json.getDouble("starting_bid_price");
		}
		
		
		this.auctionEndsTs = json.getString("auction_end_ts");
		this.createdTs = json.getString("created_ts");
		
		this.createdTsDate = AndroidResourceFactory.ISODateToDate(createdTs);
		if(!auctionEndsTs.equals("null")) {
			this.auctionEndsTsDate = AndroidResourceFactory.ISODateToDate(auctionEndsTs);
		}
		
		if(json.has("image_src"))
			this.imageSrcUrl = json.getString("image_src");
		
		if(json.has("user"))
			this.user = new User(json.getJSONObject("user"));
		if(json.has("category"))
			this.category = new Category(json.getJSONObject("category"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return AndroidResourceFactory.stringEncode(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return AndroidResourceFactory.stringEncode(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public double getBuyItNowPrice() {
		return buyItNowPrice;
	}

	public void setBuyItNowPrice(double buyItNowPrice) {
		this.buyItNowPrice = buyItNowPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getStartingBidPrice() {
		return startingBidPrice;
	}

	public void setStartingBidPrice(double startingBidPrice) {
		this.startingBidPrice = startingBidPrice;
	}

	public String getAuctionEndsTs() {
		return auctionEndsTs;
	}

	public void setAuctionEndsTs(String auctionEndsTs) {
		this.auctionEndsTs = auctionEndsTs;
	}

	public String getImageSrcUrl() {
		return imageSrcUrl;
	}

	public void setImageSrcUrl(String imageSrcUrl) {
		this.imageSrcUrl = imageSrcUrl;
	}

	public String getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getAuctionEndsTsString(){
		return (this.auctionEndsTsDate == null) ? "" : AndroidResourceFactory.dateToString(this.auctionEndsTsDate);
	}
	
	public String getDateFormatedString(){
		return AndroidResourceFactory.dateToString(this.createdTsDate);
	}

	public String getBuyItNowPriceString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(this.buyItNowPrice);
	}
	
	public String getStartingBidPriceString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(this.startingBidPrice);
	}
	
	public Date getAuctionEndDate() {
		return auctionEndsTsDate;
	}

}

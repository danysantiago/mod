package icom5016.modstore.models;

import icom5016.modstore.resources.AndroidResourceFactory;

import java.text.NumberFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Bids {
	private double bidAmount;
	private String username;
	private User biddingUser;
	private int userId;
	private int productId;
	private Product product;
	private Date bidDate;
	
	
	public Bids(double bidAmount, String username, User biddingUser, int userId,
			int productId, Product product) {
		this.bidAmount = bidAmount;
		this.username = username;
		this.biddingUser = biddingUser;
		this.userId = userId;
		this.productId = productId;
		this.product = product;
	}

	public Bids(JSONObject bids) throws JSONException{
		if(bids.has("user_id"))
			this.userId = bids.getInt("user_id");
		if(bids.has("user"))
			this.biddingUser = new User(bids.getJSONObject("user"));
		
		if(bids.has("product_id"))
			this.productId = bids.getInt("product_id");
		if(bids.has("product"))
			this.product = new Product(bids.getJSONObject("product"));
		
		if(bids.has("username"))
			this.username = bids.getString("username");
		
		if(bids.has("bid_amount"))
			this.bidAmount = bids.getDouble("bid_amount");
		
		if(bids.has("created_ts")){
			this.bidDate = AndroidResourceFactory.ISODateToDate(bids.getString("created_ts"));
		}
		
	}

	public Date getBidDate() {
		return bidDate;
	}
	public String getBidDateString() {
		return AndroidResourceFactory.dateToString(bidDate);
	}

	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}

	public double getBidAmount() {
		return bidAmount;
	}

	public String getBidAmountString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format(this.bidAmount);
	}

	public void setBidAmount(double bidAmount) {
		this.bidAmount = bidAmount;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public User getBiddingUser() {
		return biddingUser;
	}


	public void setBiddingUser(User biddingUser) {
		this.biddingUser = biddingUser;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}

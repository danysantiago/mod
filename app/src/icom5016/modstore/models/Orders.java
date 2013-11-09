package icom5016.modstore.models;

import icom5016.modstore.resources.AndroidResourceFactory;

import java.text.NumberFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Orders {
	//Needed
	private String createdTs;
	private int orderId;
	private int addressId;
	private int userId;
	private int creditcardId;
	
	
	//Optional Parameters
	
	//Models
	private User user;
	private CreditCard creditCard;
	private Address address;
	
	//Optional
	private int detailSize;
	private double orderTotal;
	
	private Date createdTsDate;
	
	public Orders(String createdTs, int orderId, int addressId, int userId,
			int creditcardId, User user, CreditCard creditCard,
			Address address, int detailSize, double orderTotal) {
		super();
		this.createdTs = createdTs;
		this.createdTsDate = AndroidResourceFactory.ISODateToDate(createdTs);
		this.orderId = orderId;
		this.addressId = addressId;
		this.userId = userId;
		this.creditcardId = creditcardId;
		this.user = user;
		this.creditCard = creditCard;
		this.address = address;
		this.detailSize = detailSize;
		this.orderTotal = orderTotal;
	}

	
	public Orders(JSONObject json) throws JSONException{
		this.createdTs = json.getString("created_ts");
		this.createdTsDate = AndroidResourceFactory.ISODateToDate(createdTs);
		this.orderId = json.getInt("order_id");
		this.addressId = json.getInt("address_id");
		this.creditcardId = json.getInt("credit_card_id");
		
		if(json.has("user"))
			this.user = new User(json.getJSONObject("user"));
		if(json.has("creditcard"))
			this.creditCard = new CreditCard(json.getJSONObject("creditcard"));
		if(json.has("address"))
			this.address = new Address(json.getJSONObject("address"));
		
		if(json.has("order_total"))
			this.orderTotal = json.getDouble("order_total");
		
		if(json.has("details_size"))
			this.detailSize = json.getInt("details_size");
		
	}


	public String getCreatedTs() {
		return createdTs;
	}


	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public int getAddressId() {
		return addressId;
	}


	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getCreditcardId() {
		return creditcardId;
	}


	public void setCreditcardId(int creditcardId) {
		this.creditcardId = creditcardId;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public CreditCard getCreditCard() {
		return creditCard;
	}


	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public int getDetailSize() {
		return detailSize;
	}


	public void setDetailSize(int detailSize) {
		this.detailSize = detailSize;
	}


	public double getOrderTotal() {
		return orderTotal;
	}


	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}


	public String getOrderTotalString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(orderTotal);
	}
	
	public String getDateFormatedString(){
		return AndroidResourceFactory.dateToString(this.createdTsDate);
	}
	
}

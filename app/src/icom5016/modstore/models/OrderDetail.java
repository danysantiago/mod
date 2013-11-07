package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetail {
	private int id;
	private int orderId;
	private Address shippingAddress;
	private User seller;
	private User buyer;
	private int quantity;
	private double price;
	private String trackingNumber;
	private Product product;
	
	
	public OrderDetail(int id, int orderId, Address shippingAddress,
			User seller, User buyer, int quantity, double price,
			String trackingNumber, Product product, String type, String timestamp) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.shippingAddress = shippingAddress;
		this.seller = seller;
		this.buyer = buyer;
		this.quantity = quantity;
		this.price = price;
		this.trackingNumber = trackingNumber;
		this.product = product;

	}

	public OrderDetail(JSONObject json) throws JSONException {
		super();
			this.id = json.getInt("order_detail_id");
			this.orderId = json.getInt("order_id");
			this.shippingAddress = new Address(json.getJSONObject("address"));
			this.seller = new User(json.getJSONObject("seller"));
			this.buyer = new User(json.getJSONObject("buyer"));
			this.quantity = json.getInt("quantity");
			this.price = json.getDouble("price");
			this.trackingNumber = json.getString("tracking_number");
			this.product = new Product(json.getJSONObject("product"));
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(price);
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
}

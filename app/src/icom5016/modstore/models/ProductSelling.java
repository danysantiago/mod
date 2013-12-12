package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductSelling extends Product {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int stock;
	private String trackingNumber = null;
	private int qunatityBought;
	private double totalPrice;
	private int orderId;
	private int orderDeteailtId;

	public ProductSelling(JSONObject json) throws JSONException {
		super(json);
		if(json.has("stock"))
			this.stock = json.getInt("stock");
		if(json.has("tracking_number"))
			this.trackingNumber = json.getString("tracking_number");
		if(json.has("order_quantity"))
			this.qunatityBought = json.getInt("order_quantity");
		if(json.has("total_price"))
			this.totalPrice = json.getDouble("total_price");
		if(json.has("order_id"))
			this.orderId = json.getInt("order_id");
		if(json.has("order_detail_id"))
			this.orderId = json.getInt("order_detail_id");
		
		
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderDeteailtId() {
		return orderDeteailtId;
	}

	public void setOrderDeteailtId(int orderDeteailtId) {
		this.orderDeteailtId = orderDeteailtId;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public int getQunatityBought() {
		return qunatityBought;
	}

	public void setQunatityBought(int qunatityBought) {
		this.qunatityBought = qunatityBought;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTotalPriceString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(this.totalPrice);
	}

}

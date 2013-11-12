package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetail {
	
	//Essential
	private int id;
	private int orderId;
	private int productId;
	private int quantity;
	private double finalPrice;
	private String trackingNumber;
	private String createdTs;
	private boolean wonByBid;
	
	//Optional
	private Product product;
	

	public OrderDetail(int id, int orderId, int productId, int quantity,
			double finalPrice, String trackingNumber, String createdTs,
			Product product) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.finalPrice = finalPrice;
		this.trackingNumber = trackingNumber;
		this.createdTs = createdTs;
		this.product = product;
	}




	public OrderDetail(JSONObject json) throws JSONException {
		super();
			this.id = json.getInt("order_detail_id");
			this.orderId = json.getInt("order_id");
			this.productId = json.getInt("product_id");
			this.quantity = json.getInt("quantity");
			this.finalPrice = json.getDouble("final_price");
			this.trackingNumber = json.getString("tracking_number");
			this.createdTs = json.getString("created_ts");
			
			//Optional
			if(json.has("product"))
				this.product = new Product(json.getJSONObject("product"));
			
			this.wonByBid = json.getBoolean("won_by_bid");
		
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




	public int getProductId() {
		return productId;
	}




	public void setProductId(int productId) {
		this.productId = productId;
	}




	public int getQuantity() {
		return quantity;
	}




	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}




	public double getFinalPrice() {
		return finalPrice;
	}




	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}




	public String getTrackingNumber() {
		return trackingNumber;
	}




	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}




	public String getCreatedTs() {
		return createdTs;
	}




	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}



	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getFinalSoldPriceString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(finalPrice);
	}
	
	public String getTotalPriceString(){
		double tp = quantity * this.finalPrice;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(tp);
	}




	public boolean isWonByBid() {
		return wonByBid;
	}
	
	
}

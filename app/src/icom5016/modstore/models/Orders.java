package icom5016.modstore.models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Orders {
	private String orderTS;
	private List<OrderDetail> productsDetails;
	private double totalPrice;
	private int id;
	
	public Orders(int id, String orderTS, List<OrderDetail> productsDetails, double totalPrice) {
		this.id = id;
		this.orderTS = orderTS;
		this.productsDetails = productsDetails;
		this.totalPrice = totalPrice;
	}

	public Orders(JSONObject json) throws JSONException{
		this.id = json.getInt("order_id");
		
		this.orderTS = json.getString("orders_ts");
		
		this.productsDetails = new ArrayList<OrderDetail>();
		
		JSONArray jsonArray = json.getJSONArray("order_details");
		for(int i=0; i<jsonArray.length(); i++){
			this.productsDetails.add(new OrderDetail(jsonArray.getJSONObject(i)));
		}
		
		this.totalPrice = json.getDouble("total_price");
		
	}
	
	
	public int getId() {
		return id;
	}

	public String getTotalPrice() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(totalPrice);
	}

	public String getOrderTS() {
		return orderTS;
	}

	public List<OrderDetail> getProductsDetails() {
		return productsDetails;
	}
	
}

package icom5016.modstore.models;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Orders {
	public String orderTS;
	public List<OrderDetail> productsDetails;
	
	public Orders(String orderTS, List<OrderDetail> productsDetails) {
		this.orderTS = orderTS;
		this.productsDetails = productsDetails;
	}

	public Orders(JSONObject json){
		this.orderTS = json.getString("orders_ts");
		
		JSONArray jsonArray = json.getJSONArray("order_details");
		for(int i)
		
	}
	
	public Date getOrderTS() {
		return orderTS;
	}

	public List<OrderDetail> getProductsDetails() {
		return productsDetails;
	}
	
	
	
	
}

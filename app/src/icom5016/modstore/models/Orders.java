package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class Orders {
	private String created_ts;
	private double order_total;
	private int order_id;
	private int detail_size;
	
	
	public Orders(String created_ts, double order_total, int order_id,
			int detail_size) {
		super();
		this.created_ts = created_ts;
		this.order_total = order_total;
		this.order_id = order_id;
		this.detail_size = detail_size;
	}
	
	public Orders(JSONObject json) throws JSONException{
		this.created_ts = json.getString("created_ts");
		this.order_total = json.getDouble("order_total");
		this.detail_size = json.getInt("detail_size");
		this.order_id = json.getInt("order_id");
	}

	public String getCreated_ts() {
		return created_ts;
	}

	public double getOrder_total() {
		return order_total;
	}

	public int getOrder_id() {
		return order_id;
	}

	public int getDetail_size() {
		return detail_size;
	}
	
	public String getOrderTotalString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(order_total);
	}
	
	
	
	
}

package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductSearching extends Product {
	
	protected double actualBid;
	protected int stock;
	protected double aveSellerRating;
	
	public ProductSearching(JSONObject json) throws JSONException {
		super(json);
		String tmpBid =json.getString("actual_bid");
		if(tmpBid.equals("null"))
			this.actualBid = -1.0;
		else
			this.actualBid =json.getDouble("actual_bid");
	}

	public double getActualBid() {
		return actualBid;
	}

	public void setActualBid(double actualBid) {
		this.actualBid = actualBid;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getAveSellerRating() {
		return aveSellerRating;
	}

	public void setAveSellerRating(double aveSellerRating) {
		this.aveSellerRating = aveSellerRating;
	}
	
	public String getActualBidString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(this.actualBid);
	}
	

}

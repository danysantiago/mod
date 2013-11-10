package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductBidding extends Product {

	private double myLastBid;
	private double currentBid;
	
	public ProductBidding(JSONObject json) throws JSONException {
		super(json);
		this.myLastBid = json.getDouble("my_last_bid");
		if(json.has("current_bid"))
			this.currentBid = json.getDouble("current_bid");
		if(json.has("max_bid"))
			this.currentBid = json.getDouble("max_bid");
	}


	public double getMyLastBid() {
		return myLastBid;
	}

	public void setMyLastBid(double myLastBid) {
		this.myLastBid = myLastBid;
	}

	public double getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
	}

	
	public String getMyLastBidString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(this.myLastBid);
	}
	
	public String getCurrentMaxBidString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(this.currentBid);
	}
}

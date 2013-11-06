package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductBuying extends Product {

	
	private double myLastBidAmount;
	private String myLastBidAmountDate;
	
	
	public ProductBuying(int pid, int uid, int cid, String name,
			String description, String brand, String model, String dimensions,
			double buyout_price, int quantity, double bid_price,
			String auction_ends, String image_src, double myLastBidAmount,
			String myLastBidAmountDate) {
		super(pid, uid, cid, name, description, brand, model, dimensions,
				buyout_price, quantity, bid_price, auction_ends, image_src);
		this.myLastBidAmount = myLastBidAmount;
		this.myLastBidAmountDate = myLastBidAmountDate;
	}
	
	public ProductBuying(JSONObject json) throws JSONException{
		super(json.getJSONObject("product"));
		JSONObject extraData = json.getJSONObject("myLastBid");
		this.myLastBidAmount = extraData.getDouble("amount");
		this.myLastBidAmountDate = extraData.getString("createdTS");
	}

	public double getMyLastBidAmount() {
		return myLastBidAmount;
	}

	public String getMyLastBidAmountDate() {
		return myLastBidAmountDate;
	}
	
	
	public String getMyLastBidAmountString(){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(myLastBidAmount);
	}
	
}

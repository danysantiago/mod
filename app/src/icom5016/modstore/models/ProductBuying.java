package icom5016.modstore.models;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductBuying extends Product {

	protected double currBidPrice;
	protected double myCurrBid;
	protected String type;
	
	public static final String BUYING_BIDDING = "bidding";
	public static final String BUYING_NOTWIN = "didNotWin";
	
	public ProductBuying(int pid, int uid, int cid, String name,
			String description, String brand, String model, String dimensions,
			double buyout_price, int quantity, double bid_price,
			String auction_ends, String image_src, double currBidPrice, double myCurrBid, String type) {
		super(pid, uid, cid, name, description, brand, model, dimensions, buyout_price,
				quantity, bid_price, auction_ends, image_src);
		this.currBidPrice = currBidPrice;
		this.myCurrBid = myCurrBid;
		this.type = type;
	}



	public ProductBuying(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		this.currBidPrice = jsonObject.getDouble("current_bid");
		this.myCurrBid = jsonObject.getDouble("my_current_bid");
		
	}

	public double getCurrBidPrice() {
		return currBidPrice;
	}

	public double getMyCurrBid() {
		return myCurrBid;
	}
	
	public String getCurrBidPriceString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(currBidPrice);
	}

	public String getMyCurrBidString() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		// Some logic must be there to decide what price will be shown. 
		return "$" + nf.format(myCurrBid);
	}
	
	public String getType() {
		return type;
	}
	
}

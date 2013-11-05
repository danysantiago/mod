package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductSelling extends Product {

	public static final String BUY_NOW = "buyItNowType";
	public static final String BID = "bidType";
	protected String sell_type;
	
	public ProductSelling(int pid, int uid, int cid, String name,
			String description, String brand, String model, String dimensions,
			double buyout_price, int quantity, double bid_price,
			String auction_ends, String image_src, String sell_type) {
		super(pid, uid, cid, name, description, brand, model, dimensions,
				buyout_price, quantity, bid_price, auction_ends, image_src);
		
		this.sell_type = sell_type;
		
	}

	public ProductSelling(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		this.sell_type = jsonObject.getString("sell_type");
	}

	public String getSellType() {
		return sell_type;
	}

}

package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductCart extends Product {

	
	private static final long serialVersionUID = 1L;
	private int cartQuantity;
	
	public ProductCart(JSONObject json) throws JSONException {
		super(json);
		if(json.has("cart_quantity"))
			cartQuantity = json.getInt("cart_quantity");
		
	}

	public int getCartQuantity() {
		return cartQuantity;
	}

	public void setCartQuantity(int cartQuantity) {
		this.cartQuantity = cartQuantity;
	}

}

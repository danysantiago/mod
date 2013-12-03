package icom5016.modstore.adapter;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.ProductCart;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CartAdapter extends ArrayAdapter<ProductCart> {
	
	private ImageLoader imageloader;

	public CartAdapter(Context context, JSONArray jsonArr) throws JSONException {
		super(context, R.layout.listview_product_listing);
		
		imageloader = new ImageLoader(context);
		
		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new ProductCart(jsonArr.getJSONObject(i)));
		}
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ProductCart product = (ProductCart) this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_product_listing, parent, false);
		
		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_title);
		ImageView image = (ImageView) row.findViewById(R.id.prodrow_image);
		TextView price = (TextView) row.findViewById(R.id.prodrow_price);
		TextView date = (TextView) row.findViewById(R.id.prodrow_date);
		TextView type = (TextView) row.findViewById(R.id.prodrow_type);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_date_tittle);
		
		if(product.getStartingBidPrice() != -1.0){
			type.setText("Bid Now");
			date.setText(product.getAuctionEndsTsString());
		}
		else{
			type.setText("Buy It Now");
			price.setText(product.getBuyItNowPriceString());
			date.setVisibility(View.GONE);
			quantity.setText("Quantity: "+ product.getQuantity());
		}
		
		title.setText(product.getName());
		
		if (product.getImageSrcUrl() != null) {
			String url = Server.Images.GET + product.getImageSrcUrl();

			imageloader.DisplayImage(url, image);
			image.setTag(url);
		}

		row.setTag(product);

		return row;
	}
}

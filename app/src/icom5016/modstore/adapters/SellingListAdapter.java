package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.ProductSelling;
import icom5016.modstore.resources.ConstantClass;

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

public class SellingListAdapter extends ArrayAdapter<ProductSelling> {

	private ImageLoader imageloader;
	private String type;

	public SellingListAdapter(Context context, JSONArray list, String type ) throws JSONException {
		super(context, R.layout.listview_orderdetails_row);
		this.type = type;
		imageloader = new ImageLoader(context);
		
			for(int i=0; i<list.length(); i++){
				this.add(new ProductSelling(list.getJSONObject(i)));
			}
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ProductSelling product = this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_orderdetails_row, parent, false);
		
		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_orders_title);
		ImageView image = (ImageView) row.findViewById(R.id.prodrow_orders_image);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_orders_quantity);
		TextView tracking = (TextView) row.findViewById(R.id.prodrow_orders_tracking);
		TextView price = (TextView) row.findViewById(R.id.prodrow_orders_price);
		TextView date = (TextView) row.findViewById(R.id.prodrow_orders_date);
		TextView typeView = (TextView) row.findViewById(R.id.prodrow_orders_type);
		
		title.setText(product.getName());
		if(type.equals(ConstantClass.SELLING_ACTIVE)){
			quantity.setText("Stock: "+product.getStock());
			tracking.setVisibility(View.GONE);
			if(product.getStartingBidPrice() != -1.0){
				typeView.setText("Bidding");
				price.setText(product.getStartingBidPriceString());
				date.setText(product.getAuctionEndsTsString());
			}
			else{
				typeView.setText("Buy It Now");
				price.setText(product.getBuyItNowPriceString());
				date.setVisibility(View.GONE);
				row.findViewById(R.id.prodrow_orders_date_tittle).setVisibility(View.GONE);
			}
		}
		else if(type.equals(ConstantClass.SELLING_NOTSOLD)){
			quantity.setText("Quantity: "+product.getQuantity());
			tracking.setVisibility(View.GONE);
			date.setVisibility(View.GONE);
			row.findViewById(R.id.prodrow_orders_date_tittle).setVisibility(View.GONE);
			if(product.getStartingBidPrice() != -1.0){
				typeView.setText("Bidding");
				price.setText(product.getStartingBidPriceString());
			}
			else{
				typeView.setText("Buy It Now");
				price.setText(product.getBuyItNowPriceString());
				
			}
			
		}
		else if(type.equals(ConstantClass.SELLING_SOLD)){
			quantity.setText("Quantity: "+product.getQunatityBought());
			price.setText(product.getTotalPriceString());
			date.setVisibility(View.GONE);
			row.findViewById(R.id.prodrow_orders_date_tittle).setVisibility(View.GONE);
			if(!product.getTrackingNumber().equals("null"))
				tracking.setText("Tracking Number:"+product.getTrackingNumber());
			else
				tracking.setText("Tracking Number: N/A");
		}
		
		
	
		
//		if (product.getImageSrcUrl() != null) {
//			String url = Server.Images.GET + product.getImageSrcUrl();
//
//			imageloader.DisplayImage(url, image);
//			image.setTag(url);
//		}
		
		
		return row;
	}

}

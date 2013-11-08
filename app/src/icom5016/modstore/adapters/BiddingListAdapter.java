package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.ProductBidding;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BiddingListAdapter extends ArrayAdapter<ProductBidding> {

	private ImageLoader imageloader;
	private String type;

	public BiddingListAdapter(Context context, JSONArray list, String type) throws JSONException {
		super(context, R.layout.listview_orderdetails_row);
		
		imageloader = new ImageLoader(context);
		this.type = type;
		
		for(int i=0; i<list.length(); i++){
			this.add(new ProductBidding(list.getJSONObject(i)));
		}
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ProductBidding product = this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_orderdetails_row, parent, false);
		
		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_orders_title);
		ImageView image = (ImageView) row.findViewById(R.id.prodrow_orders_image);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_orders_quantity);
		TextView status = (TextView) row.findViewById(R.id.prodrow_orders_tracking);
		TextView bidEndCurr = (TextView) row.findViewById(R.id.prodrow_orders_price);
		TextView endDate = (TextView) row.findViewById(R.id.prodrow_orders_date);
		TextView myBidCurr = (TextView) row.findViewById(R.id.prodrow_orders_type);
		
		
		//Title
		title.setText(product.getName());
		
		//Remove Visibility
		quantity.setVisibility(View.GONE);
		
		if(product.getMyLastBid() >= product.getCurrentBid())
			status.setText("Winning");
		else
			status.setText("Losing");
		status.setTypeface(null, Typeface.BOLD);
		
		if(type.equals(ConstantClass.BUYING_NOTWIN)){
			endDate.setText("Bid Ended");
			
		}
		else if(type.equals(ConstantClass.BUYING_BIDDING)){
			endDate.setText("End Date: "+product.getAuctionEndsTsString());
		}
		
		//Current Bid/End Price:
		bidEndCurr.setText("Last Bid: "+product.getCurrentBidString());
		myBidCurr.setText("My Bid: "+product.getMyLastBidString());
		
		
		
		//Images
		//imageloader.DisplayImage(product.getImage_src(), image);
		
		row.setTag(product);

		return row;
	}
	
}

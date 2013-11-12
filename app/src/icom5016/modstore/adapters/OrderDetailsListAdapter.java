package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.OrderDetail;
import icom5016.modstore.models.Product;

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

public class OrderDetailsListAdapter extends ArrayAdapter<OrderDetail> {

	private ImageLoader imageloader;
	
	public OrderDetailsListAdapter(Context context, JSONArray jsonArray) throws JSONException {
		super(context, R.layout.listview_orderdetails_row);
		
		imageloader = new ImageLoader(context);
		
		for(int i=0; i<jsonArray.length(); i++){
			this.add(new OrderDetail(jsonArray.getJSONObject(i)));
		}
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		OrderDetail orderDetail = this.getItem(position);
		Product product = orderDetail.getProduct();

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_orderdetails_row, parent, false);

		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_orders_title);
		ImageView image = (ImageView) row.findViewById(R.id.prodrow_orders_image);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_orders_quantity);
		TextView tracking = (TextView) row.findViewById(R.id.prodrow_orders_tracking);
		TextView price = (TextView) row.findViewById(R.id.prodrow_orders_price);
		TextView date = (TextView) row.findViewById(R.id.prodrow_orders_date);
		TextView type = (TextView) row.findViewById(R.id.prodrow_orders_type);
		
		
		date.setVisibility(View.GONE);
		
		if(product.getStartingBidPrice() <= -1.0){
			type.setText("Auction");
		}else{
			type.setText("Bought");
		}
		
		
		title.setText(product.getName());
		if(!orderDetail.getTrackingNumber().equals("null"))
			tracking.setText(orderDetail.getTrackingNumber());
		
		price.setText(orderDetail.getFinalSoldPriceString());
		quantity.setText("Quantity: "+orderDetail.getQuantity());
		
		row.setTag(orderDetail);
		
		//TODO: Uncoment when fix
		if (product.getImageSrcUrl() != null) {
			String url = Server.Images.GET + product.getImageSrcUrl();

			imageloader.DisplayImage(url, image);
			image.setTag(url);
		}
		
		return row;
	}

}

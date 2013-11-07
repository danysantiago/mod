package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.models.OrderDetail;

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

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_ordereddetail_row, parent, false);

		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_orders_title);
	//	ImageView image = (ImageView) row.findViewById(R.id.prodrow_orders_image);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_orders_quantity);
	//	TextView tracking = (TextView) row.findViewById(R.id.prodrow_orders_tracking);
		TextView price = (TextView) row.findViewById(R.id.prodrow_orders_price);
		//TextView date = (TextView) row.findViewById(R.id.prodrow_orders_type);
		
		title.setText(orderDetail.getProduct().getName());
		quantity.setText("Quantity: "+orderDetail.getQuantity());
		
	//	if(!orderDetail.getTrackingNumber().equals(""))
		//	tracking.setText("Tracking Number: "+orderDetail.getTrackingNumber());
		
		price.setText(orderDetail.getFinalSoldPriceString());
		
	//	date.setText(orderDetail.getItemTs());
				
		
		row.setTag(orderDetail);

		return row;
	}

}

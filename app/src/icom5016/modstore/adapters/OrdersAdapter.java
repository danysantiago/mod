package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Orders;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrdersAdapter extends ArrayAdapter<Orders> {
	
	
	
	public OrdersAdapter(Context context, JSONArray jsonArray) throws JSONException{
		super(context, R.layout.listview_orders_row);
		
		//Add to list
		for(int i=0; i<jsonArray.length(); i++){
			this.add(new Orders(jsonArray.getJSONObject(i)));
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		Orders order = this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_orders_row, parent, false);

		//Layout Vars
		TextView num = (TextView) row.findViewById(R.id.ordersrow_num);
		TextView quantity = (TextView) row.findViewById(R.id.ordersrow_quantity);
		TextView total = (TextView) row.findViewById(R.id.ordersrow_total);
		TextView date = (TextView) row.findViewById(R.id.ordersrow_date);
		
		num.setText("Order # "+Integer.toString(order.getOrder_id()) );
		quantity.setText("Quantity: "+order.getDetail_size());
		total.setText("Total "+ order.getOrderTotalString());
		date.setText("Date: "+order.getCreated_ts());
				
		
		row.setTag(order);

		return row;
	}
}

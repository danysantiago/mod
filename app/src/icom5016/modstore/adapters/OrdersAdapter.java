package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.Server.Orders;

import org.json.JSONArray;

import android.content.Context;
import android.widget.ArrayAdapter;

public class OrdersAdapter extends ArrayAdapter<Orders> {
	
	
	public OrdersAdapter(Context context, JSONArray jsonArray){
		super(context, R.layout.activity_forgot_layout);
	}
}

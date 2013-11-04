package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.Server.Products;

import org.json.JSONArray;

import android.content.Context;
import android.widget.ArrayAdapter;

public class BuySellListAdapter extends ArrayAdapter<Products> {

	public BuySellListAdapter(Context context, JSONArray jsonArray) {
		super(context, R.layout.activity_forgot_layout);
		// TODO Auto-generated constructor stub
	}

}

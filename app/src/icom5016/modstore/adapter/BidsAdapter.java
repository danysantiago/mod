package icom5016.modstore.adapter;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Bids;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BidsAdapter extends ArrayAdapter<Bids> {
	
	
	public BidsAdapter(Context context, JSONArray bids) throws JSONException{
		super(context, R.layout.listview_bidsrow);
		
		for(int i=0; i<bids.length(); i++){
			this.add(new Bids(bids.getJSONObject(i)));
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflator.inflate(R.layout.listview_bidsrow, parent, false);
		Bids bid = this.getItem(position);
		
		
		TextView username = (TextView) row.findViewById(R.id.bids_username);
		TextView date = (TextView) row.findViewById(R.id.bids_date);
		TextView price = (TextView) row.findViewById(R.id.bids_price);
		
		username.setText(bid.getUsername());
		date.setText(bid.getBidDateString());
		price.setText(bid.getBidAmountString());
		
		
		return row;
	}

}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.BidsAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BidsFragment extends Fragment {
	
	private int sellerId;
	private int productId;
	private ListView lvBids;
	private TextView tvBids;
	private ProgressBar pbBids;
	private GridLayout glBids;
	private MainActivity ma;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bids, container, false);
		
		Bundle bnd = this.getArguments();
		productId = bnd.getInt(ConstantClass.PRODUCT_KEY);
		sellerId = bnd.getInt(ConstantClass.SELLER_KEY);
		//Instance Views
		this.lvBids = (ListView) view.findViewById(R.id.bidsLv);
		this.tvBids = (TextView) view.findViewById(R.id.bids_load_tv);
		this.pbBids = (ProgressBar) view.findViewById(R.id.bids_pb);
		this.glBids = (GridLayout) view.findViewById(R.id.bidsLoader);
		this.ma = (MainActivity) this.getActivity();
		
		doHttpBids();
		
		return view;
	}

	private void doHttpBids() {
		
		Uri.Builder url = Uri.parse(Server.Orders.GETBIDS).buildUpon();
		url.appendQueryParameter("sellerId", Integer.toString(sellerId));
		url.appendQueryParameter("productId", Integer.toString(productId));
		
		Bundle params = new Bundle();
		params.putString("url", url.toString());
		params.putString("method", "GET");
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				
				try {
					JSONArray listBids = json.getJSONArray("bids_list");
					
					if(listBids.length() == 0){
						pbBids.setVisibility(View.GONE);
						tvBids.setText(R.string.no_bids);
					}
					else{
						glBids.setVisibility(View.GONE);
						lvBids.setAdapter(new BidsAdapter(ma, listBids));
						lvBids.setVisibility(View.VISIBLE);
					}
					
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}
		
		});
		request.execute();
	}

}

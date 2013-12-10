package icom5016.modstore.fragments;


import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.CartAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.CartListener;

import java.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartFragment extends Fragment implements View.OnClickListener{

	
	private ListView cartListLv;
	private LinearLayout cartListEmpty;
	private TextView cartTotal;
	private ProgressDialog pd;
	private MainActivity ma;
	private Button btn;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cart, container, false);
		
		this.cartListLv = (ListView) view.findViewById(R.id.cart_lv);
		this.cartListEmpty = (LinearLayout) view.findViewById(R.id.cart_empty);
		this.cartTotal = (TextView) view.findViewById(R.id.cartTotal);
		ma = (MainActivity) this.getActivity();
		pd = ma.processDialog;
		
		
		//Set Btn Listener
		btn = (Button) view.findViewById(R.id.cart_checkout);
		btn.setOnClickListener(this);
		//btn.setFocusable(false);
		//btn.setClickable(false);
		
		pd.setMessage(getResources().getString(R.string.cart_pd));
		pd.show();
		this.doHttpCart();
		return view;
	}

	
	
	private void doHttpCart() {
		//Perform http request
		Uri.Builder urlB = Uri.parse(Server.Cart.CART).buildUpon(); 

		if(ma.getActiveUser() == null){
			Toast.makeText(ma, "Invalid Access to Intent", Toast.LENGTH_SHORT).show();
			return;
		}
		urlB.appendQueryParameter("userId", Integer.toString(ma.getActiveUser().getGuid()));
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", urlB.toString());	
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					//Get array of products
					JSONArray jsonArr = json.getJSONArray("results");

					if(jsonArr.length() == 0){
						cartListEmpty.setVisibility(View.VISIBLE);
						cartTotal.setText("$0.00");
						btn.setFocusable(false);
						btn.setClickable(false);
					}
					else{
						//Pass it to adapter and to List View
						CartAdapter adapter = new CartAdapter(ma, jsonArr);
						cartListLv.setAdapter(adapter);
						
						//Change View Visibility
						cartListLv.setOnItemClickListener(new CartListener((MainActivity) getActivity(), ma.getActiveUser().getGuid() ) );
						cartListLv.setVisibility(View.VISIBLE);
						
						double total = json.getDouble("total");
						NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
						cartTotal.setText("$" + nf.format(total));
						btn.setFocusable(true);
						btn.setClickable(true);
						
					}
					

				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(ma, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cart_checkout:
			ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new CartCheckoutFragment());
			break;
		}
	}


	
	
	
}
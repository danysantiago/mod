package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.OrdersAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.OrdersListener;
import icom5016.modstore.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MyOrdersListFragment extends Fragment {
	//User Instance Field
	private User activeUser;
	private MainInterfaceActivity mainActivity;
	private ListView lvOrders;
	private TextView tvOrders;
	private ProgressBar pbOrders;
	private GridLayout glOrders;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_myorders_list, container,false);
		
		//Instance Vars
		this.mainActivity = (MainInterfaceActivity) this.getActivity();
		this.activeUser = this.mainActivity.getActiveUser();
		
		//Instance Views
		this.lvOrders = (ListView) view.findViewById(R.id.orders_lv);
		this.tvOrders = (TextView) view.findViewById(R.id.orders_load_tv);
		this.pbOrders = (ProgressBar) view.findViewById(R.id.orders_pb);
		this.glOrders = (GridLayout) view.findViewById(R.id.orders_loader);
		
		//Request to HTTP
		try {
			doHttpOrdersRequest();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return view;
	}
	
	
	public void doHttpOrdersRequest() throws JSONException{
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERS+"?userId="+this.activeUser.getGuid());
		params.putString("method", "GET");
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				
				try {
					JSONArray listOrders = json.getJSONArray("orders_list");
					
					if(listOrders.length() == 0){
						pbOrders.setVisibility(View.GONE);
						tvOrders.setText(R.string.listbuysell_no_item);
					}
					else{
						glOrders.setVisibility(View.GONE);
						lvOrders.setAdapter(new OrdersAdapter(mainActivity, listOrders));
						lvOrders.setOnItemClickListener(new OrdersListener(mainActivity));
						lvOrders.setVisibility(View.VISIBLE);
					}
					
					
				} catch (JSONException e) {
					Toast.makeText(mainActivity, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(mainActivity, R.string.errmsg_no_connection, Toast.LENGTH_LONG).show();
			}
		
		});
		request.execute();
	}
}

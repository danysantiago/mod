package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.OrderDetailsListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.OrderDetailsListListener;
import icom5016.modstore.models.Address;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.resources.ConstantClass;

import java.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderDetailsListFragment extends Fragment {
	
	private int orderId;
	//Placeholders
	private ProgressBar pbPlaceHolder;
	private ImageView ivPlaceHolder;
	
	//Main Container
	private LinearLayout llMainContainer;
	
	//TextView and ListView
	private TextView tvAddress;
	private TextView tvDate;
	private TextView tvTotal;
	private TextView tvCCNum;
	
	private ListView lvDetails;
	
	private MainInterfaceActivity mainActivity;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_myorders_details, container,false);
		
		this.orderId = this.getArguments().getInt(ConstantClass.ORDERID_KEY);
		
		//Change Orderm Number
		TextView orderNum = (TextView) view.findViewById(R.id.order_tv);
		orderNum.setText(orderNum.getText()+Integer.toString(orderId));
		
		//PlaceHolders
		this.pbPlaceHolder = (ProgressBar) view.findViewById(R.id.odProgressBar);
		this.ivPlaceHolder = (ImageView) view.findViewById(R.id.odPlaceHolderImage);
		
		//Main Container
		this.llMainContainer = (LinearLayout) view.findViewById(R.id.odMainLayout);
		
		//Set vars
		this.tvAddress = (TextView) view.findViewById(R.id.tvOrderAddress);
		this.tvDate = (TextView) view.findViewById(R.id.tvOrderDetailsDate);
		this.tvTotal = (TextView) view.findViewById(R.id.tvOrderTotal);
		this.tvCCNum = (TextView) view.findViewById(R.id.tvOrderCCNum);
		this.lvDetails = (ListView) view.findViewById(R.id.odListView);
		
		this.mainActivity = (MainInterfaceActivity) this.getActivity();
		
		doHttpOrderDetails();
		
		return view;
	}


	private void doHttpOrderDetails() {
		//Make PB Visible
		this.ivPlaceHolder.setVisibility(View.GONE);
		this.pbPlaceHolder.setVisibility(View.VISIBLE);
		//Params
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERDETAILS+"?orderId="+this.orderId);
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					String date = json.getString("created_ts");
					Address addr = new Address(json.getJSONObject("address")); 
					CreditCard cc = new CreditCard(json.getJSONObject("creditCard"));
					JSONArray detailsList = json.getJSONArray("details");
					
					tvDate.setText(tvDate.getText()+" "+date);
					tvAddress.setText(addr.toString());
					
					double total = 0;
					for(int i=0; i<detailsList.length(); i++){
						total += detailsList.getJSONObject(i).getDouble("final_price");
					}
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(2);
					tvTotal.setText(tvTotal.getText()+" $"+nf.format(total));
					
					tvCCNum.setText(ConstantClass.CREDITCARD_LIST[cc.getType()]+" - "+cc.getNumber().substring(cc.getNumber().length()-4));
					
					//Size must be bigger than 1
					lvDetails.setAdapter(new OrderDetailsListAdapter(mainActivity, detailsList));
					lvDetails.setOnItemClickListener(new OrderDetailsListListener(mainActivity));
					
					llMainContainer.setVisibility(View.VISIBLE);
					
				} catch (JSONException e) {
					Toast.makeText(mainActivity, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				ivPlaceHolder.setVisibility(View.VISIBLE);
			}
			@Override 
			public void onDone() {
				pbPlaceHolder.setVisibility(View.GONE);
			}
			
		});
		
		request.execute();
		
		
		
		
	}


}

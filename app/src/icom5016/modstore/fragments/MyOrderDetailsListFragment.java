package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyOrderDetailsListFragment extends Fragment {
	
	private int orderId;
	//Placeholders
	private ProgressBar pbPlaceHolder;
	private ImageView ivPlaceHolder;
	
	//Main Container
	private LinearLayout llMainContainer;
	
	
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
		
		doHttpOrderDetails();
		
		return view;
	}


	private void doHttpOrderDetails() {
		//Make PB Visible
		this.ivPlaceHolder.setVisibility(View.GONE);
		this.pbPlaceHolder.setVisibility(View.VISIBLE);
		//Params
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERS+"?orderId="+this.orderId);
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				
				llMainContainer.setVisibility(View.VISIBLE);
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

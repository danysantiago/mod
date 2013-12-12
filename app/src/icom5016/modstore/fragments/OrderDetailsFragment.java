package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Orders;
import icom5016.modstore.models.ProductSelling;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailsFragment extends Fragment implements OnClickListener{

	private MainActivity ma;
	private ProductSelling product;
	private TextView tvAddress;
	private TextView tvDate;
	private EditText etTrackingNum;
	private ProgressBar pbPlaceHolder;
	private ImageView ivPlaceHolder;
	private Button button;
	private LinearLayout ll;
	private ProgressDialog pd; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_orders_details, container,false);
		this.ma = (MainActivity) this.getActivity();
		this.product = (ProductSelling) this.getArguments().getSerializable(ConstantClass.PRODUCT_KEY);
		//PlaceHolders
		this.pbPlaceHolder = (ProgressBar) view.findViewById(R.id.odProgressBar);
		this.ivPlaceHolder = (ImageView) view.findViewById(R.id.odPlaceHolderImage);
		
		//Set vars
		this.tvAddress = (TextView) view.findViewById(R.id.tvOrderAddress);
		this.tvDate = (TextView) view.findViewById(R.id.tvOrderDetailsDate);
		this.etTrackingNum = (EditText) view.findViewById(R.id.tvOrderTrackingNUm);
		this.button = (Button) view.findViewById(R.id.tvOrderButton);
		this.button.setOnClickListener(this);
		this.ll = (LinearLayout) view.findViewById(R.id.llContainer);
		
		this.pd = new ProgressDialog(ma);		
		
		doHttpOrderDetails();
		
		return view;
	}
	
	private void doHttpOrderDetails() {
		//Make PB Visible
		this.ivPlaceHolder.setVisibility(View.GONE);
		this.pbPlaceHolder.setVisibility(View.VISIBLE);
		//Params
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERDETAILS+"?orderId="+this.product.getOrderId());
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					Orders order = new Orders(json.getJSONObject("order"));
					
					tvDate.setText(tvDate.getText()+" "+order.getDateFormatedString());
					tvAddress.setText(order.getAddress().toString());
					
					if(!product.getTrackingNumber().equals("null")){
						etTrackingNum.setText(product.getTrackingNumber());
					}
					
					pbPlaceHolder.setVisibility(View.GONE);
					
					ll.setVisibility(View.VISIBLE);
					
					
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvOrderButton:
			this.pd.setMessage("Updating");
			this.pd.show();
			try {
				doHttpTrackingUpdate();
			} catch (JSONException e) {
				Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		
	}

	private void doHttpTrackingUpdate() throws JSONException {
		Bundle params = new Bundle();
		params.putString("method", "POST");
		params.putString("url", Server.Orders.CHANGE_TNUM);
		
		JSONObject credentials = new JSONObject();
		credentials.put("orderDetailsId", this.product.getOrderDeteailtId());
		credentials.put("trackingNumber", etTrackingNum.getText().toString());
		
		HttpRequest request = new HttpRequest(params , credentials,new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					String status = json.getString("status");
					if(status.equals("OK")){
						Toast.makeText(ma, "Succecfully Updated" , Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(ma, "Unable to Updated" , Toast.LENGTH_SHORT).show();
					}
					
					
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, "No Connection", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onDone() {
				pd.cancel();
			}

		});
		request.execute();
	}

}

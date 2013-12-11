package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.activities.SettingsActivity;
import icom5016.modstore.adapter.AddressAdapter;
import icom5016.modstore.adapter.CreditCardAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Address;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutFragment extends Fragment implements View.OnClickListener{
	
	private ProgressDialog pd;
	private MainActivity ma;
	private Button btn;
	private Spinner addressSpinner;
	private Spinner ccSpinner;
	private ProgressBar addressPb;
	private ProgressBar ccPb;
	private boolean addressFlag;
	private boolean ccFlag;
	private TextView noAddress;
	private TextView noPayment;
	
	private int checkoutType;
	private int productId;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cart_checkout, container, false);
		
		ma = (MainActivity) this.getActivity();
		pd = ma.processDialog;
		this.addressSpinner = (Spinner) view.findViewById(R.id.shippingCartSpinner);
	    this.ccSpinner = (Spinner) view.findViewById(R.id.paymentCartSpinner);
	    this.addressPb = (ProgressBar) view.findViewById(R.id.shippingCartProgressBar);
	    this.ccPb = (ProgressBar) view.findViewById(R.id.paymentCartProgressBar);
		this.btn = (Button) view.findViewById(R.id.cartCheckoutButton);
		this.btn.setOnClickListener(this);
		this.btn.setFocusable(false);
		this.btn.setClickable(false);
		this.noAddress = (TextView) view.findViewById(R.id.noShippingError);
		this.noPayment = (TextView) view.findViewById(R.id.noPaymentError);
		this.noAddress.setOnClickListener(this);
		this.noPayment.setOnClickListener(this);
		
		this.checkoutType = this.getArguments().getInt(ConstantClass.CHECKOUT_TYPE_KEY); 
	    if(this.checkoutType == ConstantClass.CHECKOUT_TYPE_BUY){
	    	this.productId = this.getArguments().getInt(ConstantClass.PRODUCT_KEY);
	    }
		
		
		this.requestAddressAndCc();
		
		return view;
	}
	
	private void requestAddressAndCc() {
		this.addressFlag = false;
		this.ccFlag = false;
		this.requestAddresses();
		this.requestCreditCards();
	}

	private void requestAddresses() {
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.Addresses.GET + "?userId=" + u.getGuid());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				try {
					JSONArray jsonArr = json.getJSONArray("addresses");
					if(jsonArr.length() == 0){
						addressPb.setVisibility(View.GONE);
						noAddress.setVisibility(View.VISIBLE);
					}
					else{
						//Hide PB
						addressPb.setVisibility(View.GONE);
						AddressAdapter adapter = new AddressAdapter(getActivity(), R.layout.listview_address_row, json);
						addressSpinner.setAdapter(adapter);
						addressSpinner.setVisibility(View.VISIBLE);
						
						if(ccFlag){
							btn.setFocusable(true);
							btn.setClickable(true);
							pd.dismiss();
							addressFlag = true;
						}
						else
							addressFlag=true;
						
					}
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json, Toast.LENGTH_SHORT).show();
				}
				
				
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Couldn't load the Addresses [ERR: 2]", Toast.LENGTH_SHORT).show();
			}
		});
		
		request.execute();
	}
	
	private void requestCreditCards() {
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.CreditCards.GET + "?userId=" + u.getGuid());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				try {
					JSONArray jsonArr = json.getJSONArray("creditcards");
					if(jsonArr.length() == 0){
						ccPb.setVisibility(View.GONE);
						noPayment.setVisibility(View.VISIBLE);
					}
					else{
						//Hide PB
						ccPb.setVisibility(View.GONE);
						CreditCardAdapter adapter = new CreditCardAdapter(getActivity(), R.layout.listview_creditcard_row, json);
						ccSpinner.setAdapter(adapter);
						ccSpinner.setVisibility(View.VISIBLE);
						

						if(addressFlag){
							btn.setFocusable(true);
							btn.setClickable(true);
							ccFlag = true;
						}
						else
							ccFlag=true;
						
					}
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, "Couldn't load the Credit Cards [ERR: 2]", Toast.LENGTH_SHORT).show();
			}
		});
		
		request.execute();
	}
	
	private void doHttpCompleteCheckout() {
		switch (this.checkoutType) {
		case ConstantClass.CHECKOUT_TYPE_BUY:	
			try{
				doHttpBuyItNow();
			} catch (JSONException e) {
				Toast.makeText(ma, R.string.errmsg_bad_json,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case ConstantClass.CHECKOUT_TYPE_CART:
			try {
				doHttpCart();
			} catch (JSONException e) {
				Toast.makeText(ma, R.string.errmsg_bad_json,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private void doHttpCart() throws JSONException{
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		Bundle params = new Bundle();
		params.putString("url", Server.Checkout.CART);
		params.putString("method", "POST");
		//Credentials
		JSONObject credentials = new JSONObject();
		int ccId = ((CreditCard) ccSpinner.getItemAtPosition(ccSpinner.getSelectedItemPosition())).getCreditcardId();
		credentials.put("creditcard_id", ccId );
		int addrId = ((Address) addressSpinner.getItemAtPosition(addressSpinner.getSelectedItemPosition())).getId();
		credentials.put("address_id", addrId);
		credentials.put("user_id", ""+u.getGuid());
		
		HttpRequest request = new HttpRequest(params, credentials ,new HttpCallback() {
		
			@Override
			public void onSucess(JSONObject json) {
				
				String acknowledgeCode = null;
				try {
					acknowledgeCode = json.getString("status");
				} catch (JSONException e1) {
					
				}
				
				if(acknowledgeCode != null){
				  if(acknowledgeCode.equals("OK")){
					  ma.fragmentStack.pop();
					  InvoiceFragment ifrag = new InvoiceFragment();
					  Bundle bnd = new Bundle();
					  int value = -1;
					  try {
						value = json.getInt("order_id");
					} catch (JSONException e) {
						Toast.makeText(ma, R.string.errmsg_bad_json,
								Toast.LENGTH_SHORT).show();
					}
					  bnd.putInt(ConstantClass.ORDERID_KEY, value);
					  ifrag.setArguments(bnd);
					  ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), ifrag);
				  }
				  else if(acknowledgeCode.equals("OUT_OF_STOCK")){
					  Toast.makeText(ma, "Unable to Complete Item is Out of Stock", Toast.LENGTH_SHORT).show();
				  }
				  else if(acknowledgeCode.equals("PRODUCT_FROM_BUYER")){
					  Toast.makeText(ma, "Unable to Buy Your Own Product", Toast.LENGTH_SHORT).show();
				  }
				  else if(acknowledgeCode.equals("EXPIRED_CC")){
					  Toast.makeText(ma, "Credit Card submited is Expired", Toast.LENGTH_SHORT).show();
				  }
				}
				else{
					Toast.makeText(ma, "Unable to Complete Checkout", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(ma, "Unable to Complete Checkout", Toast.LENGTH_SHORT).show();
				
			}
			public void onDone(){
				pd.dismiss();
			}
		});
		request.execute();
		
	}
	
	private void doHttpBuyItNow() throws JSONException{
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		Bundle params = new Bundle();
		params.putString("url", Server.Checkout.BUY_IT_NOW);
		params.putString("method", "POST");
		//Credentials
		JSONObject credentials = new JSONObject();
		int ccId = ((CreditCard) ccSpinner.getItemAtPosition(ccSpinner.getSelectedItemPosition())).getCreditcardId();
		credentials.put("creditcard_id", ccId );
		int addrId = ((Address) addressSpinner.getItemAtPosition(addressSpinner.getSelectedItemPosition())).getId();
		credentials.put("address_id", addrId);
		credentials.put("user_id", ""+u.getGuid());
		credentials.put("product_id", productId);
		
		HttpRequest request = new HttpRequest(params, credentials ,new HttpCallback() {
		
			@Override
			public void onSucess(JSONObject json) {
				
				String acknowledgeCode = null;
				try {
					acknowledgeCode = json.getString("status");
				} catch (JSONException e1) {
					
				}
				
				if(acknowledgeCode != null){
				  if(acknowledgeCode.equals("OK")){
					  ma.fragmentStack.pop();
					  InvoiceFragment ifrag = new InvoiceFragment();
					  Bundle bnd = new Bundle();
					  int value = -1;
					  try {
						value = json.getInt("order_id");
					} catch (JSONException e) {
						Toast.makeText(ma, R.string.errmsg_bad_json,
								Toast.LENGTH_SHORT).show();
					}
					  bnd.putInt(ConstantClass.ORDERID_KEY, value);
					  ifrag.setArguments(bnd);
					  ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), ifrag);
				  }
				  else if(acknowledgeCode.equals("PRODUCT_FROM_BUYER")){
					  Toast.makeText(ma, "Unable to Buy Your Own Product", Toast.LENGTH_SHORT).show();
				  }
				  else if(acknowledgeCode.equals("EXPIRED_CC")){
					  Toast.makeText(ma, "Credit Card submited is Expired", Toast.LENGTH_SHORT).show();
				  }
				  else if(acknowledgeCode.equals("NOTHING_ON_CART")){
					  Toast.makeText(ma, "Cart is empty", Toast.LENGTH_SHORT).show();
				  }
				}
				else{
					Toast.makeText(ma, "Unable to Complete Checkout", Toast.LENGTH_SHORT).show();
				}
				 
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(ma, "Unable to Complete Checkout", Toast.LENGTH_SHORT).show();
				
			}
			public void onDone(){
				pd.dismiss();
			}
		});
		request.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cartCheckoutButton:
			pd.setMessage(getResources().getString(R.string.cart_checkout_complete_order));
			pd.show();
			this.doHttpCompleteCheckout();
			break;
		case R.id.noPaymentError:
			ma.startActivity(new Intent(ma, SettingsActivity.class));
			break;
		case R.id.noShippingError:
			ma.startActivity(new Intent(ma, SettingsActivity.class));
			break;

		}
	}
	
	
	
}

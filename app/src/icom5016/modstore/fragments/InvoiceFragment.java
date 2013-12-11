package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.models.Orders;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InvoiceFragment extends Fragment implements View.OnClickListener{

	private Button btn;
	private MainActivity ma;
	private int id;
	private ProgressDialog pd;
	private TextView tvId;
	private TextView tvPrice;
	private TextView tvAddress;
	private TextView tvCc;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cart_invoice, container, false);
		this.btn = (Button) view.findViewById(R.id.btnViewDetails);
		this.btn.setOnClickListener(this);
		this.ma = (MainActivity) this.getActivity();
		this.pd = new ProgressDialog(getActivity());
		
		id = this.getArguments().getInt(ConstantClass.ORDERID_KEY);
		tvId = (TextView) view.findViewById(R.id.invoiceId);
		tvPrice = (TextView) view.findViewById(R.id.invoicePrice);
		tvAddress = (TextView) view.findViewById(R.id.invoiceShipAddress);
		tvCc= (TextView) view.findViewById(R.id.invoiceCcNumber);
		
		
		pd.setMessage("Getting Details...");
		pd.show();
		doHttpOrderDetails();
		
		return view;
	}

	private void doHttpOrderDetails(){
		
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERDETAILS+"?orderId="+this.id);
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					Orders order = new Orders(json.getJSONObject("order"));
					tvId.setText(order.getOrderId()+"");
					tvPrice.setText(order.getOrderTotalString());
					
					CreditCard cc = order.getCreditCard();
					tvCc.setText(cc.getTypeString()+" - "+
							cc.getNumber().substring(cc.getNumber().length()-4));
					
					tvAddress.setText(order.getAddress().toString());
					
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, "Unable to Load Details", Toast.LENGTH_SHORT).show();
			}
			@Override 
			public void onDone() {
				pd.cancel();
			}
			
		});
		
		request.execute();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnViewDetails:
			Bundle bundle = new Bundle();
			bundle.putInt(ConstantClass.ORDERID_KEY, id);
			MyOrderDetailsListFragment modlf = new MyOrderDetailsListFragment();
			modlf.setArguments(bundle);
			this.ma.fragmentStack.pop();
			this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), modlf);
			break;

		default:
			break;
		}
	}
}

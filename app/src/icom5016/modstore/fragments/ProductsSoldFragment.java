package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.OrderDetail;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.uielements.OrderedProductAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsSoldFragment extends Fragment {
	TextView lblTitle;
	ListView lstList;
	ProgressDialog pd;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_listwithtitle, container, false);
		
		lblTitle = (TextView)view.findViewById(R.id.lblListTitle);
		lstList = (ListView)view.findViewById(R.id.lstBasicList);
		
		lblTitle.setText("Products Sold:");
		
		requestProductsSold();
		
		return view;
	}
	
	private void requestProductsSold() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.OrderDetails.GETALL);
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				//Pass JSON to Adapter
				try {
					OrderedProductAdapter adapter;
					adapter = new OrderedProductAdapter(getActivity(), R.layout.listview_orderedproduct, json.getJSONArray("orderdetails"));
					lstList.setAdapter(adapter);
					lstList.setOnItemClickListener(new listOnClick((MainInterfaceActivity)getActivity()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getActivity(), "Couldn't load the Products [ERR: 1]", Toast.LENGTH_SHORT).show();
				}
				pd.dismiss();
			}

			@Override
			public void onFailed() {
				pd.dismiss();
				Toast.makeText(getActivity(), "Couldn't load the Products [ERR: 2]", Toast.LENGTH_SHORT).show();
			}
		});
		
		pd = ProgressDialog.show(getActivity(), "Loading", "Loading Products...");
		request.execute();
	}
	
	class listOnClick implements OnItemClickListener {
		private MainInterfaceActivity activity;
		
		public listOnClick(MainInterfaceActivity activity) {
			super();
			this.activity = activity;
		}
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OrderedProductFragment f = new OrderedProductFragment();
			f.setOrderDetail((OrderDetail)lstList.getAdapter().getItem(arg2));
			activity.fragmentStack.push(f);
			AndroidResourceFactory.setNewFragment(activity, this.activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
		}
	}
}

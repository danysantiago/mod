package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.CreditCardsFragment.listOnClick;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.uielements.ProductDetailsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsForSaleFragment extends Fragment {
	TextView lblTitle;
	ListView lstList;
	ProgressDialog pd;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_listwithtitle, container, false);
		
		lblTitle = (TextView)view.findViewById(R.id.lblListTitle);
		lstList = (ListView)view.findViewById(R.id.lstBasicList);
		
		lblTitle.setText("Products for Sale:");
		
		requestProductsForSale();
		
		return view;
	}
	
	private void requestProductsForSale() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.Products.GETALL);
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				//Pass JSON to Adapter
				try {
					ProductDetailsAdapter adapter;
					adapter = new ProductDetailsAdapter(getActivity(), R.layout.listview_product_row_2, json.getJSONArray("products"), false);
					lstList.setAdapter(adapter);
					//lstListView.setOnItemClickListener(new listOnClick());
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
}

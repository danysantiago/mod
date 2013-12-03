package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.ProductListingAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.Server;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.listeners.ProductListingListener;
import icom5016.modstore.resources.ConstantClass;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ProductListingFragment extends Fragment {
	
	private ListView prodListLv;
	private LinearLayout prodListEmpty;
	private ProgressDialog pd;
	private Bundle params;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_prodlisting, container,false);
		
		this.prodListLv = (ListView) view.findViewById(R.id.prodlist_lv);
		this.prodListEmpty = (LinearLayout) view.findViewById(R.id.prodlist_empty);
		MainActivity ma = (MainActivity) this.getActivity();
		pd = ma.processDialog;
		this.params = this.getArguments();
		
		
		pd.setMessage(getResources().getString(R.string.prodlist_pd));
		pd.show();
		this.doHttpSearch(this.params);
		
		return view;
	}
	
	private void doHttpSearch(Bundle args){

		//Perform http request
		String searchUrl = this.parseUrl(args); 

		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", searchUrl);	
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					//Get array of products
					JSONArray jsonArr = json.getJSONArray("results");

					if(jsonArr.length() == 0){
						prodListEmpty.setVisibility(View.VISIBLE);
					}
					else{
						//Pass it to adapter and to List View
						ProductListingAdapter adapter = new ProductListingAdapter(getActivity(), jsonArr);
						prodListLv.setAdapter(adapter);
						
						//Change View Visibility
						prodListLv.setOnItemClickListener(new ProductListingListener((MainActivity) getActivity()));
						prodListLv.setVisibility(View.VISIBLE);
					}
					

				} catch (JSONException e) {
					Toast.makeText(getActivity(), R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	}

	private String parseUrl(Bundle args) {
		
		Uri.Builder urlB = Uri.parse(Server.Products.GETSEARCH).buildUpon();
		
		String query = args.getString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY);
		String sort = args.getString(ConstantClass.SEARCH_DIALOG_SORT_KEY);
		String category = args.getString(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY);
		String rating = args.getString(ConstantClass.SEARCH_DIALOG_RATING_KEY);
		String condition = args.getString(ConstantClass.SEARCH_DIALOG_CONDITION_KEY);
		String start_price = args.getString(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY);
		String end_price = args.getString(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY);
		
		
		//Query
		if(args.containsKey(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY)){
			urlB.appendQueryParameter("searchString", query.trim());
		}
		
		//Sort
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_SORT_KEY)){
			urlB.appendQueryParameter("sort", sort);
		}
		
		//Category
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY)){
			urlB.appendQueryParameter("category", category);
		}
		
		//Rating
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_RATING_KEY)){
			urlB.appendQueryParameter("sellerRating", rating);
		}
		
		//Condition
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_CONDITION_KEY)){
			urlB.appendQueryParameter("type", condition);
		}
		
		//Start Price
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY)){
			urlB.appendQueryParameter("priceFrom", start_price);
		}
		
		//End Price
		if(args.containsKey(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY)){
			urlB.appendQueryParameter("priceTo", end_price);
		}
	
		
		return urlB.toString();
	}

}

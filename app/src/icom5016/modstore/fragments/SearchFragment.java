package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.ProductAdapter;
import icom5016.modstore.uielements.ProductListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFragment extends Fragment {
	
	private Bundle searchArgs;
	private boolean isSearchClick;
	private ProgressDialog pd;
	private ListView list;
	private ImageView placeHolderImage;
	
	public SearchFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		
		//Set Layout
		View view = inflater.inflate(R.layout.fragment_search, container,false);
		
		//Get ListView
		this.list = (ListView) view.findViewById(R.id.listview_search_fragment);
		this.placeHolderImage = (ImageView) view.findViewById(R.id.placehoder_search);
		
		//Get Args
		 this.searchArgs = this.getArguments();
		 
		 this.isSearchClick = this.searchArgs.getBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_KEY);
		 
		 //If Search Click False return view
		 if(!isSearchClick){
			 return view;
		 }
		 
		pd = new ProgressDialog(getActivity());
		pd.setMessage(getResources().getString(R.string.search_progress_msg));
		pd.show();
		
		this.doHttpSearch(searchArgs);
		return view;
		
	}
	
	private void doHttpSearch(Bundle args){
		//Perform http request
		
		Uri.Builder urlB = Uri.parse(Server.Products.GETALL).buildUpon();
		urlB.appendQueryParameter("query", args.getString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY));
		urlB.appendQueryParameter("sort", ""+args.getInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY));
		urlB.appendQueryParameter("category", ""+args.getInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY));
		urlB.appendQueryParameter("rating", ""+args.getInt(ConstantClass.SEARCH_DIALOG_RATING_KEY));
		urlB.appendQueryParameter("condition", ""+args.getInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY));
		urlB.appendQueryParameter("startprice", ""+args.getDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY));
		urlB.appendQueryParameter("endprice", ""+args.getDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY));
		
		String searchUrl = urlB.toString();

		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", searchUrl);	
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					//Get array of products
					JSONArray jsonArr = json.getJSONArray("products");

					//Pass it to adapter and to List View
					ProductAdapter adapter = new ProductAdapter(getActivity(), jsonArr);
					list.setAdapter(adapter);
					
					//Change View Visibility
					placeHolderImage.setVisibility(View.GONE);
					list.setOnItemClickListener(new ProductListener((MainInterfaceActivity) getActivity()));
					list.setVisibility(View.VISIBLE);

				} catch (JSONException e) {
					Toast.makeText(getActivity(), R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), R.string.search_error, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	}
}

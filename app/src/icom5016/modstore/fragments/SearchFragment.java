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
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.Search.GET);		
		//Must Change
		JSONObject filter_params = new JSONObject();
		try{
			filter_params.put("query", args.getString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY));
			filter_params.put("sort", args.getInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY));
			filter_params.put("category", args.getInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY));
			filter_params.put("rating", args.getInt(ConstantClass.SEARCH_DIALOG_RATING_KEY));
			filter_params.put("condition", args.getInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY));
			filter_params.put("startprice", args.getDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY));
			filter_params.put("endprice", args.getDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY));
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		
		HttpRequest request = new HttpRequest(params, filter_params ,new HttpCallback() {
			
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
					Toast.makeText(getActivity(), "Bad JSON parsing...", Toast.LENGTH_SHORT).show();
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

package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.ProductAdapter;
import icom5016.modstore.uielements.ProductListener;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductListFragment extends Fragment {
	
	private ImageView plPlaceHolder;
	private ProgressBar plProgressBar;
	private Spinner plSpinner;
	private LinearLayout plLinearLayout;
	private TextView plTextView;
	private ListView plListView;
	private int categoryId;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_product_list, container,false);
		
		this.plPlaceHolder = (ImageView) view.findViewById(R.id.plPlaceHolder);
		this.plProgressBar = (ProgressBar) view.findViewById(R.id.plProgressBar);
		this.plSpinner = (Spinner) view.findViewById(R.id.plSpinner);
		this.plLinearLayout = (LinearLayout) view.findViewById(R.id.plLinearLayout);
		this.plListView = (ListView) view.findViewById(R.id.plListView);
		this.plTextView = (TextView) view.findViewById(R.id.plTextView);

		Bundle bundle = this.getArguments();
		
		if(bundle == null){
			Toast.makeText(getActivity(), "Invalid Access to Class", Toast.LENGTH_SHORT).show();;
		}
		else
		{
			this.categoryId = bundle.getInt(ConstantClass.PRODUCT_LIST_CATEGORY_KEY);
			this.doHttpProducsList(categoryId);
			
		}
		
		
		return view;
	}

	private void doHttpProducsList(int categoryId) {
				
				//Hide Icon Make Visible ProgressBar
				this.plPlaceHolder.setVisibility(View.GONE);
				this.plProgressBar.setVisibility(View.VISIBLE);
				
				//Perform http request
				Bundle params = new Bundle();
				params.putString("method", "GET");
				params.putString("url", Server.Products.GETCATEGORIES+categoryId); //Remember make a special route
				
				
				HttpRequest request = new HttpRequest(params, new HttpCallback() {
					
					@Override
					public void onSucess(JSONObject json) {
						
						//Get Info with Json
						
						try {
							//Get category
							JSONObject categoryJson = json.getJSONObject("category");
							//Get Sub category
							JSONArray subCategoryJson = json.getJSONArray("subcategory");
							//Get Products
							JSONArray productsJson = json.getJSONArray("products");
						
							Category mCategory = new Category(categoryJson);
							
							if(mCategory.getParentId() >= 0){
								plTextView.setText(mCategory.getName());
							}
							
							if(subCategoryJson.length() == 0){
								plSpinner.setVisibility(View.INVISIBLE);
							}
							else
							{
								//Set the Adapter
							}
							
							
							//Load Products
							if(productsJson.length() == 0){
								plPlaceHolder.setVisibility(View.VISIBLE);
								Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
							}
							else{
								//Pass it to adapter and to List View
								ProductAdapter adapter = new ProductAdapter(getActivity(), productsJson);
								plListView.setAdapter(adapter);
								plListView.setOnItemClickListener(new ProductListener((MainInterfaceActivity) getActivity()));
							}
							
							plLinearLayout.setVisibility(View.VISIBLE);
						} catch (JSONException e) {
							Toast.makeText(getActivity(), "Bad JSON parsing...",
									Toast.LENGTH_SHORT).show();
							plPlaceHolder.setVisibility(View.VISIBLE);
						}
						
						
						
						
					}
					
					@Override
					public void onFailed() {
						
						
						plPlaceHolder.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), R.string.category_list_error, Toast.LENGTH_LONG).show();
					}
					
					@Override
					public void onDone() {
						plProgressBar.setVisibility(View.GONE);
					}
				});
				request.execute();
	}
	
}

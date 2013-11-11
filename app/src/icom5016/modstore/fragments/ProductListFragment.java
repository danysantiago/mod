package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.ProductAdapter;
import icom5016.modstore.adapters.ProductListSpinnerAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.ProductListener;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
	private boolean spinnerChange = false;
	
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

	private void doHttpProducsList(final int categoryId) {
				
				//Hide Icon Make Visible ProgressBar
				this.plPlaceHolder.setVisibility(View.GONE);
				this.plProgressBar.setVisibility(View.VISIBLE);
				
				//Perform http request
				Bundle params = new Bundle();
				params.putString("method", "GET");
				params.putString("url", this.parseUrl(categoryId)); //Remember make a special route
				
				
				HttpRequest request = new HttpRequest(params, new HttpCallback() {
					
					@Override
					public void onSucess(JSONObject json) {
						
						//Get Info with Json
						
						try {
							//Get Products
							JSONArray productsJson = json.getJSONArray("results");
							List<Category> allCategories = ((MainInterfaceActivity) getActivity()).loadCategoriesById(ConstantClass.CategoriesFile.ALL_CATEGORIES);
							
							//Get category
							Category mCategory = null;
							for(Category e: allCategories){
								if(e.getId() == categoryId){
									mCategory = e;
									break;
								}
							}
							//Get Sub category
							List<Category> subCategoriesList = ((MainInterfaceActivity) getActivity()).loadCategoriesById(categoryId);
							
							if(mCategory.getParentId() >= 0){
								plTextView.setText(mCategory.getName());
							}
							
							if(subCategoriesList.size() == 0){
								plSpinner.setVisibility(View.INVISIBLE);
							}
							else
							{
								//Set the Adapter
								ProductListSpinnerAdapter spinnerAdapter = new ProductListSpinnerAdapter(getActivity(), subCategoriesList );
								spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								plSpinner.setAdapter(spinnerAdapter);
								
								
								plSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								    public void onItemSelected(AdapterView<?> listView, View view, int pos, long l) { 
								        if(spinnerChange){
								        	Category category = (Category) listView.getAdapter().getItem(pos);
								    		Bundle bundle = new Bundle();
								    		bundle.putInt(ConstantClass.PRODUCT_LIST_CATEGORY_KEY, category.getId());
								    		ProductListFragment plf = new ProductListFragment();
								    		plf.setArguments(bundle);
								    		MainInterfaceActivity mia = (MainInterfaceActivity) getActivity();
											mia.fragmentStack.push(plf);
											spinnerChange = false;
											AndroidResourceFactory.setNewFragment(mia, mia.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
								        }
								        else
								        	spinnerChange = true;
								    } 

								    public void onNothingSelected(AdapterView<?> adapterView) {
								        return;
								    } 
								}); 
							}
							
							
							//Load Products
							if(productsJson.length() == 0){
								plPlaceHolder.setVisibility(View.VISIBLE);
								plSpinner.setVisibility(View.GONE);
								Toast.makeText(getActivity(), "No Product Found", Toast.LENGTH_LONG).show();
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

	@Override
	public void onResume() {
		this.spinnerChange = false;
		super.onResume();
	}
	
	private String parseUrl(int id){
		Uri.Builder urlB = Uri.parse(Server.Products.GETSEARCH).buildUpon();
		urlB.appendQueryParameter("category", Integer.toString(id));
		return urlB.toString();
	}
	
}

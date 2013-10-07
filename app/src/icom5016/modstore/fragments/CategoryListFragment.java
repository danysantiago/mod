package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.CategoryListAdapter;
import icom5016.modstore.uielements.CategoryListListener;

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
import android.widget.TextView;
import android.widget.Toast;

public class CategoryListFragment extends Fragment {
	
	private ListView categoriesList;
	private ProgressBar categoryListProgressBar;
	private ImageView categoryListImageView;
	private LinearLayout categoriesListLayout;
	private TextView categoryListTextView;
	
	public CategoryListFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		//Inflate Layout
		View view = inflater.inflate(R.layout.fragment_category_list, container,false);
		
		
		//Instance Views
		this.categoriesListLayout = (LinearLayout) view.findViewById(R.id.categoryLinearLayout);
		this.categoriesList = (ListView) view.findViewById(R.id.categoryListView);
		this.categoryListProgressBar = (ProgressBar) view.findViewById(R.id.categoryListProgressBar);
		this.categoryListImageView = (ImageView) view.findViewById(R.id.placehoderCategoryList);
		this.categoryListTextView = (TextView) view.findViewById(R.id.categoryListText);
		
		Bundle bundle = this.getArguments();
		int parent = -1; //If -1 Default
		
		
		if(bundle != null){
			parent = bundle.getInt(ConstantClass.CATEGORY_LIST_PARENT_KEY);
		}	
		
		this.doHttpCategoryList(parent);
		
		return view;
	}
	
	private void doHttpCategoryList(int parent){
		//Hide Icon Make Visible ProgressBar
		this.categoryListImageView.setVisibility(View.GONE);
		this.categoryListProgressBar.setVisibility(View.VISIBLE);
		
		
		//Perform http request
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.Categories.GET+Integer.toString(parent));
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				
				//Get Info with Json
				
				try {
					//Get Parent
					JSONObject parentJson = json.getJSONObject("parent");
					//Get List
					JSONArray listJson = json.getJSONArray("list");
					
					if(listJson.length() == 0){
						//Open ProductListFragment
						Bundle bundle = new Bundle();
						bundle.putInt(ConstantClass.PRODUCT_LIST_CATEGORY_KEY, parentJson.getInt("id"));
						ProductListFragment plf = new ProductListFragment();
						plf.setArguments(bundle);
						MainInterfaceActivity mia = (MainInterfaceActivity) getActivity();
						mia.fragmentStack.push(plf);
						AndroidResourceFactory.setNewFragment(mia, mia.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
					}
					else{
						//Populate stuff
						
						//Change Text View
						String parentName = parentJson.getString("name");
						if(!parentName.isEmpty())
							categoryListTextView.setText(parentName);
						else
							categoryListTextView.setText(R.string.category_list_title);
						
						
						
						//Pass it to adapter and to listview
						CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), listJson);
						categoriesList.setAdapter(adapter);
						categoriesList.setOnItemClickListener(new CategoryListListener((MainInterfaceActivity) getActivity()));
						//Set Visibility
						categoriesListLayout.setVisibility(View.VISIBLE);
					}
					
					
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "Bad JSON parsing...",
							Toast.LENGTH_SHORT).show();
					categoryListImageView.setVisibility(View.VISIBLE);
				}
				
				
				
				
			}
			
			@Override
			public void onFailed() {
				
				
				categoryListImageView.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), R.string.category_list_error, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onDone() {
				categoryListProgressBar.setVisibility(View.GONE);
			}
		});
		request.execute();
	}

}

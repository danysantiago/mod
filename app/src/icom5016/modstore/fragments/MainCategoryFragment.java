package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainCategoryFragment extends Fragment {
	// Intances
	private String categoryTitle;

	private ProgressBar pd;
	private ListView list;
	Spinner spinner;
	private TextView noDataTextView;

	public MainCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.categoryTitle = getArguments().getString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY);		
		View view = inflater.inflate(R.layout.fragment_maincategory, container, false);
		
		// Set subcategories
		spinner = (Spinner) view.findViewById(R.id.subcategories_spinner);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		spinnerAdapter.add(categoryTitle);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				categoryTitle = (String) spinner.getAdapter().getItem(pos);
				doHttpCategories();
			}

			@Override
            public void onNothingSelected(AdapterView<?> arg0) {
	            // TODO Auto-generated method stub
	            
            }

		});
		
		//Get views
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		list = (ListView) view.findViewById(R.id.listView);
		noDataTextView = (TextView) view.findViewById(R.id.textView);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		doHttpCategories();
	}
	
	private void doHttpCategories() {
		//Show progress view, hide list view
		pd.setVisibility(View.VISIBLE);
		list.setVisibility(View.GONE);
		noDataTextView.setVisibility(View.GONE);
		
		//Perform http request
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.BASE_URL + Server.Categories.GET
				+ this.categoryTitle);
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					//Get array of products
					JSONArray jsonArr = json.getJSONArray("products");
					JSONArray subCategoriesJson = json.getJSONArray("sub-categories");

					//Pass it to adapter and to listview
					ProductAdapter adapter = new ProductAdapter(getActivity(), jsonArr);
					list.setAdapter(adapter);
					
					ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner.getAdapter();
					spinnerAdapter.clear();
					spinnerAdapter.add(categoryTitle);
					for(int i = 0; i < subCategoriesJson.length(); i++) {
						spinnerAdapter.add((String) subCategoriesJson.get(i));
					}
					

					//Show list view
					pd.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);

				} catch (JSONException e) {
					Toast.makeText(getActivity(), "Bad JSON parsing...",
							Toast.LENGTH_SHORT).show();
					showNoDataLabel();
				}

			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Could not get data",
						Toast.LENGTH_SHORT).show();
				showNoDataLabel();
			}
		});
		request.execute();
    }

	private void showNoDataLabel() {
		//If something goes wrong, show no data label, hide listview
		pd.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		noDataTextView.setVisibility(View.VISIBLE);
	}

}

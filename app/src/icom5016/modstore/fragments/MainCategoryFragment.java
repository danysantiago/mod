package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainCategoryFragment extends Fragment {
	// Intances
	private String categoryTitle;
	private String[] subCategories;

	private ProgressBar pd;
	private ListView list;
	private TextView noDataTextView;

	public MainCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.categoryTitle = getArguments().getString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY);
		this.subCategories = DataFetchFactory.fetchSubCategories(this.categoryTitle);
		View view = inflater.inflate(R.layout.fragment_maincategory, container, false);
		
		// Set category title
		TextView title = (TextView) view.findViewById(R.id.category_textView);
		title.setText(this.categoryTitle);
		//Get views
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		list = (ListView) view.findViewById(R.id.listView);
		noDataTextView = (TextView) view.findViewById(R.id.textView);

		//SHow progress view, hide list view
		pd.setVisibility(View.VISIBLE);
		list.setVisibility(View.GONE);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

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

					//Pass it to adapter and to listview
					ProductAdapter adapter = new ProductAdapter(getActivity(), jsonArr);
					list.setAdapter(adapter);

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

	public class ProductAdapter extends ArrayAdapter<JSONObject> {

		public ProductAdapter(Context context, JSONArray jsonArr) throws JSONException {
			super(context, R.layout.listview_product_row_1);
			
			for(int i = 0; i < jsonArr.length(); i++) {
				this.add(jsonArr.getJSONObject(i));
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			try {
				JSONObject productJson = this.getItem(position);

				LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
				
				row = inflater.inflate(R.layout.listview_product_row_1, parent, false);

				TextView name = (TextView) row.findViewById(R.id.name_textView);
				name.setText(productJson.getString("name"));

				TextView description = (TextView) row.findViewById(R.id.description_textView);
				description.setText(productJson.getString("description"));

				TextView price = (TextView) row.findViewById(R.id.price_textView);
				price.setText(productJson.getString("price"));
				
				row.setTag(productJson);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return row;
		}
	}
}

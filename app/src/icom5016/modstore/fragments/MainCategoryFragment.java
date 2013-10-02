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

import android.app.Fragment;
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
	//Intances
	private String categoryTitle;
	private String[] subCategories;
	
	private ProgressBar pd;
	private ListView list;
	private TextView noDataTextView;

	
	public MainCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		
		this.categoryTitle = getArguments().getString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY);
		this.subCategories = DataFetchFactory.fetchSubCategories(this.categoryTitle);
		View view = inflater.inflate(R.layout.fragment_maincategory, container,false);
		//Change Text View for Demo Purpose
		//TextView textView = (TextView) view.findViewById(R.id.textview_maincategory);
		//textView.setText(categoryTitle+"\n\nSubcategories: "+this.subCategories[0]+" "+this.subCategories[1]+" "+this.subCategories[2]+" "+this.subCategories[3]);
		
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		list = (ListView) view.findViewById(R.id.listView);
		noDataTextView = (TextView) view.findViewById(R.id.textView);
		
		pd.setVisibility(View.VISIBLE);
		list.setVisibility(View.GONE);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	  
	  Bundle params = new Bundle();
	  params.putString("method", "GET");
	  params.putString("url", Server.BASE_URL + Server.Categories.GET + this.categoryTitle);
	  HttpRequest request = new HttpRequest(params, new HttpCallback() {
      
      @Override
      public void onSucess(JSONObject json) {
        try {
        
          String[] array;
          JSONArray jsonArr = json.getJSONArray("products");
          array = new String[jsonArr.length()];
          for(int i = 0; i < jsonArr.length(); i++) {
            array[i] = jsonArr.getJSONObject(i).getString("name");
          }
          
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);
          list.setAdapter(adapter);
          
          pd.setVisibility(View.GONE);
          list.setVisibility(View.VISIBLE);
        
        } catch (JSONException e) {
          Toast.makeText(getActivity(), "Bad JSON parsing...", Toast.LENGTH_SHORT).show();
          showNoDataLabel();
        }
        
      }
      
      @Override
      public void onFailed() {
        Toast.makeText(getActivity(), "Could not get data", Toast.LENGTH_SHORT).show();
        showNoDataLabel();
      }
    });
	  request.execute();
	}
	
	private void showNoDataLabel() {
    pd.setVisibility(View.GONE);
    list.setVisibility(View.GONE);
    noDataTextView.setVisibility(View.VISIBLE);
	}
	
}

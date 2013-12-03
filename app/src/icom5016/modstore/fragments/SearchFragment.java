package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.CategorySpinnerAdapter;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchFragment extends Fragment {

	private SearchView searchView;
	private Spinner sortSpinner;
    private Spinner categoriesSpinner;
    private Spinner ratingSpinner;
    private Spinner conditionSpinner;
    private EditText startPrice;
    private EditText endPrice;
   
    private List<Category> categoryList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_search, container,false);
		
		this.searchView = (SearchView) view.findViewById(R.id.search_view);
	    searchView.setFocusable(true);
	    searchView.setIconified(false);
	    this.searchView.setOnQueryTextListener(new SearchQueryListener()); 
	    
	    
	    //Get Views
	    this.sortSpinner = (Spinner) view.findViewById(R.id.filterSortSpinner);
	    this.categoriesSpinner = (Spinner) view.findViewById(R.id.filterCategorySpinner);
	    this.ratingSpinner = (Spinner) view.findViewById(R.id.filterRatingSpinner);
	    this.conditionSpinner = (Spinner) view.findViewById(R.id.filterConditionSpinner);
	    this.startPrice = (EditText) view.findViewById(R.id.filterStartText);
	    this.endPrice = (EditText) view.findViewById(R.id.filterEndText);
	    
	    //Get All Categories
	    List<Category> allCategories = ((MainActivity) this.getActivity()).loadCategoriesById(ConstantClass.CategoriesFile.ALL_CATEGORIES);
		List<Category> sortedCategories = AndroidResourceFactory.sortCategories(allCategories);
		sortedCategories.add(0, new Category(ConstantClass.CategoriesFile.ALL_CATEGORIES, ConstantClass.CategoriesFile.ALL_CATEGORIES, "All"));
		this.categoryList = sortedCategories;
	    
	    this.initDialogAdapters();
		
		return view;
	}
	
	private void initDialogAdapters() {
		this.sortSpinner.setAdapter(
					new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_SORT));
		this.categoriesSpinner.setAdapter(
				new CategorySpinnerAdapter(this.getActivity(), R.layout.listview_filter_spinner, this.categoryList));
		this.ratingSpinner.setAdapter(
				new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_RATING));
		this.conditionSpinner.setAdapter(
				new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_CONDITION));
	}
	
	
	//Query Listener
	//**** Hack to Access Local Class Variables and Methods
	private class SearchQueryListener implements SearchView.OnQueryTextListener{
	
		@Override
		public boolean onQueryTextChange(String newText) {
			return true;
		}
		
		@Override
		public boolean onQueryTextSubmit(String query) {
			searchView.clearFocus();
			return customQueryTextOnSubmit(query);
		}
	}
	
	private boolean customQueryTextOnSubmit(String query){
		
		MainActivity ma = (MainActivity) this.getActivity();
		Bundle ret = new Bundle();
		if(!this.getListingBundle(ret)){
			return false;
		}
		if(query.isEmpty()){
			Toast.makeText(this.getActivity(), R.string.error_empty_search, Toast.LENGTH_SHORT).show();
			return false;
		}else
			ret.putString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY, query.trim());
		
		
		ProductListingFragment lf  = new ProductListingFragment();
		lf.setArguments(ret);
		ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), lf);
		return true;
	}
	
	private boolean getListingBundle(Bundle ret){
		
		
		//Sort Value
		int sortSpinnerValue = this.sortSpinner.getSelectedItemPosition();
		ret.putString(ConstantClass.SEARCH_DIALOG_SORT_KEY, ConstantClass.SEARCH_FILTER_SORT_URL_PARMS[sortSpinnerValue]);
		
		//Rating
		int ratingSpinnerValue = this.ratingSpinner.getSelectedItemPosition();
		
		if(ratingSpinnerValue != 0){
			ret.putString(ConstantClass.SEARCH_DIALOG_RATING_KEY, Integer.toString(6-ratingSpinnerValue));
		}
		
		//Categories
		int categoryValue = this.categoryList.get(this.categoriesSpinner.getSelectedItemPosition()).getId();
		if(categoryValue != ConstantClass.CategoriesFile.ALL_CATEGORIES)
			ret.putString(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY, Integer.toString(categoryValue));
		
		
		//Condition
		int conditionSpinnerValue = this.conditionSpinner.getSelectedItemPosition();
		ret.putString(ConstantClass.SEARCH_DIALOG_CONDITION_KEY, ConstantClass.SEARCH_FILTER_CONDITION_URL_PARMS[conditionSpinnerValue]);
		   
	    //Prices   
		double startPriceValue = -1.0, endPriceValue = -1.0; 
	       if(!this.startPrice.getText().toString().trim().isEmpty())
	    	   startPriceValue = Double.parseDouble(this.startPrice.getText().toString().trim());
	       if(!this.endPrice.getText().toString().trim().isEmpty())
	    	   endPriceValue = Double.parseDouble(this.endPrice.getText().toString().trim());
		
	     if(startPriceValue != -1.0 && endPriceValue != -1.0){
	    	 if(endPriceValue < startPriceValue){
	    		 Toast.makeText(this.getActivity(), R.string.error_search, Toast.LENGTH_SHORT).show();
	    		 return false;
	    	 }
	     }
	     
	     if(startPriceValue != -1.0){
	    	 ret.putString(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY, Double.toString(startPriceValue));
	     }
	     
	     if(endPriceValue != -1.0){
	    	 ret.putString(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY, Double.toString(endPriceValue));
	     }
	       
	   return true;
	}
	
}
	

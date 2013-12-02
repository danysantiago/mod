package icom5016.modstore.activities;

import icom5016.modstore.fragments.SearchFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.SearchFilterDialog;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class SearchActivity extends MainInterfaceActivity implements SearchFilterDialog.SearchFilterDialogListener {
		
	private SearchView searchView;
	
				/*Filter Values*/
    private int sortSpinnerValue;
    private int categoriesSpinnerIndexValue;
    private int categoriesSpinnerIdValue;
    private int ratingSpinnerValue;
    private int conditionSpinnerValue;
    private double startPriceValue;
    private double endPriceValue;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//Obtain the ActionBar
			final ActionBar ActionBarVar = this.getActionBar();
			
			//SetActionBar to Home/Up
			ActionBarVar.setDisplayHomeAsUpEnabled(true);
			ActionBarVar.setHomeButtonEnabled(true);
			ActionBarVar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
			
			this.setDefaultFilterParams();

			//Load Search Fragment
			if(savedInstanceState == null){
				
				//Load Empty Query into Bundle and starts Fragment
				Bundle bundle = new Bundle();
				bundle.putString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY, "");
				bundle.putBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_KEY, false);
				bundle.putInt(ConstantClass.SEARCH_DIALOG_SORT_KEY, sortSpinnerValue);
				bundle.putInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_INDEX_KEY, categoriesSpinnerIndexValue);
				bundle.putInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY, categoriesSpinnerIdValue);
				bundle.putInt(ConstantClass.SEARCH_DIALOG_RATING_KEY, ratingSpinnerValue);
				bundle.putInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY, conditionSpinnerValue);
				bundle.putDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY, startPriceValue);
				bundle.putDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY, endPriceValue);
				SearchFragment fragment = new SearchFragment();
				fragment.setArguments(bundle);
				AndroidResourceFactory.setNewFragment(this, fragment, MainInterfaceActivity.getContentFragmentId());
			}
			
		};
		
					/*Set Default Values*/
		@Override
		protected void onResume() {
			super.onResume();
		}
					
					/*Navigate Up the Stack*/
		
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case android.R.id.home:
				if(this.fragmentStack.size() <= 1){
					finish();
				}
				else{
					this.fragmentStack.pop();
					AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
					return true;
				}		
				break;
			case R.id.btn_search_filter:
				SearchFilterDialog.newInstance(sortSpinnerValue, categoriesSpinnerIndexValue, categoriesSpinnerIdValue ,ratingSpinnerValue, conditionSpinnerValue, startPriceValue, endPriceValue)
				.show(this.getFragmentManager(), ConstantClass.SEARCH_FILTER_DIALOG_TAG);
				break;	
			}
			return super.onOptionsItemSelected(item);
		}
		
		
		
					/*Hide Category and Search Buttons, and Expand SearchBar */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			
			//Hide Buttons
			menu.findItem(R.id.btn_search).setVisible(false);
			menu.findItem(R.id.btn_cart).setVisible(false);
			
			//Make Visible Search Bar and Expanded
			MenuItem searchBarItem = menu.findItem(R.id.searchbar);
			searchBarItem.setVisible(true);
			this.searchView = (SearchView) searchBarItem.getActionView();
			this.searchView.setIconified(false);

			this.searchView.setOnQueryTextListener(new SearchQueryListener()); 
			
			//Make Visible Settings
			menu.findItem(R.id.btn_search_filter).setVisible(true);
			
			return true;
			
		}
		
		
		
		
		//Must be overriding for SubMenu Hiding 
		@Override
		public boolean onPrepareOptionsMenu(Menu menu){
	        menu.findItem(R.id.item_categories).setVisible(false);
			return true;
		}

		//Query Listener
				//**** Hack to Access Local Class Variables and Methods
		private class SearchQueryListener implements SearchView.OnQueryTextListener{

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Change for Suggestions
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.clearFocus();
				return customQueryTextOnSubmit(query);
			}
		}
		//Custom Methods for Class SearchQueryListener
		private boolean customQueryTextOnSubmit(String query){
			
			Bundle bundle = new Bundle();
			bundle.putString(ConstantClass.SEARCH_FRAGMENT_QUERY_KEY, query);
			bundle.putBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_KEY, true);
			bundle.putInt(ConstantClass.SEARCH_DIALOG_SORT_KEY, sortSpinnerValue);
			bundle.putInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_INDEX_KEY, categoriesSpinnerIndexValue);
			bundle.putInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY, categoriesSpinnerIdValue);
			bundle.putInt(ConstantClass.SEARCH_DIALOG_RATING_KEY, ratingSpinnerValue);
			bundle.putInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY, conditionSpinnerValue);
			bundle.putDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY, startPriceValue);
			bundle.putDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY, endPriceValue);
			SearchFragment fragment= new SearchFragment();
			fragment.setArguments(bundle);
			this.fragmentStack.push(fragment);
			AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId()); 
			return true;
		}
		
		
					/*Dialog Listener*/
		@Override
		public void onDialogOkClick(DialogFragment dialog) {
			SearchFilterDialog sfDialog = (SearchFilterDialog) dialog;
			
			sortSpinnerValue = sfDialog.getSortSpinnerValue();
		    categoriesSpinnerIndexValue = sfDialog.getCategoriesSpinnerIndexValue();
		    categoriesSpinnerIdValue = sfDialog.getCategoriesSpinnerIdValue();
		    ratingSpinnerValue = sfDialog.getRatingSpinnerValue();
		    conditionSpinnerValue = sfDialog.getConditionSpinnerValue();
		    startPriceValue = sfDialog.getStartPriceValue();
		    endPriceValue = sfDialog.getEndPriceValue();
		}
		@Override
		public void onDialogCancelClick(DialogFragment dialog) {
		}
		
						/*Set Default Values*/
		private void setDefaultFilterParams() {
			sortSpinnerValue = 0;
		    categoriesSpinnerIndexValue = 0;
		    categoriesSpinnerIdValue = ConstantClass.CategoriesFile.ALL_CATEGORIES;
		    ratingSpinnerValue = 0;
		    conditionSpinnerValue = 0;
		    startPriceValue = -1;
		    endPriceValue = -1;

		}

		
			
}

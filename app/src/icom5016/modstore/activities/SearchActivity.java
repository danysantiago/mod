package icom5016.modstore.activities;

import icom5016.modstore.fragments.SearchFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.uielements.SearchFilterDialog;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class SearchActivity extends MainInterfaceActivity {
		
	private SearchView searchView;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//Obtain the ActionBar
			final ActionBar ActionBarVar = this.getActionBar();
			
			//SetActionBar to Home/Up
			ActionBarVar.setDisplayHomeAsUpEnabled(true);
			ActionBarVar.setHomeButtonEnabled(true);
			ActionBarVar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
			
			//Load Search Fragment
			if(savedInstanceState == null){
				
				//Load Empty Query into Bundle and starts Fragment
				Bundle bundle = new Bundle();
				bundle.putString(ConstantClass.SEARCH_FRAGMENT_QUERY_ID, "");
				bundle.putBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_ID, true);
				SearchFragment fragment = new SearchFragment();
				fragment.setArguments(bundle);
				
				this.fragmentStack.push(fragment);
				
				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			}
			
		};
					/*Navigate Up the Stack*/
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case android.R.id.home:
				finish();
				break;
			case R.id.btn_search_filter:
				new SearchFilterDialog().show(this.getFragmentManager(), ConstantClass.SEARCH_FILTER_DIALOG_TAG);
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
			bundle.putString(ConstantClass.SEARCH_FRAGMENT_QUERY_ID, query);
			bundle.putBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_ID, false);
			SearchFragment fragment= new SearchFragment();
			fragment.setArguments(bundle);
			this.fragmentStack.push(fragment);
			AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId()); 
			return true;
		}
		
		
		
		
			
}

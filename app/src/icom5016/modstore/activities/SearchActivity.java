package icom5016.modstore.activities;

import icom5016.modstore.fragments.CartFragment;
import icom5016.modstore.fragments.SearchFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstanceClass;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class SearchActivity extends MainInterfaceActivity {
		
	private SearchFragment currentFragment;


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
				Bundle bundle = new Bundle();
				bundle.putString(ConstanceClass.SEARCH_FRAGMENT_QUERY_ID, "");
				bundle.putBoolean(ConstanceClass.SEARCH_FRAGMENT_BOOL_ID, true);
				SearchFragment fragment = new SearchFragment();
				fragment.setArguments(bundle);
				this.currentFragment = fragment;
				AndroidResourceFactory.setNewFragment(this, this.currentFragment, this.getContentFragmentId());
			}
			
		};
					/*Navigate Up the Stack*/
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
			}
			return super.onOptionsItemSelected(item);
		}
		
		
		
					/*Hide Category and Search Buttons, and Expand SearchBar */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.findItem(R.id.btn_search).setVisible(false);
			
			//Make Visible Search Bar and Expanded
			MenuItem searchBarItem = menu.findItem(R.id.searchbar);
			searchBarItem.setVisible(true);
			SearchView searchBarView = (SearchView) searchBarItem.getActionView();
			searchBarView.setIconified(false);

			searchBarView.setOnQueryTextListener(new SearchQueryListener()); 
			
			
			return true;
			
		}
			//Must be overriding for SubMenu
		@Override
		public boolean onPrepareOptionsMenu(Menu menu){
	        menu.findItem(R.id.item_categories).setVisible(false);
			return true;
		}

		
		public void cartButtonListner(MenuItem menuItem) {
			if(this.isCartActive){
				menuItem.setIcon(R.drawable.btn_cart );
				AndroidResourceFactory.setNewFragment(this, this.currentFragment , this.getContentFragmentId());
	    	}
	    	else{
	    	  menuItem.setIcon(R.drawable.navigation_cancel);
	    	  AndroidResourceFactory.setNewFragment(this, new CartFragment(), this.getContentFragmentId());
	    	}
	    	//Create A new Activity for Cart
	    	this.isCartActive = !this.isCartActive;
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
				return customQueryTextOnSubmit(query);
			}
		}
		//Custom Methods for Class SearchQueryListener
			private boolean customQueryTextOnSubmit(String query){
				Bundle bundle = new Bundle();
				bundle.putString(ConstanceClass.SEARCH_FRAGMENT_QUERY_ID, query);
				bundle.putBoolean(ConstanceClass.SEARCH_FRAGMENT_BOOL_ID, false);
				SearchFragment fragment= new SearchFragment();
				fragment.setArguments(bundle);
				this.currentFragment = fragment;
				AndroidResourceFactory.setNewFragment(this, this.currentFragment, this.getContentFragmentId()); 
				//TODO Collapse Text View and bring fragment on front
				return true;
			}
		
}

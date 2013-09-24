package icom5016.modstore.activities;

import icom5016.modstore.fragments.CartFragment;
import icom5016.modstore.fragments.SettingsFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends MainInterfaceActivity {
		
		private Fragment currentFragment;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//Obtain the ActionBar
			final ActionBar ActionBarVar = this.getActionBar();
			
			//SetActionBar to Home/Up
			ActionBarVar.setDisplayHomeAsUpEnabled(true);
			ActionBarVar.setHomeButtonEnabled(true);
			ActionBarVar.setTitle("Settings");
			
			//Load Search Fragment
			if(savedInstanceState == null){
				this.currentFragment = new SettingsFragment();
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

						/* Disable Buttons */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.findItem(R.id.btn_search).setVisible(false);
			
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
		
		//TODO: ActionListeners para Searchbar. Nota hay que a–adir Un force close al drawer cuando se de search
		
}

package icom5016.modstore.activities;

import icom5016.modstore.fragments.SettingsFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends MainInterfaceActivity {
		
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//Obtain the ActionBar
			final ActionBar ActionBarVar = this.getActionBar();
			
			//SetActionBar to Home/Up
			ActionBarVar.setDisplayHomeAsUpEnabled(true);
			ActionBarVar.setHomeButtonEnabled(true);
			ActionBarVar.setTitle(R.string.title_settings);
			
			//Load Search Fragment
			if(savedInstanceState == null){
				this.fragmentStack.push(new SettingsFragment());
				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
			}
			
		};
					/*Navigate Up the Stack*/
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			switch(item.getItemId()){
			case android.R.id.home:
				finish();
			}
			
			return super.onOptionsItemSelected(item);
		}

						/* Disable Buttons */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.findItem(R.id.btn_search).setVisible(false);
			menu.findItem(R.id.btn_cart).setVisible(false);
			
			return true;
			
		}
			//Must be overriding for SubMenu
		@Override
		public boolean onPrepareOptionsMenu(Menu menu){
	        menu.findItem(R.id.item_categories).setVisible(false);
			return true;
		}
		
		public void cartButtonListner(MenuItem menuItem) {
			//NoOp
		}
				
}

package icom5016.modstore.activities;

import icom5016.modstore.fragments.AboutFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends MainInterfaceActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ActionBar ActionBarVar = this.getActionBar();
		//Set title
		ActionBarVar.setTitle(R.string.title_about);
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
		//Set Fragment
		AndroidResourceFactory.setNewFragment(this, new AboutFragment(), MainInterfaceActivity.getContentFragmentId());

		
	}

	/*Navigate Up the Stack*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

		//Must be overriding for SubMenu
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.item_categories).setVisible(false);
		return true;
	}
	

}

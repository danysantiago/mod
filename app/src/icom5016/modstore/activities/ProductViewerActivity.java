package icom5016.modstore.activities;

import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

public class ProductViewerActivity extends MainInterfaceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get Id
		Bundle bundle = this.getIntent().getExtras();
		
		
		final ActionBar ActionBarVar = this.getActionBar();
		//Set title
		ActionBarVar.setTitle(this.getResources().getString(R.string.app_name));
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
		//Set Fragment
		ProductFragment pf = new ProductFragment();
		pf.setArguments(bundle);
		AndroidResourceFactory.setNewFragment(this, pf , MainInterfaceActivity.getContentFragmentId());

		
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

}

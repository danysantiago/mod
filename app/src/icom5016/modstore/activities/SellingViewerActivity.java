package icom5016.modstore.activities;

import icom5016.modstore.fragments.ProductSellEditFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

public class SellingViewerActivity extends MainInterfaceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get Id
		Bundle bundle = this.getIntent().getExtras();
		
		
		final ActionBar ActionBarVar = this.getActionBar();
		
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
		//Set Fragment
		int fragId = bundle.getInt(ConstantClass.SELLINGVIEWERACTIVITY_ITEM_KEY);
		switch(fragId){
		case ConstantClass.SELLINGVIEWERACTIVITY_FRAGMENT_SELL_ITEMS:
			ActionBarVar.setTitle(this.getResources().getString(R.string.title_sellitem));
			 this.fragmentStack.push(new ProductSellEditFragment());
			 AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			break;
		}
				
		

		
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

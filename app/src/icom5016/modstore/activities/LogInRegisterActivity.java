package icom5016.modstore.activities;

import icom5016.modstore.fragments.LogInRegisterFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

public class LogInRegisterActivity extends MainInterfaceActivity {

	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Disable Drawer
		this.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		
		//Get ActionBar
		final ActionBar ActionBarVar = this.getActionBar();
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
		//Get Bundle
		Bundle bundle = this.getIntent().getExtras();
		
		boolean isLoginFlag;
		//Avoid NullPointerException
		if(bundle == null)
			isLoginFlag = true;
		else
			isLoginFlag = bundle.getBoolean(ConstantClass.LOGINREGISTER_FLAG);
		
		
		//Must Load Super Fragment With View Pager
		Bundle fragmentBundle = new Bundle();
		fragmentBundle.putBoolean(ConstantClass.LOGINREGISTER_FLAG, isLoginFlag);
		LogInRegisterFragment logInRegisterFragment = new LogInRegisterFragment();
		logInRegisterFragment.setArguments(fragmentBundle);
		AndroidResourceFactory.setNewFragment(this, logInRegisterFragment, this.getContentFragmentId());

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
	
	@Override
	public void cartButtonListner(MenuItem menuItem) {
		//NoOp
	}

}

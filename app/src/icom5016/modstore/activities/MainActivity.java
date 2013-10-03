package icom5016.modstore.activities;

import icom5016.modstore.fragments.CartFragment;
import icom5016.modstore.fragments.MainCategoryFragment;
import icom5016.modstore.fragments.MainFragment;
import icom5016.modstore.fragments.MyItemsFragment;
import icom5016.modstore.fragments.SellItemFragment;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

/*
 *  SuperClass which controls Main Activity Lunch
 */


public class MainActivity extends MainInterfaceActivity {

	private ActionBarDrawerToggle mainDrawerToggle;
	
	
	
	//Method used to set home/up button as drawer button
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Get ActionBar
		final ActionBar ActionBarVar = this.getActionBar();
		
		//Get User Variable
		final User UserVar = this.activeUser; 
		
		//Enables ActionBar Logo to behave like drawer button
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
		//Make Logo Buttons Drawer Toggle
		this.mainDrawerToggle = new ActionBarDrawerToggle(
				this,
				this.mainDrawerLayout,
				R.drawable.navigation_drawer,
				R.string.drawer_open_desc,
				R.string.drawer_close_desc
				){
			public void onDrawerClosed(View view) {
				ActionBarVar.setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	if(UserVar == null){
            		ActionBarVar.setTitle("Hello Guest");
				}
				else{
					ActionBarVar.setTitle("Hello "+UserVar.getFirstName());
				}
            	
            	
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
		};
		 
		
		 this.mainDrawerLayout.setDrawerListener(this.mainDrawerToggle);
		 
		 
		 			/*  Generates Initial Fragment  */
		 Bundle bundle = this.getIntent().getExtras();
		
		 
		 if(bundle != null){
			 
			 //This only Works if Bundle is Loaded
			 int mainActivityCase = bundle.getInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY);
			 
			 //Load Fragment Base on Bundle
			 switch(mainActivityCase){
			 case ConstantClass.MAINACTIVITY_FRAGMENT_CATEGORY:
				 //Case: Category
				Bundle categoryBundle = new Bundle();
				categoryBundle.putString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY, ConstantClass.MAINCATEGORY_FRAGMENT_MAIN_VALUE);
		  		MainCategoryFragment fragment= new MainCategoryFragment();
		  		fragment.setArguments(categoryBundle);
		  		this.fragmentStack.push(fragment);
		  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
			    AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_MY_ITEMS:
				//Case: My Items
				 this.fragmentStack.push(new MyItemsFragment());
			    AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_SELL_ITEMS:
				 //Case: Sell Items
				 this.fragmentStack.push(new SellItemFragment());
			     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
				 break;
			 default:
				 //Case: Default Main View
				 this.fragmentStack.push(new MainFragment());
			     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
				 break;
			 }
		 }
		 else{
			
			 this.fragmentStack.push(new MainFragment());
		     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
		 }
	}
	
	
					/*Drawer Toggle Specific Overrides */
	
	

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (this.mainDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        switch( item.getItemId() )
        {
        //Category Listener
        case R.string.id_btn_maincategory:
        	this.loadSpecificCategoryFragment(item);
        	break;
        }
        
        //Note: Super must be call
        return super.onOptionsItemSelected(item);
	}

	 @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.mainDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        this.mainDrawerToggle.onConfigurationChanged(newConfig);
    }

 
    //Cart Button Listener 
    public void cartButtonListner(MenuItem menuItem) {
		if(this.isCartActive){
			menuItem.setIcon(R.drawable.btn_cart );
    		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek() , this.getContentFragmentId());
    	}
    	else{
    	  menuItem.setIcon(R.drawable.navigation_cancel);
    	  this.fragmentStack.push(new CartFragment());
    	  AndroidResourceFactory.setNewFragment(this, new CartFragment(), this.getContentFragmentId());
    	}
    	//Create A new Activity for Cart
    	this.isCartActive = !this.isCartActive;
		
	}
    
    //Category Menu
  	private void loadSpecificCategoryFragment(MenuItem item) {
  		Bundle bundle = new Bundle();
  		bundle.putString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY, (String) item.getTitle());
  		MainCategoryFragment fragment= new MainCategoryFragment();
  		fragment.setArguments(bundle);
  		this.fragmentStack.push(fragment);
  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
  	}

}

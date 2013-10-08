package icom5016.modstore.activities;

import icom5016.modstore.fragments.CategoryListFragment;
import icom5016.modstore.fragments.ItemWatchFragment;
import icom5016.modstore.fragments.MainFragment;
import icom5016.modstore.fragments.ProductListFragment;
import icom5016.modstore.fragments.ProductSellEditFragment;
import icom5016.modstore.fragments.ProductsForSaleFragment;
import icom5016.modstore.fragments.ProductsSoldFragment;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
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
            	ActionBarVar.setTitle("Hello Guest");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
		};
		 
		
		 this.mainDrawerLayout.setDrawerListener(this.mainDrawerToggle);
		 
		 
		 			/*  Generates Initial Fragment  */
		 Bundle bundle = this.getIntent().getExtras();
		 Bundle fragmentBundle = new Bundle();
		 
		 if(bundle != null){
			 
			 //This only Works if Bundle is Loaded
			 int mainActivityCase = bundle.getInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY);
			 
			 //Load Fragment Base on Bundle
			 switch(mainActivityCase){
			 case ConstantClass.MAINACTIVITY_FRAGMENT_CATEGORY:
				 //Case: Category
				fragmentBundle.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
	    	  	CategoryListFragment fragment= new CategoryListFragment();
	    	  	fragment.setArguments(fragmentBundle);
	    	  	this.fragmentStack.push(fragment);
		  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_MY_ITEMS:
				//Case: My Items
				 this.fragmentStack.push(new ItemWatchFragment());
			    AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_SELL_ITEMS:
			    ProductSellEditFragment sellEditfragment= new ProductSellEditFragment();
    	  		this.fragmentStack.push(sellEditfragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_ITEMS_FOR_SALE:
				ProductsForSaleFragment forSalefragment = new ProductsForSaleFragment();
	    	  	this.fragmentStack.push(forSalefragment);
	    	  	AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				break;
			 case ConstantClass.MAINACTIVITY_FRAGMENT_ITEMS_SOLD:
				ProductsSoldFragment soldFragment = new ProductsSoldFragment();
	    	  	this.fragmentStack.push(soldFragment);
	    	  	AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				break;
			
			 default:
				 //Case: Default Main View
				 this.fragmentStack.push(new MainFragment());
			     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
				 break;
			 }
		 }
		 else{
			
			 this.fragmentStack.push(new MainFragment());
		     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
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

 
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	final ActionBar ActionBarVar = this.getActionBar();
    	boolean drawerOpen = this.mainDrawerLayout.isDrawerOpen(this.mainDrawerList);
    	if(drawerOpen){
    		if(this.activeUser == null){
    			ActionBarVar.setTitle("Hello Guest");
    		}
    		else{
    			ActionBarVar.setTitle("Hello "+this.activeUser.getFirstName());
    		}
    	}
    	
    	return super.onPrepareOptionsMenu(menu);
		
	}


    
    
    
    //Category Menu
  	private void loadSpecificCategoryFragment(MenuItem item) {
  		//Iterate to Confirm selection
  		Category selectedCategory = null;
  		for(Category e: this.mainCategories){
  			if(e.getName().equals(item.getTitle())){
  				selectedCategory = e;
  				break;
  			}
  		}
  		Bundle bundle = new Bundle();
  		bundle.putInt(ConstantClass.PRODUCT_LIST_CATEGORY_KEY, selectedCategory.getId());
  		ProductListFragment fragment= new ProductListFragment();
  		fragment.setArguments(bundle);
  		this.fragmentStack.push(fragment);
  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
  		
  	}

}

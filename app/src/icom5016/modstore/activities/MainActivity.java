package icom5016.modstore.activities;

import icom5016.modstore.fragments.MainFragment;
import icom5016.modstore.fragments.MyItemsFragment;
import icom5016.modstore.fragments.ProductListFragment;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/*
 *  SuperClass which controls Main Activity Lunch
 */


public class MainActivity extends MainInterfaceActivity {

	private ActionBarDrawerToggle mainDrawerToggle;
	
	
	
					/* Set Ups */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Get ActionBar
		final ActionBar ActionBarVar = this.getActionBar();
		
						/* Make Drawer Toggleble from Action Bar */
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
		 
		 if(bundle != null){
			 
			 //This only Works if Bundle is Loaded
			 int mainActivityCase = bundle.getInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY);
			 
			 //Load Fragment Base on Bundle
			 switch(mainActivityCase){
			 case ConstantClass.MAINACTIVITY_FRAGMENT_MY_ITEMS:
				//Case: My Items
				 this.fragmentStack.push(new MyItemsFragment());
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
			
//			 this.fragmentStack.push(new MainFragment());
//		     AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
		 }
		 
		 //Load Categories 
		 this.loadAllCategories();
		 
		 
	}
	
						/* Menu Related Methods */

						/*	Listeners */
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


    //Load Specific Category Fragment 
  	private void loadSpecificCategoryFragment(MenuItem item) {
  		
  		//Check if not Working
  		if(this.mainCategoriesList.size() <= 0)
  			return;
  		
  		//Iterate to Confirm selection
  		Category selectedCategory = null;
  		for(Category e: this.mainCategoriesList){
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
	
	
					/* All Related to Drawer Opening Methods */
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
    
    protected boolean areMainCategoriesListLoaded(){
		return this.mainCategoriesList.size() <= 0;
	}
    protected void updateSubMenuCategories(Menu menu){
		SubMenu categoriesMenu = (SubMenu) menu.findItem(R.id.item_categories).getSubMenu();
		for(Category e : this.mainCategoriesList)
		{
			categoriesMenu.add(R.id.item_categories, R.string.id_btn_maincategory , Menu.NONE, e.getName());
		}
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
    	
    	//Removes Options based on Drawer Status
        menu.findItem(R.id.item_categories).setVisible(!drawerOpen);
        menu.findItem(R.id.btn_cart).setVisible(!drawerOpen);
        menu.findItem(R.id.btn_search).setVisible(!drawerOpen);
        
        //Make sure it loads
        if(!this.areMainCategoriesListLoaded())
			this.updateSubMenuCategories(menu);
    	
    	return super.onPrepareOptionsMenu(menu);
		
	}

}

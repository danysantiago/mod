package icom5016.modstore.activities;

import icom5016.modstore.fragments.CartFragment;
import icom5016.modstore.fragments.MainFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
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
                ActionBarVar.setTitle("Hello User");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
		};
		
		 this.mainDrawerLayout.setDrawerListener(this.mainDrawerToggle);
		 
		 
		 			/*  Generates Initial Fragment  */
		//Default Fragment
	    if (savedInstanceState == null) {
	    	AndroidResourceFactory.setNewFragment(this, new MainFragment(), this.getContentFragmentId());
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
        
        
        //Note Super must be call
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
    		AndroidResourceFactory.setNewFragment(this, new MainFragment() , this.getContentFragmentId());
    	}
    	else{
    	  menuItem.setIcon(R.drawable.navigation_cancel);
    	  AndroidResourceFactory.setNewFragment(this, new CartFragment(), this.getContentFragmentId());
    	}
    	//Create A new Activity for Cart
    	this.isCartActive = !this.isCartActive;
		
	}
    
    

}
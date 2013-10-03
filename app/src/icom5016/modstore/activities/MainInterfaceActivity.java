package icom5016.modstore.activities;

import icom5016.modstore.fragments.MainCategoryFragment;
import icom5016.modstore.fragments.MyItemsFragment;
import icom5016.modstore.fragments.SellItemFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;
import icom5016.modstore.resources.User;
import icom5016.modstore.uielements.DrawerAdapter;

import java.util.Stack;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/*
 *  Abstract Class with template for Activity Bar
 */

public abstract class MainInterfaceActivity extends Activity {
	
					/* Instance variables */
	
	
	//Drawer Variables
	protected DrawerLayout mainDrawerLayout; //Contains main layout variable
	protected ListView mainDrawerList; //ListView use forDrawer
	
	//User variable
	protected User activeUser = null;
		//If Null User Does Not Exist
	
	//Cart Toggle Variable
	protected boolean isCartActive = false;
	
	//Fragment Stack
	protected Stack<Fragment> fragmentStack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Verify Log-In
		SharedPreferences preferences = //If Open First Time: Creates File; O.W. Reads it
				this.getSharedPreferences(ConstantClass.USER_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		boolean isUserLogIn = preferences.getBoolean(ConstantClass.USER_IS_LOGIN, false);
		
			//If Log-In Create 
		if(isUserLogIn){
			//pre: must be log-in thus data will be loaded into preferences
			String userName = preferences.getString(ConstantClass.USER_USERNAME_KEY, ""); 
			String firstName = preferences.getString(ConstantClass.USER_FIRSTNAME_KEY, "");
			String middleName = preferences.getString(ConstantClass.USER_MIDDLENAME_KEY, "");
			String lastName = preferences.getString(ConstantClass.USER_LASTNAME_KEY, "");
			String email = preferences.getString(ConstantClass.USER_EMAIL_KEY, "");
			boolean isAdmin = preferences.getBoolean(ConstantClass.USER_IS_ADMIN_KEY, false);	
			this.activeUser = new User(userName, firstName, middleName, lastName, email, isAdmin);	
		}
		
		
			//Load ActionBar Variable
		final ActionBar ActionBarVar = this.getActionBar();
		
			//Set Action Bar title
		ActionBarVar.setTitle(R.string.app_name);
		
		
		//Load Variables
		this.mainDrawerLayout  = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.mainDrawerList    = (ListView) this.findViewById(R.id.left_drawer);
		
		//Set Option into Drawer
		this.mainDrawerList.setAdapter(
				new DrawerAdapter(this, this.activeUser)
		);
		
		//Set Drawer ClickListener 
		this.mainDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		//Init Stack
		this.fragmentStack = new Stack<Fragment>();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionbar_main_menu, menu);
		
		SubMenu categoriesMenu = (SubMenu) menu.findItem(R.id.item_categories).getSubMenu();
		String[] mainCategories = DataFetchFactory.fetchMainCategories();
		for(String e : mainCategories)
		{
			categoriesMenu.add(R.id.item_categories, R.string.id_btn_maincategory , Menu.NONE, e);
		}
		return super.onCreateOptionsMenu(menu);
	}
					
							/*Population Methods*/
	
	
	//Make Invalid to Change Category as it changes the fragment 
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		boolean drawerOpen = this.mainDrawerLayout.isDrawerOpen(this.mainDrawerList);
        menu.findItem(R.id.item_categories).setVisible(!drawerOpen);
        menu.findItem(R.id.btn_cart).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
		
	
	
	
	
						/*Supplementary Methods*/
	
	protected int getContentFragmentId()
	{
		return R.id.content_frame;
	}
	
	
						/*Listeners Implementation*/
	
	//ActionBar Buttons
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() )
        {
        //CallBack Search
        case R.id.btn_search:
        	//Start Search Intent
        	Intent searchIntent = new Intent(this, SearchActivity.class);
        	this.startActivity(searchIntent);
        	//Create a new search activity that looks good.
        	return true;
        default:
        	//Return Super Call if not valid
        	return super.onOptionsItemSelected(item);
        	
        }

	}
	
	//Drawer
		//**** Hack to Access Local Class Variables and Methods
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
    	if(this.activeUser == null)
    	{
    		this.guestDrawerListener(position);
    	}
    	else{
    		this.userDrawerListener(position);
    	}
    	this.mainDrawerList.setItemChecked(position, false);
    	this.mainDrawerLayout.closeDrawer(this.mainDrawerList);
    }

    //Listener for Drawers
    	//Guest Drawer
    private void guestDrawerListener(int position){
    	
    	//Global Bundle for Each. Saving Memory, 
    	Bundle bundle = new Bundle();
    	
    	switch(position){
    	case 0:
    		//Home (Refresh)
    		if(this instanceof MainActivity ){
    			this.finish();
        		this.startActivity(this.getIntent());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 1:
    		//Category (new Fragment)
    		//Home (Refresh)
    		if(this instanceof MainActivity ){
    	  		bundle.putString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY, ConstantClass.MAINCATEGORY_FRAGMENT_MAIN_VALUE);
    	  		MainCategoryFragment fragment= new MainCategoryFragment();
    	  		fragment.setArguments(bundle);
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_CATEGORY);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 2:
    		//About (new Activity)
    		Intent aboutIntent = new Intent(this, AboutActivity.class);
    		this.startActivity(aboutIntent);
    		break;
    	case 3:
    		//Log-In (new Activity)
    		Intent loginIntent = new Intent(this, LogInRegisterActivity.class);
    		bundle.putBoolean(ConstantClass.LOGINREGISTER_FLAG, true); //true to start in login
    		loginIntent.putExtras(bundle);
        	this.startActivity(loginIntent);
    		break;
    	case 4:
    		//Register (new Register)
    		Intent registerIntent = new Intent(this, LogInRegisterActivity.class);
    		bundle.putBoolean(ConstantClass.LOGINREGISTER_FLAG, false); //false to start in login
    		registerIntent.putExtras(bundle);
        	this.startActivity(registerIntent);
    		break;
    	}
    }
    	//User Drawer
    private void userDrawerListener(int position){
    	
    	//Global Bundle for Each. Saving Memory, 
    	Bundle bundle = new Bundle();
    	
    	switch(position){
    	case 0:
    		//Home (Reload)
    		if(this instanceof MainActivity ){
    			this.finish();
        		this.startActivity(this.getIntent());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 1:
    		//Categories (new Fragment)
    		if(this instanceof MainActivity ){
    	  		bundle.putString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY, ConstantClass.MAINCATEGORY_FRAGMENT_MAIN_VALUE);
    	  		MainCategoryFragment fragment= new MainCategoryFragment();
    	  		fragment.setArguments(bundle);
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_CATEGORY);
    			homeIntent.putExtras(bundle);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 2:
    		//My Items (new Fragment)
    		//Categories (new Fragment)
    		if(this instanceof MainActivity ){
    	  		MyItemsFragment fragment= new MyItemsFragment();
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_MY_ITEMS);
    			homeIntent.putExtras(bundle);
    			this.startActivity(homeIntent);
    		}
    		
    		break;
    	case 3:
    		//Sell Item (new Fragment)
    		if(this instanceof MainActivity ){
    	  		SellItemFragment fragment= new SellItemFragment();
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_SELL_ITEMS);
    			homeIntent.putExtras(bundle);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 4:
    		//Settings (new Activity)
    		Intent settingsIntent = new Intent(this, SettingsActivity.class);
    		this.startActivity(settingsIntent);
    		break;
    	case 5:
    		//About (new Activity)
    		Intent aboutIntent = new Intent(this, AboutActivity.class);
    		this.startActivity(aboutIntent);
    		break;
    	case 6:
    		//Log-Out (refresh)
    		
    			//Destroy Preferences
    		SharedPreferences preferences = //If Open First Time: Creates File; O.W. Reads it
			this.getSharedPreferences(ConstantClass.USER_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
    		preferences.edit().clear().commit();
    			//Refresh MainActivity
    		if(this instanceof MainActivity ){
    			this.finish();
        		this.startActivity(this.getIntent());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			this.startActivity(homeIntent);
    		}
    		break;
    	}
    }
    
  	//Make On Back Return to Previous Element
  		@Override
  		public void onBackPressed() {
  			//Normal Back if no other Fragment is Use
  			if(this.fragmentStack.size() <= 1){
  				super.onBackPressed();
  			}
  			else{
  				this.fragmentStack.pop();
  				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
  			}
  		}
  	
    
    //Cart Button Listener Abstract
    public abstract void cartButtonListner(MenuItem menuItem);
}

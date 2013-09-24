package icom5016.modstore.activities;

import icom5016.modstore.fragments.MainCategoryFragment;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstanceClass;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/*
 *  Abstract Class with template for Activity Bar
 */

public abstract class MainInterfaceActivity extends Activity {
	
					/* Instance variables */
	
	//Drawer Variables
	//Variables for Drawer
	protected DrawerLayout mainDrawerLayout; //Contains main layout variable
		//Protected for editability in other classes
	private ListView mainDrawerList; //ListView use forDrawer
	//Note Toggler Must be Implement Directly on the Activity
	private String[] drawerOptionsList; //List of Drawer Options
	
	//Cart Boolean Variable
	protected boolean isCartActive = false;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Load ActionBar
		final ActionBar ActionBarVar = this.getActionBar();
		
		//Set Action Bar title
		ActionBarVar.setTitle(R.string.app_name);
		
		
		//Load Variables
		this.mainDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.mainDrawerList = (ListView) this.findViewById(R.id.left_drawer);
		this.drawerOptionsList = DataFetchFactory.fetchDrawerOptions();
		
		//Load List View in to Drawer
		this.mainDrawerList.setAdapter(
				new ArrayAdapter<String>(this, R.layout.drawer_list_item, this.drawerOptionsList)
		);
		
		//Set Drawer Listener 
		this.mainDrawerList.setOnItemClickListener(new DrawerItemClickListener()); 
			//DrawerItemClickListener Local Private Class
		
		
		
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
		Log.v("ID of Element", Integer.toString(item.getItemId()) );
        switch( item.getItemId() )
        {
        //CallBack Search
        case R.id.btn_search:
        	//Start Search Intent
        	Intent searchIntent = new Intent(this, SearchActivity.class);
        	this.startActivity(searchIntent);
        	//Create a new search activity that looks good.
        	return true;
        case R.string.id_btn_maincategory:
        	this.loadCategoryFragment(item);
        	return true;
        default:
        	//Return Super Call if not valid
        	return super.onOptionsItemSelected(item);
        	
        }

	}
	
	//Category Menu
	private void loadCategoryFragment(MenuItem item) {
		Bundle bundle = new Bundle();
		bundle.putString(ConstanceClass.MAINCATEGORY_FRAGMENT_CATEGORY_ID, (String) item.getTitle());
		MainCategoryFragment fragment= new MainCategoryFragment();
		fragment.setArguments(bundle);
		//TODO: Make Fragment work with cart
		AndroidResourceFactory.setNewFragment(this, fragment, this.getContentFragmentId());
		
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
    	this.initDrawerActivity(position);
    	this.mainDrawerList.setItemChecked(position, true);
    	this.mainDrawerLayout.closeDrawer(this.mainDrawerList);
    }

    //Listener for specific values of drawer
    private void initDrawerActivity(int position){
    	switch(position){
    	case 0: //Home (Restart Main Activity)
    		Intent homeIntent = new Intent(this, MainActivity.class);
        	this.startActivity(homeIntent);
        	break;
    	case 2: //Settings
    		Intent settingsIntent = new Intent(this, SettingsActivity.class);
    		this.startActivity(settingsIntent);
    	}
    }
    
    
    //TODO: Create fragment change with categories
    
    
    //Cart Button Listener Abstract
    public abstract void cartButtonListner(MenuItem menuItem);
}

package icom5016.modstore.activities;

import icom5016.modstore.adapters.DrawerAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


/*
 *  Abstract Class with template for Activity Bar
 */

public abstract class MainInterfaceActivity extends FragmentActivity {
	
					/* Instance variables */
					
					/*Category Vairables */
	
	
	
	//Drawer Variables
	protected DrawerLayout mainDrawerLayout; //Contains main layout variable
	protected ListView mainDrawerList; //ListView use forDrawer
	
	//User variable
	protected User activeUser = null;
		//If Null User Does Not Exist
	
	//Fragment Stack
	public Stack<Fragment> fragmentStack;
	
	//Own Reference
	private MainInterfaceActivity thisActivity;
	
	//Progress Dialog
	protected ProgressDialog processDialog;
	
	
	//MainCategoryList
	protected List<Category> mainCategoriesList = new ArrayList<Category>(); //If Size is 0 Nothing is Listed
	
	
	
	
	
						/* Set Up Methods */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Load self activity
		this.thisActivity = this;
		
		//Load ActionBar Variable
		final ActionBar ActionBarVar = this.getActionBar();
		
		//Set Action Bar title
		ActionBarVar.setTitle(R.string.app_name);
		
		//Load Variables
		this.mainDrawerLayout  = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.mainDrawerList    = (ListView) this.findViewById(R.id.left_drawer);
		
		//Init Fragment Stack
		this.fragmentStack = new Stack<Fragment>();
		
		//Init Progress Dialog
		this.processDialog = new ProgressDialog(this);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//Load Current User
		loadActiveUser();
			//No Verification needed beacuse user can be NULL
		
		//Set DrawerList based on the type of user
		this.mainDrawerList.setAdapter(
				new DrawerAdapter(this, this.activeUser)
		);
		
		//Set Drawer ClickListener 
		this.mainDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		
		
	}
	
							/* Data Fetch Methods for Other Clases */
				/*Since ALl Methods in this section are treated as Asyncronous 
				 * it must be verify with a verificationMethod */
	
	public List<Category> loadCategoriesById(int parentId) { 		
		SharedPreferences spf = this.getSharedPreferences(ConstantClass.CATEGORIES_FILE, Context.MODE_PRIVATE);
		
		List<Category> result = new ArrayList<Category>();
		
		if(!spf.getBoolean(ConstantClass.CategoriesFile.LOAD_BOOLEAN_KEY, false)){
			Toast.makeText(this, R.string.errmsg_cat_notloaded, Toast.LENGTH_SHORT).show();
			return result;
		}
		
		List<Category> categories = new ArrayList<Category>();
		String strJson = spf.getString(ConstantClass.CategoriesFile.ALL_CAT_JSON_KEY, "ERRORKey");
		if(strJson != null && !strJson.equals("ERRORKey")){
			try {
				JSONObject jsonCat = new JSONObject(strJson);
				JSONArray array = jsonCat.getJSONArray("categories");
				for(int i=0; i<array.length(); i++)
					categories.add(new Category(array.getJSONObject(i)));
			} catch (JSONException e) {
				Toast.makeText(this, R.string.errmsg_bad_json, Toast.LENGTH_SHORT).show();
			}
		}
		
		
		if(parentId == ConstantClass.CategoriesFile.ALL_CATEGORIES)
			return categories;
		
		for(Category e: categories){
			if(e.getParentId() == parentId){
				result.add(e);
			}
		}
		return result;
		
	}
	
	
	
	
	protected void loadAllCategories(){
		processDialog.setMessage(this.getResources().getText(R.string.pd_mactivity));
		processDialog.show();
		getAllCategoriesFromHTTP();
	}
	
	
	private void getAllCategoriesFromHTTP() {

		//Perform http request
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.Categories.GETALL); //Get All Category
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				//Also add to editor;
				Editor catFileEditor = getSharedPreferences(ConstantClass.CATEGORIES_FILE, Context.MODE_PRIVATE).edit();
			
				catFileEditor.putString(ConstantClass.CategoriesFile.ALL_CAT_JSON_KEY, json.toString());
				catFileEditor.putBoolean(ConstantClass.CategoriesFile.LOAD_BOOLEAN_KEY, true);
				catFileEditor.commit();
				
				mainCategoriesList = loadCategoriesById(-1);
				invalidateOptionsMenu();
				processDialog.dismiss();
					
			}

			@Override
			public void onFailed() {
				Toast.makeText(thisActivity, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onDone(){
				//Regardless Dimsiss Progress Dialog
				processDialog.dismiss();
			}
			
		});
		request.execute();
	}
	
	

	protected void loadActiveUser(){
		this.activeUser = DataFetchFactory.getUserFromSPref(this);
	}

	
									/*Verification Methods
									 * They work on pares with Load Methods for Async Verification*/
	
	
	
	protected boolean isUserLogin(boolean sendToLogInFlag){ //Starts New Activity Based on Flag
		if(sendToLogInFlag){
			if(this.activeUser == null){
				//Flush User File
				SharedPreferences preferences = getSharedPreferences(ConstantClass.USER_FILE, Context.MODE_PRIVATE);
	    		preferences.edit().clear().commit();
				
				//Start Login Activity
				Intent loginIntent = new Intent(this, LogInRegisterActivity.class);
				this.startActivity(loginIntent);
				
			}
		}
		return this.activeUser == null;
	}
		
		
							/*Option Menus Generation */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionbar_main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
					
	
						/*Supplementary Methods*/
	
	//Method that obtain the Content Frame for all Activities
	public static int getContentFragmentId()
	{
		return R.id.content_frame;
	}
	
	//Getter for Active User use inside Fragments
	public User getActiveUser() {
		return activeUser;
	}
	
						/*Listeners Implementation*/
	
	

	//ActionBar Buttons
	// Note: Only the ones that are shared are implemented here
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
	

							/* Navigation Methods */
	@Override
	public void onBackPressed() {

			//Normal Back if no other Fragment is Use
			if(this.fragmentStack.size() <= 1){
				super.onBackPressed();
			}
			else{
				this.fragmentStack.pop();
				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			}		
	}

	
			/* Drawer Listener */

	//**** Hack to Access Local Class Variables and Methods
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
    	
    	//Base on Usertype matches the actions listeners
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

    							/*Listener for Drawers */
    
    //Guest Drawer
    private void guestDrawerListener(int position){
    	
    	//Global Bundle
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
    		//About (new Activity)
    		Intent aboutIntent = new Intent(this, AboutActivity.class);
    		this.startActivity(aboutIntent);
    		break;
    	case 2:
    		//Log-In (new Activity)
    		Intent loginIntent = new Intent(this, LogInRegisterActivity.class);
    		bundle.putBoolean(ConstantClass.LOGINREGISTER_FLAG, true); //true to start in login
    		loginIntent.putExtras(bundle);
        	this.startActivity(loginIntent);
    		break;
    	case 3:
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
    	
    	//Global Bundle
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
    		//My Orders
    		Intent myOrdersIntent = new Intent(this, MyOrdersActivity.class);
    		this.startActivity(myOrdersIntent);
    		break;
    	case 2:
    		//Sell Item (new Activity)
    		//bundle.putInt(ConstantClass.SELLINGVIEWERACTIVITY_ITEM_KEY, ConstantClass.SELLINGVIEWERACTIVITY_FRAGMENT_SELL_ITEMS);
    		Intent sellingIntent = new Intent(this, ProductSellEditActivity.class);
    		//sellingIntent.putExtras(bundle);
    		this.startActivity(sellingIntent);
    		break;
    	case 3:
    		//My Account (Old: Settings) (new Activity)
    		bundle.putInt(ConstantClass.USER_GUID_KEY, this.activeUser.getGuid());
    		Intent settingsIntent = new Intent(this, SettingsActivity.class);
    		settingsIntent.putExtras(bundle);
    		this.startActivity(settingsIntent);
    		break;
    	case 4:
    		//About (new Activity)
    		Intent aboutIntent = new Intent(this, AboutActivity.class);
    		this.startActivity(aboutIntent);
    		break;
    	case 5:
    		//Log-Out (refresh)
    		
    		//Destroy Preferences
    		SharedPreferences preferences = getSharedPreferences(ConstantClass.USER_FILE, Context.MODE_PRIVATE);
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
    	
    	case 6:
    		//Admin Menu
    			//Send User ID
    		bundle.putInt(ConstantClass.USER_GUID_KEY, this.activeUser.getGuid());
    		Intent adminIntent = new Intent(this, AdminActivity.class);
    		adminIntent.putExtras(bundle);
    		this.startActivity(adminIntent);
    	}
    }

	
    
}

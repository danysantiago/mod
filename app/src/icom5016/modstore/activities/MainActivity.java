package icom5016.modstore.activities;

import icom5016.modstore.adapter.MainListAdapter;
import icom5016.modstore.fragments.AboutFragment;
import icom5016.modstore.fragments.AdminFragment;
import icom5016.modstore.fragments.CartFragment;
import icom5016.modstore.fragments.CategoryListFragment;
import icom5016.modstore.fragments.LogInFragment;
import icom5016.modstore.fragments.MainFragment;
import icom5016.modstore.fragments.MyStoreFragment;
import icom5016.modstore.fragments.RegisterFragment;
import icom5016.modstore.fragments.SearchFragment;
import icom5016.modstore.fragments.SellProductFragment;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	/*User */
	private User activeUser = null;
	
	//Fragment Stack
	public Stack<Fragment> fragmentStack;
	
	//Progress Dialog
	public ProgressDialog processDialog;
	
	//Self Ref
	private MainActivity thisActivity;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		this.fragmentStack = new Stack<Fragment>();
		
		//Load Main Fragment fragment
		//this.loadFragmentInMainActivityStack(getContainerId(), new MainFragment());
		processDialog = new ProgressDialog(this);
		this.thisActivity = this;
		loadAllCategories();
		
		reloadActionBarAndUser(getActionBar());
		
	}
	

	
	public User getActiveUser() {
		return activeUser;
	}


	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}


	@Override
	protected void onResume() {
		super.onResume();
		//No Verification needed because user can be NULL
		//final ActionBar actionBar = this.getActionBar();
		//this.reloadActionBarAndUser(actionBar);
		
	}
	
	public Category loadCategoryById(int id){
		List<Category> allCategories = this.loadCategoriesById(ConstantClass.CategoriesFile.ALL_CATEGORIES);
		
		for(Category e: allCategories){
			if(e.getId() == id)
				return e;
		}
		
		return new Category(-3, -3, "Not Found");
	}
	
	//Load Categories
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
	
	
	
	
	public void loadAllCategories(){
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

	
	public void reloadActionBarAndUser(ActionBar actionBar){
		// Load Current User
		loadActiveUser();
		// No Verification needed beacuse user can be NULL
		actionBar.setTitle(R.string.app_name);
		MainListAdapter mla = new MainListAdapter(actionBar.getThemedContext(),activeUser);
		actionBar.setListNavigationCallbacks(mla, this);
	}
	
	
	protected void loadActiveUser(){
		this.activeUser = DataFetchFactory.getUserFromSPref(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	//Action Listener
	@Override 
	public boolean onNavigationItemSelected(int position, long id) {

		if(this.activeUser == null) {
    		return this.guestDrawerListener(position);
    	} else{
    		return this.userDrawerListener(position);
    	}
	}
	
		/*Listener for Drawers */
    
    //Guest Drawer
    private boolean guestDrawerListener(int position){
    	switch(position){
    	case 0:
    		this.loadFragmentInMainActivityStack(getContainerId(), new MainFragment());
    		break;
    	case 1:
    		//Shop By Category
    		CategoryListFragment clf = new CategoryListFragment();
    		Bundle bnd = new Bundle();
    		bnd.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
    		clf.setArguments(bnd);
    		this.loadFragmentInMainActivityStack(getContainerId(), clf);
    		break;
    	case 2:
    		//About Fragment
    		this.loadFragmentInMainActivityStack(getContainerId(), new AboutFragment());
    		break;
    	case 3:
    		//Login Fragment
    		this.loadFragmentInMainActivityStack(getContainerId(), new LogInFragment());
    		break;
    	case 4:
    		//Register Fragment
    		this.loadFragmentInMainActivityStack(getContainerId(), new RegisterFragment());
    		break;
		default:
			return false;
    	}
    	return true;
    }
    
    //User Drawer
    private boolean userDrawerListener(int position){
    	Bundle bundle = new Bundle();
    	switch(position){
    	case 0:
    		this.loadFragmentInMainActivityStack(getContainerId(), new MainFragment());
    		break;
    	case 1:
    		//Shop By Category
    		CategoryListFragment clf = new CategoryListFragment();
    		Bundle bnd = new Bundle();
    		bnd.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
    		clf.setArguments(bnd);
    		this.loadFragmentInMainActivityStack(getContainerId(), clf);
    		break;
    	case 2:
    		//MyStore
    		this.loadFragmentInMainActivityStack(getContainerId(), new MyStoreFragment());
    		break;
    	case 3:
    		//SellItem
    		SellProductFragment spf = new SellProductFragment();
    		this.loadFragmentInMainActivityStack(getContainerId(), spf);
    		break;
    	case 4:
    		//MyAccount
    		bundle.putInt(ConstantClass.USER_GUID_KEY, this.activeUser.getGuid());
    		Intent myacc = new Intent(this, SettingsActivity.class);
    		myacc.putExtras(bundle);
    		this.startActivity(myacc);
    		break;
    	case 5:
    		//About Fragment
    		this.loadFragmentInMainActivityStack(getContainerId(), new AboutFragment());
    		break;
    	case 6:
    		//Log-Out (refresh)
    		
    		//Destroy Preferences
    		SharedPreferences preferences = getSharedPreferences(ConstantClass.USER_FILE, Context.MODE_PRIVATE);
    		preferences.edit().clear().commit();
    		//Refresh MainActivity
    		if(this instanceof MainActivity ){
    			this.finish();
        		this.startActivity(this.getIntent());
    		}
    		
    		break;
    	case 7:
    		//Admin Menu
    		this.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new AdminFragment());
    		break;
    	default:
    		return false;
    	}
    	
    	return true;
    }
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}
	
	
		/* Navigation Methods */
	@Override
	public void onBackPressed() {
	
		// Normal Back if no other Fragment is Use
		if (this.fragmentStack.size() <= 1) {
			super.onBackPressed();
		} else {
			this.fragmentStack.pop();
			AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), R.id.container);
		}	
	}
		
	public static int getContainerId(){
		return R.id.container;
	}
	
	public void loadFragmentInMainActivityStack(int id, Fragment fragment){
		FragmentManager fm = this.getSupportFragmentManager();
		this.fragmentStack.push(fragment);
		fm.beginTransaction().replace(MainActivity.getContainerId(), this.fragmentStack.peek()).commit();
	}
	
	/* Listner */
	public void cartBtnListener(MenuItem item){
		//Check if log-in
		if(this.activeUser == null){
			Toast.makeText(this, R.string.error_cart, Toast.LENGTH_SHORT).show();
			this.loadFragmentInMainActivityStack(getContainerId(), new LogInFragment());
			return;
		}else{
			this.loadFragmentInMainActivityStack(getContainerId(), new CartFragment());
		}
	}
	
	public void searchBtnListener(MenuItem item){
		this.loadFragmentInMainActivityStack(getContainerId(), new SearchFragment());
		
	}
	
	

}

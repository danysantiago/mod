package icom5016.modstore.activities;

import icom5016.modstore.fragments.CategoryListFragment;
import icom5016.modstore.fragments.ProductSellEditFragment;
import icom5016.modstore.fragments.ProductsForSaleFragment;
import icom5016.modstore.fragments.ProductsSoldFragment;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;
import icom5016.modstore.uielements.DrawerAdapter;

import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


/*
 *  Abstract Class with template for Activity Bar
 */

public abstract class MainInterfaceActivity extends Activity {
	
					/* Instance variables */
					
					/*Progress Dialog*/
	private ProgressDialog pd;
					/*Category Variables */
	protected Category[] mainCategories;
	
					/*Cart Variables */
	private PopupWindow popUp;
	
	//Drawer Variables
	protected DrawerLayout mainDrawerLayout; //Contains main layout variable
	protected ListView mainDrawerList; //ListView use forDrawer
	
	//User variable
	protected User activeUser = null;
		//If Null User Does Not Exist
	
	//Fragment Stack
	public Stack<Fragment> fragmentStack;
	
	private MainInterfaceActivity thisActivity;
	
	Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		thisActivity = this;
		
			//Load ActionBar Variable
		final ActionBar ActionBarVar = this.getActionBar();
		
			//Set Action Bar title
		ActionBarVar.setTitle(R.string.app_name);
		
		//Load Variables
		this.mainDrawerLayout  = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.mainDrawerList    = (ListView) this.findViewById(R.id.left_drawer);
		
		//Init Stack
		this.fragmentStack = new Stack<Fragment>();
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		this.activeUser = DataFetchFactory.getUserInSharedPreferences(this);
		
		//Set Option into Drawer
		this.mainDrawerList.setAdapter(
				new DrawerAdapter(this, this.activeUser)
		);
		
		//Set Drawer ClickListener 
		this.mainDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionbar_main_menu, menu);
		
		SubMenu categoriesMenu = (SubMenu) menu.findItem(R.id.item_categories).getSubMenu();
		this.mainCategories = DataFetchFactory.fetchMainCategories();
		for(Category e : mainCategories)
		{
			categoriesMenu.add(R.id.item_categories, R.string.id_btn_maincategory , Menu.NONE, e.getName());
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
	
	public static int getContentFragmentId()
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
    	  		bundle.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
    	  		CategoryListFragment fragment= new CategoryListFragment();
    	  		fragment.setArguments(bundle);
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
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
    	bundle = new Bundle();
    	
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
    			bundle.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
    	  		CategoryListFragment fragment= new CategoryListFragment();
    	  		fragment.setArguments(bundle);
    	  		this.fragmentStack.push(fragment);
    	  		AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
    		}
    		else{
    			Intent homeIntent = new Intent(this, MainActivity.class);
    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_CATEGORY);
    			homeIntent.putExtras(bundle);
    			this.startActivity(homeIntent);
    		}
    		break;
    	case 2:
    		break;
    	case 3:
    		//My Market (new Fragments: Sell Item, Items for Sale, Items Sold)
    		AlertDialog.Builder myItemsDialog = new AlertDialog.Builder(this);
    		myItemsDialog.setTitle("My Market")
			   .setNegativeButton("Sell Item", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
			    		if(thisActivity instanceof MainActivity ){
			    	  		ProductSellEditFragment fragment= new ProductSellEditFragment();
			    	  		thisActivity.fragmentStack.push(fragment);
			    	  		AndroidResourceFactory.setNewFragment(thisActivity, thisActivity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			    		}
			    		else{
			    			Intent homeIntent = new Intent(thisActivity, MainActivity.class);
			    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_SELL_ITEMS);
			    			homeIntent.putExtras(bundle);
			    			thisActivity.startActivity(homeIntent);
			    		}
			       }
			   })
			   .setNeutralButton("Items for Sale", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
			    		//Items for Sale (new Fragment)
			    		if(thisActivity instanceof MainActivity ){
			    	  		ProductsForSaleFragment fragment = new ProductsForSaleFragment();
			    	  		thisActivity.fragmentStack.push(fragment);
			    	  		AndroidResourceFactory.setNewFragment(thisActivity, thisActivity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			    		}
			    		else{
			    			Intent homeIntent = new Intent(thisActivity, MainActivity.class);
			    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_ITEMS_FOR_SALE);
			    			homeIntent.putExtras(bundle);
			    			thisActivity.startActivity(homeIntent);
			    		}
			       }
			   })
			   .setPositiveButton("Items Sold", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
			    		if(thisActivity instanceof MainActivity ){
			    	  		ProductsSoldFragment fragment = new ProductsSoldFragment();
			    	  		thisActivity.fragmentStack.push(fragment);
			    	  		AndroidResourceFactory.setNewFragment(thisActivity, thisActivity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			    		}
			    		else{
			    			Intent homeIntent = new Intent(thisActivity, MainActivity.class);
			    			bundle.putInt(ConstantClass.MAINACTIVITY_FRAGMENT_KEY, ConstantClass.MAINACTIVITY_FRAGMENT_ITEMS_SOLD);
			    			homeIntent.putExtras(bundle);
			    			thisActivity.startActivity(homeIntent);
			    		}
			       }
			   });
    		myItemsDialog.create().show();
    		
    		break;
    	case 4:
    		//Settings (new Activity)
    		bundle.putInt(ConstantClass.USER_GUID_KEY, this.activeUser.getGuid());
    		Intent settingsIntent = new Intent(this, SettingsActivity.class);
    		settingsIntent.putExtras(bundle);
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
    		SharedPreferences preferences = getSharedPreferences(ConstantClass.USER_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
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
    	case 7:
    		//Admin Menu
    			//Send User ID
    		bundle.putInt(ConstantClass.USER_GUID_KEY, this.activeUser.getGuid());
    		Intent adminIntent = new Intent(this, AdminActivity.class);
    		adminIntent.putExtras(bundle);
    		this.startActivity(adminIntent);
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
				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			}		
	}

    
    //Cart Button Listener Abstract
    public void cartButtonListner(MenuItem menuItem){
        
    	if(this.activeUser == null){
    		Toast.makeText(this, R.string.cart_login_msg , Toast.LENGTH_SHORT).show();
    	}
    	else{
    		this.doHttpCart(this.activeUser);
    	}
    }
    
    private void doHttpCart(User activeUser) {
    	
    	pd = new ProgressDialog(this);
		pd.setMessage(getResources().getString(R.string.cart_pd_text));
		pd.show();
		
    	Bundle params = new Bundle();
		params.putString("url", Server.Cart.GET+activeUser.getGuid());
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					JSONArray cartList = json.getJSONArray("cart");
					
					if(cartList.length() == 0){
				    	LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
				        thisActivity.popUp = new PopupWindow(inflater.inflate(R.layout.popup_cart_empty, null, false),LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
				        thisActivity.popUp.showAtLocation(thisActivity.findViewById(R.id.content_frame), Gravity.CENTER, 0, 0);
					}
					else{
						LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
						View view = inflater.inflate(R.layout.popup_cart_list, null, false);
				        thisActivity.popUp = new PopupWindow(view,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
				        thisActivity.popUp.showAtLocation(thisActivity.findViewById(R.id.content_frame), Gravity.CENTER, 0, 0);
				        
				        //Generate Products Array
				        Product[] products = Product.getaProductArrayFromJSON(cartList);
				        
				        double totalPrice = 0;
				        for(Product e: products){
				        	totalPrice += e.getPrice();
				        }
				        TextView subTotal = (TextView) view.findViewById(R.id.cartTitle);
				        subTotal.setText("Subtotal = $"+totalPrice);
				        
				        
				        
					}
					
				} catch (JSONException e) {
					Toast.makeText(thisActivity, "Bad JSON parsing...",
							Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(thisActivity, R.string.cart_error_msg, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	}

	//Cart Listener
    public void cartDismissListener(View view){
    	this.popUp.dismiss();
    }
    
    public void cartContinueShoppingListener(View view){
    	this.popUp.dismiss();
    }
}

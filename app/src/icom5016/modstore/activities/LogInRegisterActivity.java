package icom5016.modstore.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


public class LogInRegisterActivity extends FragmentInterfaceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ActionBar ActionBarVar = this.getActionBar();
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		this.setContentView(R.layout.activity_lr_pager);
		
		ViewPager pager = (ViewPager) this.findViewById(R.id.lr_pager);
		//pager.setAdapter(new  );
		

		
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

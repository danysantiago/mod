package icom5016.modstore.activities;

import icom5016.modstore.fragments.CreditCardsFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends FragmentInterfaceActivity {
	private FragmentTabHost mTabHost;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.fragment_settings);
	        mTabHost = (FragmentTabHost)findViewById(R.id.settingsTabHost);
	        mTabHost.setup(this, getSupportFragmentManager(), R.id.settingsTabContent);

	        mTabHost.addTab(mTabHost.newTabSpec("payment").setIndicator("Payment"),
	        		CreditCardsFragment.class, null);
			
			//Load Search Fragment
			/*if(savedInstanceState == null){
				this.fragmentStack.push(new SettingsFragment());
				AndroidResourceFactory.setNewFragment(this, this.fragmentStack.peek(), this.getContentFragmentId());
			}*/
			
			
			
		}

		@Override
		public void cartButtonListner(MenuItem menuItem) {
			// No CART
		};
				
}

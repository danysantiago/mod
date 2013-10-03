package icom5016.modstore.activities;

import icom5016.modstore.fragments.CreditCardsFragment;
import icom5016.modstore.uielements.ListPagerAdapter;
import icom5016.modstore.uielements.TabPagerFactory;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class SettingsActivity extends FragmentActivity implements OnTabChangeListener, OnPageChangeListener{
	
	private TabHost mTabHost;
	private ViewPager pager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tabpager_layout);
		
		//ActionBar variable
		final ActionBar ActionBarVar = this.getActionBar();
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		//Set title
		ActionBarVar.setTitle(R.string.app_name);
		
		
		//
		this.initTabHost();
		
		
		//Load ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.tabpager_viewpager);
		pager.setAdapter(new ListPagerAdapter(this.getSupportFragmentManager(), this.getFragmentList()) );
        pager.setOnPageChangeListener(SettingsActivity.this);
        

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

				/*Generates Tab List */
	private List<Fragment> getFragmentList(){
		List<Fragment> result = new ArrayList<Fragment>();
		result.add(new CreditCardsFragment());
		return result;
	}
	
	private void initTabHost(){
		mTabHost = (TabHost) findViewById(R.id.tabpager_tabhost);
        mTabHost.setup();

        // TODO OMAR: Put your taps here
        SettingsActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("cc").setIndicator("Payment"));

        mTabHost.setOnTabChangedListener(this);
    }

	// Method to add a TabHost
    private static void AddTab(SettingsActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new TabPagerFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.pager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    // Manages the Tab changes, synchronizing it with Pages
 	@Override
 	public void onTabChanged(String tabId) {
 		int pos = this.mTabHost.getCurrentTab();
         this.pager.setCurrentItem(pos);
 	}

 	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// NoOp
	}

	@Override
	public void onPageSelected(int arg0) {
		// NoOp
	}
				
}

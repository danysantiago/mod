package icom5016.modstore.uielements;

import icom5016.modstore.fragments.LogInFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LogInRegisterAdapter extends FragmentPagerAdapter {

	private final static int SIZE_OF_PAGER = 2;
	
	public LogInRegisterAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int num) {
		if(num == 0){
			return new LogInFragment();
		}else{
			return new RegisterFragment();
		}
	}

	@Override
	public int getCount() {
		return SIZE_OF_PAGER;
	}

}

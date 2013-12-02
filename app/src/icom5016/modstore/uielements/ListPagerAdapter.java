package icom5016.modstore.uielements;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public ListPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int num) {
		return this.fragments.get(num);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}

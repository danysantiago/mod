package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.fragments.CategoryListFragment;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryListingListener implements OnItemClickListener {

	private MainActivity ma;
	
	public CategoryListingListener(MainActivity ma) {
		this.ma = ma;
	}



	@Override
	public void onItemClick(AdapterView<?> listView, View arg1, int pos, long arg3) {
		Category category = (Category) listView.getAdapter().getItem(pos);
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, category.getId());
		CategoryListFragment clf = new CategoryListFragment();
		clf.setArguments(bundle);
		this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), clf);
	}

}

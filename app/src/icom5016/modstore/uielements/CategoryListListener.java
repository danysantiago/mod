package icom5016.modstore.uielements;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.fragments.CategoryListFragment;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryListListener implements OnItemClickListener {

	
	private MainInterfaceActivity activity;
	
	public CategoryListListener(MainInterfaceActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Category category = (Category) listView.getAdapter().getItem(pos);
		CategoryListFragment clf = new CategoryListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, category.getId());
		clf.setArguments(bundle);
		this.activity.fragmentStack.push(clf);
		AndroidResourceFactory.setNewFragment(activity, this.activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
	}

}

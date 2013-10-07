package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ProductListListener implements OnItemSelectedListener {

private MainInterfaceActivity activity;
	
	public ProductListListener(MainInterfaceActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemSelected(AdapterView<?> listView, View view, int pos, long arg3) {
		Category category = (Category) listView.getAdapter().getItem(pos);
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_LIST_CATEGORY_KEY, category.getId());
		ProductListFragment plf = new ProductListFragment();
		plf.setArguments(bundle);
		this.activity.fragmentStack.push(plf);
		AndroidResourceFactory.setNewFragment(this.activity, this.activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}

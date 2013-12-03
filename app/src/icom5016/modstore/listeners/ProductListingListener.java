package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListingListener implements OnItemClickListener {

	private MainActivity activity;
	
	public ProductListingListener(MainActivity activity) {
		super();
		this.activity = activity;
	}



	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Product product = (Product) listView.getAdapter().getItem(pos);
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY, product.getId());
		ProductFragment pf = new ProductFragment();
		pf.setArguments(bundle);
		this.activity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
		
	}

}

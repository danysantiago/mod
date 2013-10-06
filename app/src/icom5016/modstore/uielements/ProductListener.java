package icom5016.modstore.uielements;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.AndroidResourceFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListener implements OnItemClickListener {

	private MainInterfaceActivity activity;
	
	
	
	public ProductListener(MainInterfaceActivity activity) {
		super();
		this.activity = activity;
	}



	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Product product = (Product) listView.getAdapter().getItem(pos);
		ProductFragment pf = ProductFragment.getInstance(product);
		this.activity.fragmentStack.push(pf);
		AndroidResourceFactory.setNewFragment(activity, this.activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
	}

}

package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.ProductViewerActivity;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.ConstantClass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class BidddingListListener implements OnItemClickListener {

	private MainInterfaceActivity mainActivity;

	public BidddingListListener(MainInterfaceActivity activity ){
		this.mainActivity = activity;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listViewAdapter, View view, int pos, long arg3) {
		Product product = (Product) listViewAdapter.getAdapter().getItem(pos);
		Intent productActivity = new Intent(mainActivity, ProductViewerActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY, product.getId());
		productActivity.putExtras(bundle);
		this.mainActivity.startActivity(productActivity);
		
	}

}

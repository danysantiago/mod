package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.ConstantClass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class BidSellListListener implements OnItemClickListener {

	private MainInterfaceActivity mainActivity;
	private int notificationsFlag;

	public BidSellListListener(MainInterfaceActivity activity, int notificationsFlag ){
		this.mainActivity = activity;
		this.notificationsFlag = notificationsFlag;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listViewAdapter, View view, int pos, long arg3) {
		Product product = (Product) listViewAdapter.getAdapter().getItem(pos);
		Intent productFragment = new Intent(mainActivity, ProductFragment.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY, product.getId());
		bundle.putInt(ConstantClass.PRODUCT_NOTIFICATION_KEY, this.notificationsFlag);
		productFragment.putExtras(bundle);
		this.mainActivity.startActivity(productFragment);
		
	}

}

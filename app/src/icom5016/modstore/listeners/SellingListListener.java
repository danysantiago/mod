package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.ProductSellEditActivity;
import icom5016.modstore.activities.ProductViewerActivity;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.ConstantClass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SellingListListener implements OnItemClickListener {

	private MainInterfaceActivity mainActivity;
	private String type;

	public SellingListListener(MainInterfaceActivity activity, String type ){
		this.mainActivity = activity;
		this.type = type;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listViewAdapter, View view, int pos, long arg3) {
		Product product = (Product) listViewAdapter.getAdapter().getItem(pos);
		
		if (type == ConstantClass.SELLING_ACTIVE) {
			Intent sellingActivity = new Intent(mainActivity, ProductSellEditActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putSerializable("productObj", product);
			sellingActivity.putExtras(bundle);
			
			this.mainActivity.startActivity(sellingActivity);
		} else if (type == ConstantClass.SELLING_SOLD) {
			// View que tiene información del producto y a quien enviarselo, tracking y todo eso.
		} else if (type == ConstantClass.SELLING_NOTSOLD) {
			Intent sellingActivity = new Intent(mainActivity, ProductViewerActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putInt(ConstantClass.PRODUCT_KEY, product.getId());
			bundle.putString(ConstantClass.SELLING_TYPE_VIEW_KEY, this.type);
			sellingActivity.putExtras(bundle);
			this.mainActivity.startActivity(sellingActivity);
		}
	}

}

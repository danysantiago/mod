package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.BidsFragment;
import icom5016.modstore.fragments.OrderDetailsFragment;
import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.fragments.SellProductFragment;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.ProductSelling;
import icom5016.modstore.resources.ConstantClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SellingListListener implements OnItemClickListener {

	private MainActivity mainActivity;
	private String type;
	private Dialog dialog;
	private int sellerId;

	public SellingListListener(MainActivity activity, String type, int sellerId ){
		this.mainActivity = activity;
		this.type = type;
		this.sellerId = sellerId;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listViewAdapter, View view, int pos, long arg3) {
		final ProductSelling product = (ProductSelling) listViewAdapter.getAdapter().getItem(pos);
		LayoutInflater inflater = this.mainActivity.getLayoutInflater();
        View sfView = inflater.inflate(R.layout.dialog_selling_list, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
        builder.setTitle(R.string.selling_dialog_title);
        builder.setView(sfView);
        
        ListView lv = (ListView) sfView.findViewById(R.id.lvSellingListDialog);
        List<String> list;
        
        if(type.equals(ConstantClass.SELLING_ACTIVE)){
        	list = new ArrayList<String>(Arrays.asList(new String[]{"Listing", "Edit"}));
        	if(product.getStartingBidPrice() != -1.0){
        		list.add("Bids");
			}
        }
        	
        else if(type.equals(ConstantClass.SELLING_SOLD)){
        	list = new ArrayList<String>(Arrays.asList(new String[]{"Listing", "Order Details"}));
        	if(product.getStartingBidPrice() != -1.0){
        		list.add("Bids");
			}
        }
        else if(type.equals(ConstantClass.SELLING_NOTSOLD)){
        	list = new ArrayList<String>(Arrays.asList(new String[]{"Listing"}));
        }
        else
        	list = new ArrayList<String>(Arrays.asList(new String[]{"Listing"}));
        
        lv.setAdapter(new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, list));
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				switch (arg2) {
				case 0:
					loadListingFragment(product.getId());
					break;
				case 1:
					 if(type.equals(ConstantClass.SELLING_ACTIVE)){
				        	loadEditFragment(product, type);
				        }
				        else if(type.equals(ConstantClass.SELLING_SOLD)){
				        	loadOrderDetails(product);
				        }
					break;
				case 2:
					loadBidsFragment(product.getId());
					break;
				}
				dialog.dismiss();
			}
		});
        
        this.dialog = builder.create();
        this.dialog.show();
		
	}
	
	protected void loadOrderDetails(ProductSelling product) {
		Bundle bnd = new Bundle();
		bnd.putSerializable(ConstantClass.PRODUCT_KEY, product);
		OrderDetailsFragment odf = new OrderDetailsFragment();
		odf.setArguments(bnd);
		mainActivity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), odf);
	}

	protected void loadEditFragment(Product product, String type) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ConstantClass.PRODUCT_SELL_PROD_KEY, product);
		SellProductFragment spf = new SellProductFragment();
		spf.setArguments(bundle);
		mainActivity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), spf);
	}

	private void loadBidsFragment(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY,id);
		bundle.putInt(ConstantClass.SELLER_KEY, sellerId);
		BidsFragment bf = new BidsFragment();
		bf.setArguments(bundle);
		mainActivity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), bf);
	}

	private void loadListingFragment(int id){
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY,id);
		ProductFragment pf = new ProductFragment();
		pf.setArguments(bundle);
		mainActivity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
	}
}

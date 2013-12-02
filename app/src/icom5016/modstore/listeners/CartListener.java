package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.ProductFragment;
import icom5016.modstore.models.ProductCart;
import icom5016.modstore.resources.ConstantClass;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CartListener implements OnItemClickListener {

	private MainActivity ma;
	private Dialog dialog;
	private int userId;
	private AlertDialog quantityDialog;
	
	
	public CartListener(MainActivity activity, int userId) {
		ma = activity;
		this.userId = userId;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final ProductCart product = (ProductCart) arg0.getAdapter().getItem(arg2);
		
		AlertDialog.Builder builder = new Builder(ma);
		ListView dialogListView = new ListView(ma);
		dialogListView.setAdapter(new ArrayAdapter<String>(ma, android.R.layout.simple_list_item_1, ConstantClass.CART_DIALOG_OPTIONS));
		dialogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					viewProductListing(product.getId());
					break;
				case 1:
					changeProductQuantity(product.getId(), product.getQuantity());
					break;
				case 2:
					removeItemFromCart(product.getId());
					break;
				}
			}
		});
		builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setTitle(R.string.cart_dialog_title);
		builder.setView(dialogListView);
		this.dialog = builder.create();
		this.dialog.show();
	}

	private void changeProductQuantity(final int id, int quantity) {
		
		Builder builder = new Builder(ma);
		builder.setTitle(R.string.cart_change_quantity_dialog_title);
		LayoutInflater li = ma.getLayoutInflater();
		View view = li.inflate(R.layout.dialog_cart_quantity_change, null);
		EditText et = (EditText) view.findViewById(R.id.cartChangeQuantityEt);
		et.setText(""+quantity);
		final EditText etEdited = et;
		builder.setView(view);
		builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doHttpChangeProductQuantity(id, Integer.parseInt(etEdited.getText().toString()));
			}
		});
		this.dialog.dismiss();
		this.quantityDialog = builder.create();
		this.quantityDialog.show();
	}

	private void doHttpChangeProductQuantity(int productId, int quantity) {
		Toast.makeText(ma, "Wait for Phase 3 ^_^", Toast.LENGTH_SHORT).show();
		this.dialog.dismiss();
		
	}

	private void viewProductListing(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.PRODUCT_KEY, id);
		ProductFragment pf = new ProductFragment();
		pf.setArguments(bundle);
		this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
		this.dialog.dismiss();
	}
	
	
	

	private void removeItemFromCart(int productId) {
		doHttpRemoveFromCart(productId, this.userId);
	}

	private void doHttpRemoveFromCart(int productId, int userId2) {
		Toast.makeText(ma, "Wait for Phase 3 ^_^", Toast.LENGTH_SHORT).show();
		this.dialog.dismiss();
	}

}

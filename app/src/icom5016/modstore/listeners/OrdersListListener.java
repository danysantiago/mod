package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.fragments.MyOrderDetailsListFragment;
import icom5016.modstore.models.Orders;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OrdersListListener implements OnItemClickListener {

	private MainActivity mainActivity;
	
	public OrdersListListener(MainActivity activity){
		this.mainActivity = activity;
	}
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Orders order = (Orders) listView.getAdapter().getItem(pos);
		
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.ORDERID_KEY, order.getOrderId());
		MyOrderDetailsListFragment odlf = new MyOrderDetailsListFragment();
		odlf.setArguments(bundle);
		this.mainActivity.loadFragmentInMainActivityStack(MainActivity.getContainerId(), odlf);
		
	}

}

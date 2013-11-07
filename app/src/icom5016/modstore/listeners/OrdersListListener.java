package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.MyOrderDetailActivity;
import icom5016.modstore.models.Orders;
import icom5016.modstore.resources.ConstantClass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OrdersListListener implements OnItemClickListener {

	private MainInterfaceActivity mainActivity;
	
	public OrdersListListener(MainInterfaceActivity activity){
		this.mainActivity = activity;
	}
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Orders order = (Orders) listView.getAdapter().getItem(pos);
		Intent orderDetails = new Intent(mainActivity, MyOrderDetailActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantClass.ORDERID_KEY, order.getOrderId());
		orderDetails.putExtras(bundle);
		mainActivity.startActivity(orderDetails);
		
		
	}

}

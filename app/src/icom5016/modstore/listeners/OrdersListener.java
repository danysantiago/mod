package icom5016.modstore.listeners;

import icom5016.modstore.activities.MainInterfaceActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OrdersListener implements OnItemClickListener {

	private MainInterfaceActivity mainActivity;
	
	public OrdersListener(MainInterfaceActivity activity){
		this.mainActivity = activity;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}

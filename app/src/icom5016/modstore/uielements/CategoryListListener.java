package icom5016.modstore.uielements;

import icom5016.modstore.activities.MainInterfaceActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CategoryListListener implements OnItemClickListener {

	
	private MainInterfaceActivity activity;
	
	public CategoryListListener(MainInterfaceActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(this.activity, "Yay Category Click", Toast.LENGTH_SHORT).show();

	}

}

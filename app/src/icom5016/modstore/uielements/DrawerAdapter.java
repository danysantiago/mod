package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.User;
import android.content.Context;
import android.widget.ArrayAdapter;

public class DrawerAdapter extends ArrayAdapter<String> {

	

	private  String[] listOfRows;
	private  User user;
	
	public DrawerAdapter(Context context, User user){
		super(context, R.layout.listview_drawer_row);
		this.user = user;
		
		//Different List Base on User Status
		if(this.user == null){
			this.listOfRows = ConstantClass.DRAWER_GUEST_LIST;
		}
		else{
			
			this.listOfRows = ConstantClass.DRAWER_USER_LIST;
			
		}
		super.addAll(this.listOfRows);
	}
	
	public void setUser(User user){
		this.user = user;
		if(this.user == null){
			this.listOfRows = ConstantClass.DRAWER_GUEST_LIST;
		}
		else{
			this.listOfRows = ConstantClass.DRAWER_USER_LIST;
		}
	}

}

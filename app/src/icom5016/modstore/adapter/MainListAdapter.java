package icom5016.modstore.adapter;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<String> {

	

	private  String[] listOfRows;
	private  User user;
	
	public MainListAdapter(Context context, User user){
		super(context, R.layout.listview_mainlist_row);
		this.user = user;
		
		//Different List Base on User Status
		if(this.user == null){
			this.listOfRows = ConstantClass.DRAWER_GUEST_LIST;
		}
		else{
			this.listOfRows = ConstantClass.DRAWER_USER_LIST;
			if(this.user.isAdmin()){
				this.listOfRows = ConstantClass.DRAWER_ADMIN_LIST;
			}
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
			if(this.user.isAdmin()){
				this.listOfRows = ConstantClass.DRAWER_ADMIN_LIST;
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_mainlist_row, parent, false);
		
		TextView tv = (TextView) row.findViewById(R.id.lvMainList);
		tv.setText(R.string.app_name);
		
		return row;
	}

	

	
}

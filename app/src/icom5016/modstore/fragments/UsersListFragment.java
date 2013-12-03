package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.models.ProductSearching;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UsersListFragment extends Fragment {
	
	private ListView usersListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		usersListView = new ListView(getActivity());
		
		usersListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return usersListView;
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getHTTPUsers();
	}

	private void getHTTPUsers() {
		//Perform http request
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.User.GETALL); //Get All Users
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				UsersArrayAdapter adapter;
				try {
					adapter = new UsersArrayAdapter(getActivity(), json.getJSONArray("result"));
					usersListView.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}

			
		});
		request.execute();
		
	}
	
	private class UsersArrayAdapter extends ArrayAdapter<User> {

		public UsersArrayAdapter(Context context, JSONArray jsonArr) {
			super(context, R.layout.listview_product_listing);
			
			for(int i = 0; i < jsonArr.length(); i++) {
				try {
					this.add(new User(jsonArr.getJSONObject(i)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			User user = (User) this.getItem(position);

			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
			
			row = inflater.inflate(R.layout.listview_user_row, parent, false);
			
			//Layout Vars
			TextView username = (TextView) row.findViewById(R.id.usernameTextView);
			TextView email = (TextView) row.findViewById(R.id.emailTextView);
			TextView firstName = (TextView) row.findViewById(R.id.firstNameTextView);
			TextView lastName = (TextView) row.findViewById(R.id.lastNameTextView);
			
			username.setText(user.getUsername());
			email.setText(user.getEmail());
			firstName.setText(user.getFirstName());
			lastName.setText(user.getLastName());
			
			if(user.isAdmin()) {
				username.setTextColor(getResources().getColor(R.color.dark_green));
			}

			return row;
		}
		
	}

}

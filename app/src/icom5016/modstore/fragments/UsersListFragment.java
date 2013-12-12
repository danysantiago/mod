package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UsersListFragment extends Fragment {
	
	private static final String[] userOptions = {"Edit", "Delete"};
	
	private MainActivity ma;
	private LinearLayout container;
	private ListView usersListView;
	private ProgressBar pd;
	private Button addUserBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.ma = (MainActivity) this.getActivity();
		
		View view = inflater.inflate(R.layout.fragment_users_list, container, false);
		
		this.container = (LinearLayout) view.findViewById(R.id.container);
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		usersListView = (ListView) view.findViewById(R.id.listView);
		
		addUserBtn = (Button) view.findViewById(R.id.AddUserButton);
		addUserBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createUser();
				
			}
		});
		
		usersListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
				final User user = (User) usersListView.getAdapter().getItem(pos);
				if(!user.isDelete()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    builder.setTitle("User Options");
				    builder.setItems(userOptions, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								showEditDialog(user);
								break;
							case 1:
								showDeleteDialog(user);
								break;
							}
							
						}
	
					});
					builder.show();
				}
			}
		}); 
		
		return view;
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		container.setVisibility(View.GONE);
		pd.setVisibility(View.VISIBLE);
		
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

			@Override
			public void onDone() {
				container.setVisibility(View.VISIBLE);
				pd.setVisibility(View.GONE);
			}
			
		});
		request.execute();
		
	}
	
	private void httpDeleteUser(int guid) {
		Bundle params = new Bundle();
		params.putString("method", "DELETE");
		params.putString("url", Server.User.DELETE + "?userId=" + Integer.toString(guid)); //Get All Users
		
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDone() {
				getHTTPUsers();
			}
			
		});
		request.execute();
	}
	
	private void showDeleteDialog(final User user) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Delete User");
		builder.setMessage("Are you sure you wish to delete user?\n\n" + user.getUsername() + "\n" + user.getEmail() + "\n\n This process cannot be undone.");
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				httpDeleteUser(user.getGuid());
			}
			
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();
		
	}
	
	private void showEditDialog(User user) {
		LinearLayout ll = new LinearLayout(getActivity());
		getActivity().getLayoutInflater().inflate(R.layout.dialog_user_info, ll);
		
		EditText username = (EditText) ll.findViewById(R.id.usernameEditText);
		EditText email = (EditText) ll.findViewById(R.id.emailEditText);
		EditText firstname = (EditText) ll.findViewById(R.id.firstnameEditText);
		EditText lastname = (EditText) ll.findViewById(R.id.lastnameEditText);
		CheckBox isAdmin = (CheckBox) ll.findViewById(R.id.adminCheckBox);
		
		username.setText(user.getUsername());
		email.setText(user.getEmail());
		firstname.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		isAdmin.setChecked(user.isAdmin());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Modify User");
		builder.setView(ll);
		builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
			}
			
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}
	
	private void createUser() {
			LinearLayout ll = new LinearLayout(getActivity());
			getActivity().getLayoutInflater().inflate(R.layout.dialog_user_info, ll);
			
			EditText username = (EditText) ll.findViewById(R.id.usernameEditText);
			EditText email = (EditText) ll.findViewById(R.id.emailEditText);
			EditText firstname = (EditText) ll.findViewById(R.id.firstnameEditText);
			EditText lastname = (EditText) ll.findViewById(R.id.lastnameEditText);
			EditText password = (EditText) ll.findViewById(R.id.passwordEditText);
			TextView passwordTV = (TextView) ll.findViewById(R.id.passwordTextView);
			Button passwordBtn = (Button) ll.findViewById(R.id.passwordButton);
			CheckBox isAdmin = (CheckBox) ll.findViewById(R.id.adminCheckBox);
			
			password.setVisibility(View.VISIBLE);
			passwordTV.setVisibility(View.VISIBLE);
			passwordBtn.setVisibility(View.GONE);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Create User");
			builder.setView(ll);
			builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
				}
				
			});
			builder.setNegativeButton("Cancel", null);
			builder.show();
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
			
			if (user.isDelete()) {
				username.setPaintFlags(username.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

			return row;
		}
		
	}

}

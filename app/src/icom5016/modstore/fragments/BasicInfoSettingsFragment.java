package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.SettingListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.Server;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.models.SettingRow;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BasicInfoSettingsFragment extends Fragment {
	ListView lstSettingList;
	
	private User u;
	
	public BasicInfoSettingsFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_basicsettings, container, false);
		
		lstSettingList = (ListView) view.findViewById(R.id.lstSettingList);
		
		loadSettingList();
		
        return view;
    }
	
	private void loadSettingList() {
		ArrayList<SettingRow> settingList = new ArrayList<SettingRow>();
		
		u = DataFetchFactory.getUserFromSPref(getActivity());
		
		settingList.add(new SettingRow(u.getUsername(), "Username"));
		settingList.add(new SettingRow(u.getFirstName(), "First Name"));
		settingList.add(new SettingRow(u.getMiddleName(), "Middle Name"));
		settingList.add(new SettingRow(u.getLastName(), "Last Name"));
		settingList.add(new SettingRow(u.getEmail(), "Email"));
		settingList.add(new SettingRow("Change your password...", null));
		
		SettingListAdapter adapter = new SettingListAdapter(getActivity(), R.layout.listview_setting_row, settingList);
		lstSettingList.setAdapter(adapter);
		lstSettingList.setOnItemClickListener(new listOnClick());
	}

	private class listOnClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
			if (pos == 5) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				
				final View v = inflater.inflate(R.layout.dialog_changepw, null);
				builder.setView(v);
				builder.setTitle("Change your Password");
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Cancel...
	               }
				});
				
				builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText pw1 = (EditText) v.findViewById(R.id.txtChangePW1);
	            	   EditText pw2 = (EditText) v.findViewById(R.id.txtChangePW2);
	            	   
	            	   if(!pw1.getText().toString().equals(pw2.getText().toString())) {
	            		   Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
	            		   return;
	            	   }
	            	   
	            	   try {
						changePWHttp(pw1.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	               }
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				// Username cant be edited.
				if (pos == 0) {
					return;
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				SettingRow settingRow = (SettingRow) lstSettingList.getAdapter().getItem(pos);
				final EditText editText = new EditText(getActivity());
				final int dialogPos = pos;
				
				editText.setText(settingRow.title);

				builder.setView(editText);
				builder.setTitle("Change your " + settingRow.value);
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Cancel...
	               }
				});
				
				builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   String firstName = null;
	            	   String middleName = null;
	            	   String lastName = null;
	            	   String email = null;
	            	   
	            	   //Change... PUT to NodeJS
	            	   if (dialogPos == 1)
	            		   firstName = editText.getText().toString();
	            	   else if (dialogPos == 2)
	            		   middleName = editText.getText().toString();
	            	   else if (dialogPos == 3)
	            		   lastName = editText.getText().toString();
	            	   else if (dialogPos == 4)
	            		   email = editText.getText().toString();
	            	   else
	            		   return;
	            	   
	            	   try {
	            		   changeUserHttp(firstName, middleName, lastName, email);
	            	   } catch (JSONException e) {
							// TODO Auto-generated catch block
            		   		e.printStackTrace();
	            	   }
	               }
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
	
	private void changePWHttp(String password) throws JSONException {
		
		JSONObject json = new JSONObject();
		json.put("password", password);
		json.put("userId", u.getGuid());
		
		Bundle params = new Bundle();
		params.putString("method", "POST");
		params.putString("url", Server.User.UPDATE_PASSWORD);
		
		
		HttpRequest request = new HttpRequest(params, json, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Failed to update password.", Toast.LENGTH_SHORT).show();
			}
		});
		request.execute();
	}
	
	private void changeUserHttp(String firstName, String middleName, String lastName, String email) throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("userId", u.getGuid());
		
		final User newUser = u;
		
		if (firstName != null) {
			json.put("firstName", firstName);
			newUser.setFirstName(firstName);
		} if (middleName != null) {
			json.put("middleName", middleName);
			newUser.setMiddleName(middleName);
		} if (lastName != null) {
			json.put("lastName", lastName);
			newUser.setLastName(lastName);
		} if (email != null) {
			json.put("email", email);
			newUser.setEmail(email);
		}
		
		Bundle params = new Bundle();
		params.putString("method", "PUT");
		params.putString("url", Server.User.UPDATE);
		
		HttpRequest request = new HttpRequest(params, json, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				// Update User on Shared Preferences.
				DataFetchFactory.setUserInSharedPreferences(newUser, getActivity());
				// Reload User Data
				loadSettingList();
				
				Toast.makeText(getActivity(), "User account updated!", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Problem updating the user account.", Toast.LENGTH_SHORT).show();
			}
		});
		request.execute();
	}
}

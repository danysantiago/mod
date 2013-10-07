package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.SettingRow;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import icom5016.modstore.uielements.SettingListAdapter;

import java.util.ArrayList;

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

public class BasicInfoSettingsFragment extends Fragment {
	ListView lstSettingList;
	
	public BasicInfoSettingsFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_basicsettings, container, false);
		lstSettingList = (ListView)view.findViewById(R.id.lstSettingList);
		ArrayList<SettingRow> settingList = new ArrayList<SettingRow>();
		User u = DataFetchFactory.getUserInSharedPreferences(getActivity());
		
		settingList.add(new SettingRow("Username", u.getUsername()));
		settingList.add(new SettingRow("First Name", u.getFirstName()));
		settingList.add(new SettingRow("Middle Name", u.getMiddleName()));
		settingList.add(new SettingRow("Last Name", u.getLastName()));
		settingList.add(new SettingRow("Email", u.getEmail()));
		settingList.add(new SettingRow("Change your password...", null));
		
		SettingListAdapter adapter = new SettingListAdapter(getActivity(), R.layout.listview_setting_row, settingList);
		lstSettingList.setAdapter(adapter);
		lstSettingList.setOnItemClickListener(new listOnClick());
		
        return view;
    }

	private class listOnClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 5) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				
				builder.setView(inflater.inflate(R.layout.dialog_changepw, null));
				builder.setTitle("Change your Password");
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Cancel...
	               }
				});
				
				builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Change... PUT to NodeJS
	               }
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				SettingRow settingRow = (SettingRow)lstSettingList.getAdapter().getItem(arg2);
				EditText editText = new EditText(getActivity());
				
				editText.setText(settingRow.value);

				builder.setView(editText);
				builder.setTitle("Change your " + settingRow.title);
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Cancel...
	               }
				});
				
				builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Change... PUT to NodeJS
	               }
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
}

package icom5016.modstore.dialog;

import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.AddressesFragment;
import icom5016.modstore.models.Address;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AddressDialog extends DialogFragment {
	Spinner cboCountries;
	
	EditText txtLine1;
	EditText txtLine2;
	EditText txtCity;
	EditText txtState;
	EditText txtZipcode;
	
	CheckBox chkDefault;
	
	public Address address;

	private User u;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		u = DataFetchFactory.getUserFromSPref(getActivity());
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View v = inflater.inflate(R.layout.dialog_address, null);
	    
	    String positiveButton = (address == null) ? "Add" : "Update";
	    String title = (address == null) ? "Add an Address" : "View Address";
	    
	    builder.setView(v)
	    	.setTitle(title)
	    	.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int id) {
				   String addr1 = ((EditText) v.findViewById(R.id.txtAddrLine1)).getText().toString();
				   String addr2 = ((EditText) v.findViewById(R.id.txtAddrLine2)).getText().toString();
				   String city = ((EditText) v.findViewById(R.id.txtAddrCity)).getText().toString();
				   String state = ((EditText) v.findViewById(R.id.txtAddrState)).getText().toString();
				   String zipcode = ((EditText) v.findViewById(R.id.txtAddrZipcode)).getText().toString();
				   String country = ((Spinner) v.findViewById(R.id.cboAddrCountry)).getSelectedItem().toString();
				   String primary = ((CheckBox) v.findViewById(R.id.chkAddrDefault)).isChecked() ? "1" : "0";
				   
				   JSONObject json = new JSONObject();
				   try {
					json.put("user_id", u.getGuid());
					json.put("line1", addr1);
					json.put("line2", addr2);
					json.put("city", city);
					json.put("state", state);
					json.put("zipcode", zipcode);
					json.put("country", country);
					json.put("is_primary", primary);
					
					if(address == null) {
						AddressesFragment.leakFragment.insertAddrHttp(json);
					} else {
						json.put("address_id", address.getId());
						AddressesFragment.leakFragment.updateAddrHttp(json);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }

	    	})
	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           });

	    cboCountries = (Spinner)v.findViewById(R.id.cboAddrCountry);
	    txtLine1 = (EditText)v.findViewById(R.id.txtAddrLine1);
	    txtLine2 = (EditText)v.findViewById(R.id.txtAddrLine2);
	    txtCity = (EditText)v.findViewById(R.id.txtAddrCity);
	    txtState = (EditText)v.findViewById(R.id.txtAddrState);
	    txtZipcode = (EditText)v.findViewById(R.id.txtAddrZipcode);
	    chkDefault = (CheckBox)v.findViewById(R.id.chkAddrDefault);
	    
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataFetchFactory.getCountries());
	    cboCountries.setAdapter(adapter);

	    loadAddress();
	    
	    return builder.create();
	}
	
	private void loadAddress() {
	    if (address != null) {
	    	SpinnerAdapter tempAdapter = cboCountries.getAdapter();
	    	int pos = -1;
	    	
	    	txtLine1.setText(address.getLine1());
	    	txtLine2.setText(address.getLine2());
	    	txtCity.setText(address.getCity());
	    	txtState.setText(address.getState());
	    	txtZipcode.setText(address.getZipcode());
	    	chkDefault.setChecked(address.isDefault());

	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		if (tempAdapter.getItem(i).equals(address.getCountry())) {
	    			pos = i;
	    		}
	    	}

	    	if (pos != -1)
	    		cboCountries.setSelection(pos);
	    }
	}
}
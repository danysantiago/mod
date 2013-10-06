package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Address;
import icom5016.modstore.resources.DataFetchFactory;
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_address, null);
	    
	    String positiveButton = (address == null) ? "Add" : "Update";
	    String title = (address == null) ? "Add an Address" : "View Address";
	    
	    builder.setView(v)
	    	.setTitle(title)
	    	.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int id) {
			       // PUT/POST to Server.
			   }
	    	})
	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   AddressDialog.this.getDialog().cancel();
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
	    	
	    	txtLine1.setText(address.line1);
	    	txtLine2.setText(address.line2);
	    	txtCity.setText(address.city);
	    	txtState.setText(address.state);
	    	txtZipcode.setText(address.zipcode);
	    	chkDefault.setChecked(address.isDefault);

	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		if (tempAdapter.getItem(i).equals(address.country)) {
	    			pos = i;
	    		}
	    	}

	    	if (pos != -1)
	    		cboCountries.setSelection(pos);
	    }
	}
}
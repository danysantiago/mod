package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Address;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.resources.DataFetchFactory;

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

public class CreditCardDialog extends DialogFragment {
	Spinner cboTypes;
	Spinner cboYears;
	Spinner cboAddresses;
	
	EditText txtFullname;
	EditText txtNumber;
	EditText txtSecurityCode;
	EditText txtExpireMonth;
	
	CheckBox chkDefault;
	
	public CreditCard creditCard;
	public JSONObject addressesJson;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_creditcard, null);
	    
	    String positiveButton = (creditCard == null) ? "Add" : "Update";
	    String title = (creditCard == null) ? "Add a Credit Card" : "View Credit Card";
	    
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
            	   CreditCardDialog.this.getDialog().cancel();
               }
           });
	    
	    cboTypes = (Spinner)v.findViewById(R.id.cboType);
	    cboYears = (Spinner)v.findViewById(R.id.cboCCExpireYear);
	    cboAddresses = (Spinner)v.findViewById(R.id.cboCCAddress);
	    txtFullname = (EditText)v.findViewById(R.id.txtCCFullName);
	    txtNumber = (EditText)v.findViewById(R.id.txtCCNumber);
	    txtSecurityCode = (EditText)v.findViewById(R.id.txtCCSecurityCode);
	    txtExpireMonth = (EditText)v.findViewById(R.id.txtCCExpireMonth);
	    chkDefault = (CheckBox)v.findViewById(R.id.chkCCDefault);
	    
	    ImagesAdapter adapter = new ImagesAdapter(getActivity(), R.layout.listview_image_row, DataFetchFactory.getCreditCardImages());
		cboTypes.setAdapter(adapter);
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataFetchFactory.getNextYears(15));
	    cboYears.setAdapter(adapter2);
	    
	    AddressAdapter adapter3 = new AddressAdapter(getActivity(), R.layout.listview_address_row, addressesJson);
	    cboAddresses.setAdapter(adapter3);
	    
	    loadCreditCard();
	    
	    return builder.create();
	}
	
	private void loadCreditCard() {
	    if (creditCard != null) {
	    	SpinnerAdapter tempAdapter = cboYears.getAdapter();
	    	String temp[] = creditCard.getExpire().split("/");
	    	int pos = -1;
	    	
	    	txtFullname.setText(creditCard.getName());
	    	txtNumber.setText(creditCard.getNumber());
	    	txtSecurityCode.setText(creditCard.getSecurityCode());
	    	txtExpireMonth.setText(temp[0]);
	    	cboTypes.setSelection(creditCard.getType());
	    	chkDefault.setChecked(creditCard.isDefault());
	    	
	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		if (tempAdapter.getItem(i).equals(temp[1])) {
	    			pos = i;
	    		}
	    	}

	    	if (pos != -1)
	    		cboYears.setSelection(pos);
	    	
	        tempAdapter = cboAddresses.getAdapter();
	        Address addr;
	        pos = -1;
	        
	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		addr = (Address)tempAdapter.getItem(i);
	    		if (addr.getId() == creditCard.getAid()) {
	    			pos = i;
	    		}
	    	}
	    	if (pos != -1)
	    		cboAddresses.setSelection(pos);
	    }
	}
}

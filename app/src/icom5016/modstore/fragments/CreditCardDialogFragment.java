package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.resources.DataFetchFactory;
import icom5016.modstore.uielements.ImagesAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class CreditCardDialogFragment extends DialogFragment {
	Spinner cboTypes;
	Spinner cboYears;
	Spinner cboAddresses;
	
	EditText txtFullname;
	EditText txtNumber;
	EditText txtSecurityCode;
	EditText txtExpireMonth;
	
	public CreditCard creditCard;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_add_creditcard, null);
	    
	    String positiveButton = (creditCard == null) ? "Add" : "Update";
	    String title = (creditCard == null) ? "Add a Credit Card" : "View Credit Card";
	    
	    builder.setView(v)
	    	.setTitle(title)
	    	.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int id) {
			       // sign in the user ...
			   }
	    	})
	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   CreditCardDialogFragment.this.getDialog().cancel();
               }
           });
	    
	    cboTypes = (Spinner)v.findViewById(R.id.cboType);
	    cboYears = (Spinner)v.findViewById(R.id.cboCCExpireYear);
	    cboAddresses = (Spinner)v.findViewById(R.id.cboCCAddress);
	    txtFullname = (EditText)v.findViewById(R.id.txtCCFullName);
	    txtNumber = (EditText)v.findViewById(R.id.txtCCNumber);
	    txtSecurityCode = (EditText)v.findViewById(R.id.txtCCSecurityCode);
	    txtExpireMonth = (EditText)v.findViewById(R.id.txtCCExpireMonth);
	    
	    ImagesAdapter adapter = new ImagesAdapter(getActivity(), R.layout.listview_image_row, DataFetchFactory.getCreditCardImages());
		cboTypes.setAdapter(adapter);
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataFetchFactory.getNextYears(15));
	    cboYears.setAdapter(adapter2);

	    loadCreditCard();
	    
	    return builder.create();
	}
	
	private void loadCreditCard() {
	    if (creditCard != null) {
	    	SpinnerAdapter tempAdapter = cboYears.getAdapter();
	    	String temp[] = creditCard.expire.split("/");
	    	int pos = -1;
	    	
	    	txtFullname.setText(creditCard.name);
	    	txtNumber.setText(creditCard.number);
	    	txtSecurityCode.setText(creditCard.securityCode);
	    	txtExpireMonth.setText(temp[0]);
	    	cboTypes.setSelection(creditCard.type);

	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		if (tempAdapter.getItem(i).equals(temp[1])) {
	    			pos = i;
	    		}
	    	}
	    	
	    	if (pos != -1)
	    		cboYears.setSelection(pos);
	    }
	}
}

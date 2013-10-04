package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
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

public class NewCreditCardDialogFragment extends DialogFragment {
	Spinner cboTypes;
	Spinner cboYears;
	Spinner cboAddresses;
	
	EditText txtFullname;
	EditText txtNumber;
	EditText txtSecurityCode;
	EditText txtExpireMonth;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_add_creditcard, null);
	    
	    builder.setView(v)
	    	.setTitle("Add a Credit Card")
	    	.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int id) {
			       // sign in the user ...
			   }
	    	})
	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   NewCreditCardDialogFragment.this.getDialog().cancel();
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

	    return builder.create();
	}
}

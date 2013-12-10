package icom5016.modstore.dialog;

import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.AddressAdapter;
import icom5016.modstore.adapter.ImagesAdapter;
import icom5016.modstore.fragments.AddressesFragment;
import icom5016.modstore.fragments.CreditCardsFragment;
import icom5016.modstore.models.Address;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
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

@SuppressLint("SimpleDateFormat")
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
	private User u;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		u = DataFetchFactory.getUserFromSPref(getActivity());
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View v = inflater.inflate(R.layout.dialog_creditcard, null);
	    
	    String positiveButton = (creditCard == null) ? "Add" : "Update";
	    String title = (creditCard == null) ? "Add a Credit Card" : "View Credit Card";
	    
	    builder.setView(v)
	    	.setTitle(title)
	    	.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int id) {
				   String fullname = ((EditText) v.findViewById(R.id.txtCCFullName)).getText().toString();
				   String ccnumber = ((EditText) v.findViewById(R.id.txtCCNumber)).getText().toString();
				   String ccv = ((EditText) v.findViewById(R.id.txtCCSecurityCode)).getText().toString();
				   String month = ((EditText) v.findViewById(R.id.txtCCExpireMonth)).getText().toString();
				   String year = ((Spinner) v.findViewById(R.id.cboCCExpireYear)).getSelectedItem().toString();
				   String type = "" + ((Spinner) v.findViewById(R.id.cboCCExpireYear)).getSelectedItemPosition();
				   String address = "" + ((Address) ((Spinner) v.findViewById(R.id.cboCCAddress)).getSelectedItem()).getId();
				   String primary = ((CheckBox) v.findViewById(R.id.chkCCDefault)).isChecked() ? "1" : "0";

				   
				   JSONObject json = new JSONObject();
				   try {
					json.put("user_id", u.getGuid());
					json.put("address_id", address);
					json.put("name", fullname);
					json.put("type", type);
					json.put("number", ccnumber);
					json.put("security_code", ccv);
					json.put("expiration_date", year + "-" + month + "-1"  );
					json.put("is_primary", primary);
					
					if(creditCard == null) {
						CreditCardsFragment.leakFragment.insertCCHttp(json);
					} else {
						json.put("creditcard_id", creditCard.getCreditcardId());
						CreditCardsFragment.leakFragment.updateCCHttp(json);
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
	
	@SuppressLint("SimpleDateFormat")
	private void loadCreditCard() {
	    if (creditCard != null) {
	    	SpinnerAdapter tempAdapter = cboYears.getAdapter();
	    	int pos = -1;
	    	
	    	SimpleDateFormat yearF = new SimpleDateFormat("yyyy");
	    	SimpleDateFormat monthF = new SimpleDateFormat("MM");
	    	
	    	String year = yearF.format(creditCard.getExpireDate());
	    	String month = monthF.format(creditCard.getExpireDate());
	    	
	    	txtFullname.setText(creditCard.getName());
	    	txtNumber.setText(creditCard.getNumber());
	    	txtSecurityCode.setText(creditCard.getSecurityCode());
	    	txtExpireMonth.setText(month);
	    	cboTypes.setSelection(creditCard.getType());
	    	chkDefault.setChecked(creditCard.isDefault());
	    	
	    	for (int i = 0; i < tempAdapter.getCount(); i++) {
	    		if (tempAdapter.getItem(i).equals(year)) {
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
	    		if (addr.getId() == creditCard.getAddressId()) {
	    			pos = i;
	    		}
	    	}
	    	if (pos != -1) {
	    		cboAddresses.setSelection(pos);
	    	}
	    }
	}
}

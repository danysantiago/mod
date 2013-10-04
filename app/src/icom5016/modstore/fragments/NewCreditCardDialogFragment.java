package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.DataFetchFactory;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class NewCreditCardDialogFragment extends DialogFragment {
	Spinner cboTypes;
	Spinner cboYears;
	
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
	    
	    Dialog dialog = builder.create();
	    
	    Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    
	    String strYears[] = new String[15];
	    
	    for (int i = 0; i < 15; i++) {
	    	strYears[i] = String.valueOf(year + i);
	    }
	    
	    cboTypes = (Spinner)v.findViewById(R.id.cboType);
	    CreditCardTypesAdapter adapter = new CreditCardTypesAdapter(getActivity(), R.layout.listview_image_row, DataFetchFactory.getCreditCardImages());
		cboTypes.setAdapter(adapter);
		
		cboYears = (Spinner)v.findViewById(R.id.cboCCExpireYear);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strYears);
	    cboYears.setAdapter(adapter2);

	    return dialog;
	}

	private class CreditCardTypesAdapter extends ArrayAdapter<Integer> {
		Context context;
	    int layoutResourceId;   

	    public CreditCardTypesAdapter(Context context, int layoutResourceId, int creditCardImages[]) {
	        super(context, layoutResourceId);

			for (int i = 0; i < creditCardImages.length; i++) {
				this.add(Integer.valueOf(creditCardImages[i]));
			}
	        
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	    }

	    	    
	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	    	return getView(position, convertView, parent);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ImageHolder holder = null;
	       
	        if(row == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	           
	            holder = new ImageHolder();
	            holder.imgListImage = (ImageView)row.findViewById(R.id.imgListImage);
	           
	            row.setTag(holder);
	        } else {
	            holder = (ImageHolder)row.getTag();
	        }
	       
	        holder.imgListImage.setImageResource(this.getItem(position));

	        return row;
	    }
	   
	    class ImageHolder {
	        ImageView imgListImage;
	    }
	}
}

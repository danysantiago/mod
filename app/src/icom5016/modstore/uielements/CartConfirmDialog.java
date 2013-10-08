package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class CartConfirmDialog extends DialogFragment {

	public CartCofirmDialogListener mListener;
	
	
	public interface CartCofirmDialogListener{
		public void onDialogBuyClick(DialogFragment dialog);
		public void onDialogCancelClick(DialogFragment dialog);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CartCofirmDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SearchFilterDialogListener");
        }
	}
	
								/*Creating Dialog*/
	
	private LinearLayout llDialog;
	private ProgressBar pbDialog;
	private Spinner addrSpinner;
	private Spinner ccSpinner;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
	    View ccdView	= inflater.inflate(R.layout.popup_dialog_confirmation, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	   
		builder.setTitle(R.string.cart_dialog_title);
	    builder.setNegativeButton(R.string.cart_dialog_cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            mListener.onDialogCancelClick(CartConfirmDialog.this);
	        }
	    });
	    builder.setPositiveButton(R.string.cart_dialog_confirm_to_buy, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        		mListener.onDialogBuyClick(CartConfirmDialog.this);
	        }
	    });
	    builder.setView(ccdView);
	    
	    llDialog = (LinearLayout) ccdView.findViewById(R.id.cartDialogLL);
	    pbDialog = (ProgressBar) ccdView.findViewById(R.id.cartDialogProgressBar);
	    addrSpinner = (Spinner) ccdView.findViewById(R.id.cartAddressSpinner);
	    ccSpinner = (Spinner) ccdView.findViewById(R.id.cartCreditCardSpinner);
	   
	    this.doHttpSpinners();
	    
		return builder.create();
	
	}


	private void doHttpSpinners() {
		pbDialog.setVisibility(View.GONE);
		llDialog.setVisibility(View.VISIBLE);
	}
}

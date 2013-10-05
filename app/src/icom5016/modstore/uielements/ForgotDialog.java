package icom5016.modstore.uielements;

import icom5016.modstore.activities.ForgotActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ForgotDialog extends DialogFragment {


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle(R.string.forgot_dialog_title);
		    builder.setItems(R.array.forgot_dialog_values, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	  Bundle bundle = new Bundle();
		            	  Intent forgotIntent = new Intent(getActivity(), ForgotActivity.class);
		            	   switch(which){
		            	   case 0: //UserName
		            		   bundle.putInt(ConstantClass.FORGOT_TYPE_KEY, ConstantClass.FORGOT_TYPE_USERNAME);
		            		   forgotIntent.putExtras(bundle);
		            		   startActivity(forgotIntent);
		            		   break;
		            	   case 1:
		            		   bundle.putInt(ConstantClass.FORGOT_TYPE_KEY, ConstantClass.FORGOT_TYPE_PASSWORD);
		            		   forgotIntent.putExtras(bundle);
		            		   startActivity(forgotIntent);
		            		   break;
		            	   }
		           }
		    });
		    return builder.create();

	}
	
}
 
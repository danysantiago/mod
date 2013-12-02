package icom5016.modstore.activities;

import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_forgot_layout);
		
		final ActionBar ActionBarVar = this.getActionBar();
		//Set title
		ActionBarVar.setTitle(R.string.forgot_dialog_title);
		
		//SetActionBar to Home/Up
		ActionBarVar.setDisplayHomeAsUpEnabled(true);
		ActionBarVar.setHomeButtonEnabled(true);
		
	}

			/*Navigate Up the Stack*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void forgotSendListener(View view){
		
		//Get Email
		EditText email_box = (EditText) this.findViewById(R.id.forgotEmail);
		String email = email_box.getText().toString().trim();
		
		if(AndroidResourceFactory.validateEmail(email)){
			//Send Notification Based on Dialog Selection
			Bundle bundle = this.getIntent().getExtras();
			if(bundle != null){
				int type = bundle.getInt(ConstantClass.FORGOT_TYPE_KEY);
				switch(type){
				case ConstantClass.FORGOT_TYPE_USERNAME:
					Toast.makeText(this, "Username forgoten, email: "+email, Toast.LENGTH_SHORT).show();
					break;
				case ConstantClass.FORGOT_TYPE_PASSWORD:
					Toast.makeText(this, "Password forgorten, email: "+email, Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		else{
			Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
		}
		
		
	}
}

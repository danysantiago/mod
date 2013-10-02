package icom5016.modstore.activities;

import android.app.ActionBar;
import android.os.Bundle;


public class LogInRegisterActivity extends FragmentInterfaceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Obtain the ActionBar
		final ActionBar ActionBarVar = this.getActionBar();
		
		ActionBarVar.setTitle(R.string.title_settings);
	}

}

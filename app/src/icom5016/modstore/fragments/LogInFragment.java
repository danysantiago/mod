package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInFragment extends Fragment implements OnClickListener{
	
	
	public LogInFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_login, container,false);
		
		//Set Listeners for Buttons
		CheckBox cb = (CheckBox) view.findViewById(R.id.login_show);
		cb.setOnClickListener(this);
		
		TextView tv = (TextView) view.findViewById(R.id.login_forgot);
		tv.setOnClickListener(this);
		
		Button btn = (Button) view.findViewById(R.id.login_btn);
		btn.setOnClickListener(this);
		
		
		return view;
	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_show:
        	this.showButtonListener((CheckBox)v);
            break;
        case R.id.login_forgot:
        	this.forgotButtonListener((TextView)v);
        	break;
        case R.id.login_btn:
        	this.loginButtonListener((Button)v);
        }
    }
	
	public void showButtonListener(CheckBox view){
		EditText password_box = (EditText) this.getView().findViewById(R.id.login_field_password);
		if(view.isChecked()){
            password_box.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			password_box.setInputType(129);
		}
	}
	
	public void forgotButtonListener(TextView view){
		
	}
	
	public void loginButtonListener(Button view){
		EditText username_box = (EditText) this.getView().findViewById(R.id.login_field_username);
		EditText password_box = (EditText) this.getView().findViewById(R.id.login_field_password);
		
		User user = DataFetchFactory.fetchAndValidateUser(username_box.getText().toString(), password_box.getText().toString());
		
		if(user != null){
			this.getActivity().finish();
		}
		else{
			Toast.makeText(this.getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();;
		}
		
	}
	
}

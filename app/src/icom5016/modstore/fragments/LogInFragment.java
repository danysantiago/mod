package icom5016.modstore.fragments;

import org.json.JSONObject;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;
import icom5016.modstore.uielements.ForgotDialog;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		new ForgotDialog().show(this.getActivity().getFragmentManager(), ConstantClass.FORGOT_TAG);;
	}
	
	public void loginButtonListener(Button view){
		EditText username_box = (EditText) this.getView().findViewById(R.id.login_field_username);
		EditText password_box = (EditText) this.getView().findViewById(R.id.login_field_password);
		
		String user = username_box.getText().toString().trim();
		String pass = password_box.getText().toString().trim();
		
		doHttpLogin(user, pass);
		
	}
	
	public void doHttpLogin(String username, String password) {
		
		try {
			JSONObject credentials = new JSONObject();
			credentials.put("user", username);
			credentials.put("pass", password);
		}
		
		Bundle params = new Bundle();
		params.putString("url", Server.BASE_URL + "/login");
		params.putString("method", "POST");
		params.putString("payload", credentials.toString());
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				User user = new User(json);
				setUserInSharedPreferences(user);
				getActivity().finish();
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();;
			}
		});
		request.execute();
	}
	
	public void setUserInSharedPreferences(User user){
		SharedPreferences.Editor preferencesEdit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
		preferencesEdit.putString(ConstantClass.USER_USERNAME_KEY, user.getUsername());
		preferencesEdit.putString(ConstantClass.USER_FIRSTNAME_KEY, user.getFirstName());
		preferencesEdit.putString(ConstantClass.USER_LASTNAME_KEY, user.getLastName());
		preferencesEdit.putString(ConstantClass.USER_MIDDLENAME_KEY, user.getMiddleName());
		preferencesEdit.putString(ConstantClass.USER_EMAIL_KEY, user.getEmail());
		preferencesEdit.putBoolean(ConstantClass.USER_IS_ADMIN_KEY, user.isAdmin());
		preferencesEdit.putInt(ConstantClass.USER_UID_KEY, user.getUid());
		preferencesEdit.putBoolean(ConstantClass.USER_IS_LOGIN, true);
		preferencesEdit.commit();
	}
	
}

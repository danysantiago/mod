package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.dialog.ForgotDialog;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
	
	private ProgressDialog pd;
	private Activity thisActivity;
	
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
		Button btnRegister = (Button) view.findViewById(R.id.login_register);
		btnRegister.setOnClickListener(this);
		
		this.thisActivity = this.getActivity();
		
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
        	break;
        case R.id.login_register:
        	this.btnRegisterListener(v);
        	break;
        	
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
		
		String username = username_box.getText().toString().trim();
		String password = password_box.getText().toString().trim();
		
		try {
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.logging_message));
			pd.show();
			
	        doHttpLogin(username, password);
        } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}
	
	private void doHttpLogin(String username, String password) throws JSONException {
		Bundle params = new Bundle();
		params.putString("url", Server.User.LOGIN);
		params.putString("method", "POST");
		
		JSONObject credentials = new JSONObject();
		credentials.put("user", username);
		credentials.put("pass", password);
		
		HttpRequest request = new HttpRequest(params, credentials, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					if(json.getString("status").equals("OK")) {
						User user = new User(json.getJSONObject("account"));
						DataFetchFactory.setUserInSharedPreferences(user, thisActivity);
						MainActivity ma = (MainActivity) getActivity();
						ma.reloadActionBarAndUser(ma.getActionBar());
						ma.fragmentStack.pop();
						ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), ma.fragmentStack.peek());
						
					} else {
						Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();;
					}
				} catch (JSONException e) {
					Toast.makeText(thisActivity, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), R.string.errmsg_no_connection, Toast.LENGTH_LONG).show();;
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	    
    }

	private void btnRegisterListener(View view){
		MainActivity ma = (MainActivity) this.getActivity();
		ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new RegisterFragment());
	}
	
	
}

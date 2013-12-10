package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements OnClickListener{
	public RegisterFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_register, container,false);
		Button btn = (Button) view.findViewById(R.id.register_btn);
		btn.setOnClickListener(this);
		
		return view;
	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.register_btn:
        	this.registerBtnListener((Button)v);
            break;
        }
    }

	private void registerBtnListener(Button v) {
		EditText email = (EditText) getView().findViewById(R.id.register_email);
		EditText firstName = (EditText) getView().findViewById(R.id.register_fn);
		EditText userName = (EditText) getView().findViewById(R.id.register_username);
		EditText lastName = (EditText) getView().findViewById(R.id.register_lastname);
		EditText password = (EditText) getView().findViewById(R.id.register_password);
		EditText verifyPassword = (EditText) getView().findViewById(R.id.register_confirm_pw);
		
		
		if(!verifyPassword.getText().toString().equals(password.getText().toString())) {
			Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//Do validation
		JSONObject newUser = new JSONObject();
		try {
			newUser.put("email", email.getText().toString());
			newUser.put("user_name", userName.getText().toString());
			newUser.put("first_name", firstName.getText().toString());
			newUser.put("last_name", lastName.getText().toString());
			newUser.put("user_password", password.getText().toString());
			newUser.put("is_admin", "0");
			
			doHttpRegister(newUser);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doHttpRegister(JSONObject newUser) {
		Bundle params = new Bundle();
		params.putString("url", Server.User.REGISTER);
		params.putString("method", "POST");
		HttpRequest request = new HttpRequest(params, newUser, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				String status;
				try {
					status = json.getString("status");
					
					if(status.equals("ok")) {
						Toast.makeText(getActivity(), "Registration Sucessful, proceed to log in.", Toast.LENGTH_LONG).show();					
					} else if (status.equals("email taken")) {
						Toast.makeText(getActivity(), "Email already in use.", Toast.LENGTH_SHORT).show();					
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Registration unsucessful.", Toast.LENGTH_SHORT).show();
				
			}
		});
		request.execute();
	}
}

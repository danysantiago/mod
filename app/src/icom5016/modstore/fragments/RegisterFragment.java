package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
		//Get Everything
		Toast.makeText(getActivity(), "No se te olvide cambiar esto", Toast.LENGTH_LONG).show();
	}
}

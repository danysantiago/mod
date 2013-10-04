package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.uielements.CreditCardAdapter;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class AddressesFragment extends ListFragment {
	public AddressesFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("Addresses:");

        return v;
    }

	@Override
	void addOnClickListener(View v) {
		Toast t = Toast.makeText(v.getContext(), "The user wants to add something!", Toast.LENGTH_SHORT);
		t.show();
	}
}
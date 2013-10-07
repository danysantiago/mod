package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Address;
import icom5016.modstore.uielements.AddressAdapter;
import icom5016.modstore.uielements.AddressDialog;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class AddressesFragment extends SettingListFragment {
	public AddressesFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("Addresses:");

        requestAddresses();
        
        //Locale.getDefault().getDisplayCountry();
        
        return v;
    }

	private void requestAddresses() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.Addresses.GETALL);
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				//Pass JSON to Adapter
				AddressAdapter adapter = new AddressAdapter(getActivity(), R.layout.listview_address_row, json);
				lstListView.setAdapter(adapter);
				lstListView.setOnItemClickListener(new listOnClick());

				//Show list view
				showList();
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Couldn't load the Addresses [ERR: 2]", Toast.LENGTH_SHORT).show();
				showError();
			}
		});
		
		request.execute();
	}
	
	class listOnClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	        DialogFragment dialog = new AddressDialog();
	        ((AddressDialog)dialog).address = (Address)lstListView.getAdapter().getItem(arg2);
	        dialog.show(getActivity().getSupportFragmentManager(), "AddressDialog");
		}
	}

	@Override
	void addOnClickListener(View v) {
        DialogFragment dialog = new AddressDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "AddressDialog");
	}
}
package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.CreditCardAdapter;
import icom5016.modstore.dialog.CreditCardDialog;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CreditCardsFragment extends SettingListFragment {
	public CreditCardsFragment() { };
	int pos;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("Credit Cards:");
        
        requestCreditCards();
        
        return v;
    }
	
	private void requestCreditCards() {
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.CreditCards.GET + "?userId=" + u.getGuid());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				//Pass JSON to Adapter
				CreditCardAdapter adapter = new CreditCardAdapter(getActivity(), R.layout.listview_creditcard_row, json);
				lstListView.setAdapter(adapter);
				lstListView.setOnItemClickListener(new listOnClick());

				//Show list view
				showList();
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Couldn't load the Credit Cards [ERR: 2]", Toast.LENGTH_SHORT).show();
				showError();
			}
		});
		
		request.execute();
	}
	
	class listOnClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	        //DialogFragment dialog = new CreditCardDialog();
	        //((CreditCardDialog)dialog).creditCard = (CreditCard)lstListView.getAdapter().getItem(arg2);
	        //dialog.show(getActivity().getSupportFragmentManager(), "NewCreditCardDialog");
	        pos = arg2;
	        requestAddresses();
		}
	}
	
	private void requestAddresses() {
		User u = DataFetchFactory.getUserFromSPref(getActivity());
		
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.Addresses.GET + "?userId=" + u.getGuid());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
		        DialogFragment dialog = new CreditCardDialog();
		        
		        if (pos != -1) {
		        	((CreditCardDialog)dialog).creditCard = (CreditCard) lstListView.getAdapter().getItem(pos);
		        }
		        
		        ((CreditCardDialog)dialog).addressesJson = json;
		        
		        dialog.show(getActivity().getSupportFragmentManager(), "NewCreditCardDialog");
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Couldn't load the Addresses for Credit Card Dialog [ERR: 3]", Toast.LENGTH_SHORT).show();
				showError();
			}
		});
		
		request.execute();
	}

	@Override
	public void addClick() {
		pos = -1;
        requestAddresses();
        //DialogFragment dialog = new CreditCardDialog();
        //dialog.show(getActivity().getSupportFragmentManager(), "NewCreditCardDialog");
	}
}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.uielements.CreditCardAdapter;
import icom5016.modstore.uielements.CreditCardDialog;

import org.json.JSONObject;

import android.app.ProgressDialog;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("Credit Cards:");
        
        requestCreditCards();
        
        return v;
    }
	
	private void requestCreditCards() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.CreditCards.GETALL);
		
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
	        DialogFragment dialog = new CreditCardDialog();
	        ((CreditCardDialog)dialog).creditCard = (CreditCard)lstListView.getAdapter().getItem(arg2);
	        dialog.show(getActivity().getSupportFragmentManager(), "NewCreditCardDialog");
		}
	}

	@Override
	void addOnClickListener(View v) {
        DialogFragment dialog = new CreditCardDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "NewCreditCardDialog");
	}
}

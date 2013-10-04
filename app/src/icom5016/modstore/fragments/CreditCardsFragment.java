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

public class CreditCardsFragment extends ListFragment {
	public CreditCardsFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("Credit Cards:");
        
        requestCategories();
        
        return v;
    }
	
	private void requestCategories() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.CreditCards.GETALL);
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				//Pass JSON to Adapter
				CreditCardAdapter adapter = new CreditCardAdapter(getActivity(), R.layout.listview_creditcard_row, json);
				lstCreditCards.setAdapter(adapter);

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

	@Override
	void addOnClickListener(View v) {
		Toast t = Toast.makeText(v.getContext(), "The user wants to add something!", Toast.LENGTH_SHORT);
		t.show();
	}
}

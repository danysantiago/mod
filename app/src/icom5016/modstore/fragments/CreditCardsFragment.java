package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.MainCategoryFragment.ProductAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.uielements.CreditCardAdapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardsFragment extends Fragment {
	private ListView lstCreditCards;
	private ImageButton btnAdd;
	private TextView txtTitle;
	private ProgressBar listProgressBar;
	private TextView txtError;
	
	public CreditCardsFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list, container,false);
		ActionBar ab = this.getActivity().getActionBar();

		// Load all the fragment elements.
        lstCreditCards = (ListView)view.findViewById(R.id.lstListView);       
    	btnAdd = (ImageButton)view.findViewById(R.id.btnListAdd);
        txtTitle = (TextView)view.findViewById(R.id.txtListTitle);
        listProgressBar = (ProgressBar)view.findViewById(R.id.listProgressBar);
        txtError = (TextView)view.findViewById(R.id.txtListErrorMessage);

        btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast t = Toast.makeText(v.getContext(), "The user wants to add something!", Toast.LENGTH_SHORT);
				t.show();
			}
		});
        
    	ab.setTitle("Settings");
        txtTitle.setText("Credit Cards:");
        
        showLoading();
        
        requestCategories();
        
        return view;
    }
	
	private void showLoading() {
		listProgressBar.setVisibility(View.VISIBLE);
		lstCreditCards.setVisibility(View.GONE);
		btnAdd.setVisibility(View.GONE);
		txtTitle.setVisibility(View.GONE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.GONE);
	}
	
	private void showList() {
		listProgressBar.setVisibility(View.GONE);
		lstCreditCards.setVisibility(View.VISIBLE);
		btnAdd.setVisibility(View.VISIBLE);
		txtTitle.setVisibility(View.VISIBLE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.GONE);
	}
	
	public void showError() {
		listProgressBar.setVisibility(View.GONE);
		lstCreditCards.setVisibility(View.GONE);
		btnAdd.setVisibility(View.GONE);
		txtTitle.setVisibility(View.GONE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.VISIBLE);
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
}

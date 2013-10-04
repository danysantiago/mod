package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
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

public abstract class ListFragment extends Fragment {
	protected ListView lstCreditCards;
	protected ImageButton btnAdd;
	protected TextView txtTitle;
	protected ProgressBar listProgressBar;
	protected TextView txtError;
	protected ActionBar actionBar;
	
	public ListFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list, container,false);
		
		// Load all the fragment elements.
        lstCreditCards = (ListView)view.findViewById(R.id.lstListView);       
    	btnAdd = (ImageButton)view.findViewById(R.id.btnListAdd);
        txtTitle = (TextView)view.findViewById(R.id.txtListTitle);
        listProgressBar = (ProgressBar)view.findViewById(R.id.listProgressBar);
        txtError = (TextView)view.findViewById(R.id.txtListErrorMessage);
        actionBar = this.getActivity().getActionBar();
        
        btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addOnClickListener(v);
			}
        });
        
        showLoading();
        
        return view;
    }
	
	protected void setTitle(String title) {
		txtTitle.setText(title);
	}
	
	protected void showLoading() {
		listProgressBar.setVisibility(View.VISIBLE);
		lstCreditCards.setVisibility(View.GONE);
		btnAdd.setVisibility(View.GONE);
		txtTitle.setVisibility(View.GONE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.GONE);
	}
	
	protected void showList() {
		listProgressBar.setVisibility(View.GONE);
		lstCreditCards.setVisibility(View.VISIBLE);
		btnAdd.setVisibility(View.VISIBLE);
		txtTitle.setVisibility(View.VISIBLE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.GONE);
	}
	
	protected void showError() {
		listProgressBar.setVisibility(View.GONE);
		lstCreditCards.setVisibility(View.GONE);
		btnAdd.setVisibility(View.GONE);
		txtTitle.setVisibility(View.GONE);
		listProgressBar.setVisibility(View.GONE);
		txtError.setVisibility(View.VISIBLE);
	}
	
	abstract void addOnClickListener(View v);
}
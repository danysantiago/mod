package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.uielements.CreditCardAdapter;

import java.util.ArrayList;

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
	
	public CreditCardsFragment() {
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list, container,false);
		ActionBar ab = this.getActivity().getActionBar();
		
		ab.setTitle("Settings");
		
		ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Omar G Soto Fortuno", "06/2016"));
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Daniel Santiago", "06/2016"));
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Manuel E Marquez", "06/2016"));
       
        CreditCardAdapter adapter = new CreditCardAdapter(view.getContext(), R.layout.listview_creditcard_row, creditCards);
       
        lstCreditCards = (ListView)view.findViewById(R.id.lstListView);       
        lstCreditCards.setAdapter(adapter);
        
        btnAdd = (ImageButton)view.findViewById(R.id.btnListAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast t = Toast.makeText(v.getContext(), "The user wants to add something!", Toast.LENGTH_SHORT);
				t.show();
			}
		});
        
        txtTitle = (TextView)view.findViewById(R.id.txtListTitle);
        txtTitle.setText("Credit Cards:");
        
        listProgressBar = (ProgressBar)view.findViewById(R.id.listProgressBar);
        txtError = (ProgressBar)view.findViewById(R.id.txtListErrorMessage);
        
        return view;
    }
}

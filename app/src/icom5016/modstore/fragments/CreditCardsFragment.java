package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.CreditCard;
import icom5016.modstore.uielements.CreditCardAdapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CreditCardsFragment extends Fragment {
	private ListView lstCreditCards;
	
	public CreditCardsFragment() {
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_creditcards, container,false);
		
		ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Omar G Soto Fortuno", "06/2016"));
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Daniel Santiago", "06/2016"));
        creditCards.add(new CreditCard(0, "1234 1234 1234 1234", "Manuel E Marquez", "06/2016"));
       
        CreditCardAdapter adapter = new CreditCardAdapter(view.getContext(), R.layout.listview_creditcard_row, creditCards);
       
        lstCreditCards = (ListView)view.findViewById(R.id.lstListView);       
        lstCreditCards.setAdapter(adapter);
        
        return view;
    }
}

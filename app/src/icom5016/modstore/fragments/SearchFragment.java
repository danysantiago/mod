package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchFragment extends Fragment {
	
	private String searchQuery = "";
	private boolean isEmptyQuery = true;
	
	public SearchFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_search, container,false);
		
		//Obtain from bundle
		this.searchQuery = this.getArguments().getString(ConstantClass.SEARCH_FRAGMENT_QUERY_ID);
		this.isEmptyQuery = this.getArguments().getBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_ID);
		Log.v("Arraived", "Yay");
		
		
		TextView textView = (TextView) view.findViewById(R.id.textview_search);
		if(isEmptyQuery)
			textView.setText("Nothing to Search");
		else{
			textView.setText(this.searchQuery);
		}
		
		return view;
	}
	
}

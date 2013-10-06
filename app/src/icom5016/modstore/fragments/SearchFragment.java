package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchFragment extends Fragment {
	
	private Bundle searchArgs;
	private boolean isSearchClick;
	private ProgressDialog pd;
	
	public SearchFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		
		//Set Layout
		View view = inflater.inflate(R.layout.fragment_search, container,false);
		
		//Get Args
		 this.searchArgs = this.getArguments();
		 
		 this.isSearchClick = this.searchArgs.getBoolean(ConstantClass.SEARCH_FRAGMENT_BOOL_KEY);
		 
		 //If Search Click False return view
		 if(!isSearchClick){
			 return view;
		 }

		 //GenerateFragment
		// try {
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.search_progress_msg));
				pd.show();
				
		        this.doHttpSearch(searchArgs);
	      //  } catch (JSONException e) {
		    //    e.printStackTrace();
	        //}
	    
		return view;
	}
	
	private void doHttpSearch(Bundle bundle){
		
	}
	
}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	public MainFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_main, container,false);
		return view;
	}
	/*
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  
	  Bundle params = new Bundle();
    params.putString("url", Server.BASE_URL + "/example");
    params.putString("method", "GET");
    HttpRequest request = new HttpRequest(params, new HttpCallback() {
      
      @Override
      public void onSucess(JSONObject json) {
        TextView txt = (TextView) getActivity().findViewById(R.id.main_textview);
        try {
          txt.setText(json.getString("message"));
        } catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
      
      @Override
      public void onFailed() {
        // TODO Auto-generated method stub
        
      }
    });
	}*/
}

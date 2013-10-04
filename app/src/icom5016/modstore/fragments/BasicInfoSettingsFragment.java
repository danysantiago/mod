package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicInfoSettingsFragment extends Fragment {

	
	public BasicInfoSettingsFragment() { };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_basicsettings, container, false);
        
        return view;
    }

}

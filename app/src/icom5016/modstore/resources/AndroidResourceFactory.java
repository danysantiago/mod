package icom5016.modstore.resources;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class AndroidResourceFactory {

	//Fragment Manager
	public static void setNewFragment(Activity activeActivity, Fragment fragment, int id){
		FragmentManager fragmentManager = activeActivity.getFragmentManager();
        fragmentManager.beginTransaction().replace(id, fragment).commit();
	}
	
	public static boolean validateEmail(String email){
		//TODO: Validate
		return true;
	}

}
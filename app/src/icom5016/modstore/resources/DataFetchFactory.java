package icom5016.modstore.resources;

import icom5016.modstore.models.User;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;




public class DataFetchFactory {

	//TODO: Change to Dynamic Loading
	public static String[] fetchMainCategories(){
		return new String[]{"Books", "Electronics", "Computers", "Clothing", "Shoes", "Sports" };
	}

	//TODO: Load 
	public static String[] fetchSubCategories(String categoryTitle) {
		//Super static
		return new String[]{"Yay","this","is","static","don't","forget"};
	}

	public static User fetchAndValidateUser(String string, String string2) {
		
		return new User("mamanu", "Manuel", "Enrique", "Marquez", "manuel.marquez1@upr.edu",0 ,true);
	}
	
	public static User getUserInSharedPreferences(Activity activity){
		//Verify Log-In
				SharedPreferences preferences = //If Open First Time: Creates File; O.W. Reads it
						activity.getSharedPreferences(ConstantClass.USER_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
				boolean isUserLogIn = preferences.getBoolean(ConstantClass.USER_IS_LOGIN, false);
				
					//If Log-In Create 
				if(isUserLogIn){
					//pre: must be log-in thus data will be loaded into preferences
					String userName = preferences.getString(ConstantClass.USER_USERNAME_KEY, ""); 
					String firstName = preferences.getString(ConstantClass.USER_FIRSTNAME_KEY, "");
					String middleName = preferences.getString(ConstantClass.USER_MIDDLENAME_KEY, "");
					String lastName = preferences.getString(ConstantClass.USER_LASTNAME_KEY, "");
					String email = preferences.getString(ConstantClass.USER_EMAIL_KEY, "");
					boolean isAdmin = preferences.getBoolean(ConstantClass.USER_IS_ADMIN_KEY, false);
					int guid = preferences.getInt(ConstantClass.USER_GUID_KEY, -1);
					return new User(userName, firstName, middleName, lastName, email, guid ,isAdmin);	
				}
				
				return null;
	}
	
	public static User getUserWithId(int id){
		return new User("mamanu", "Manuel", "Enrique", "Marquez", "manuel.marquez1@upr.edu",0 ,true);
	}
	
}

package icom5016.modstore.resources;

import icom5016.modstore.models.User;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;




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
	
	public static void setUserInSharedPreferences(User user, Activity activity){
		SharedPreferences preferences = //If Open First Time: Creates File; O.W. Reads it
				activity.getSharedPreferences(ConstantClass.USER_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		preferences.edit().putString(ConstantClass.USER_USERNAME_KEY, user.getUsername()).apply();
		preferences.edit().putString(ConstantClass.USER_FIRSTNAME_KEY, user.getFirstName()).apply();
		preferences.edit().putString(ConstantClass.USER_LASTNAME_KEY, user.getLastName()).apply();
		preferences.edit().putString(ConstantClass.USER_MIDDLENAME_KEY, user.getMiddleName()).apply();
		preferences.edit().putString(ConstantClass.USER_EMAIL_KEY, user.getEmailString()).apply();
		preferences.edit().putBoolean(ConstantClass.USER_IS_ADMIN_KEY, user.isAdmin()).apply();
		preferences.edit().putInt(ConstantClass.USER_GUID_KEY, user.getGuid()).apply();
		preferences.edit().putBoolean(ConstantClass.USER_IS_LOGIN, true).apply();
		
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
		return null;
	}
	
}

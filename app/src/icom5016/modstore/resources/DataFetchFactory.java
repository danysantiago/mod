package icom5016.modstore.resources;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class DataFetchFactory {


							/* User Data */
	
	
	
	
	
	//TODO: Change to Dynamic Loading
	public static Category[] fetchMainCategories(){
		return new Category[]{
				new Category(-1, 0, "Books"),
				new Category(-1, 5, "Electronics"),
				new Category(-1, 11, "Computers"),
				new Category(-1, 16, "Clothing"),
				new Category(-1,26, "Shoes"),
				new Category(-1,30, "Sports")
		};
	}

	//TODO: Load 
	public static String[] fetchSubCategories(String categoryTitle) {
		//Super static
		return new String[]{"Yay","this","is","static","don't","forget"};
	}

	public static User fetchAndValidateUser(String string, String string2) {
		
		return new User("mamanu", "Manuel", "Enrique", "Marquez", "manuel.marquez1@upr.edu",0 ,true);
	}
	
	public static User getUserFromSPref(Activity activity){
		//Verify Log-In
		SharedPreferences preferences = //If Open First Time: Creates File; O.W. Reads it
				activity.getSharedPreferences(ConstantClass.USER_FILE, Context.MODE_PRIVATE);
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
	
	public static int[] getCreditCardImages() {
		return new int[]{R.drawable.cc_visa, R.drawable.cc_mastercard, R.drawable.cc_americanexpress, R.drawable.cc_discover, R.drawable.cc_ebay, R.drawable.cc_googlecheckout, R.drawable.cc_paypal};
	}
	
	
	public static User getUserWithId(int id){
		return new User("mamanu", "Manuel", "Enrique", "Marquez", "manuel.marquez1@upr.edu",0 ,true);
	}
	
	public static String[] getNextYears(int next) {
	    Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    
	    String strYears[] = new String[next];
	    
	    for (int i = 0; i < next; i++) {
	    	strYears[i] = String.valueOf(year + i);
	    }
	    
	    return strYears;
	}
	
	public static String[] getCountries() {
		return new String[]{"Afghanistan","Aland Islands","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antarctica","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bonaire Sint Eustatius and Saba","Bosnia and Herzegovina","Botswana","Bouvet Island","Brazil","British Indian Ocean Territory","Brunei Darussalam","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands","Colombia","Comoros","Congo","Congo","Cook Islands","Costa Rica","Croatia","Cuba","Curacao","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Falkland Islands (Malvinas)","Faroe Islands","Fiji","Finland","France","French Guiana","French Polynesia","French Southern Territories","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guernsey","Guinea","Guinea-Bissau","Guyana","Haiti","Heard Island and McDonald Islands","Holy See (Vatican City State)","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Isle of Man","Israel","Italy","Ivory Coast","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kiribati","North�Korea","South�Korea","Kuwait","Kyrgyzstan","Lao People's Democratic Republic","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macao","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island","Northern Mariana Islands","Norway","Oman","Pakistan","Palau","Palestinian Territory Occupied","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russian Federation","Rwanda","Saint Barthelemy","Saint Helena Ascension and Tristan da Cunha","Saint Kitts and Nevis","Saint Lucia","Saint Martin (French part)","Saint Pierre and Miquelon","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Sint Maarten (Dutch part)","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Georgia and the South Sandwich Islands","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Svalbard and Jan Mayen","Swaziland","Sweden","Switzerland","Syrian Arab Republic","Taiwan Province of China","Tajikistan","Tanzania","Thailand","Timor-Leste","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu","Venezuela","Viet Nam","Virgin Islands British","Virgin Islands U.S.","Wallis and Futuna","Western Sahara","Yemen","Zambia","Zimbabwe"};
	}
	
	public static String[] fetchAllCategories(){
		return new String[]{"Books", "Electronics", "Computers", "Clothing", "Shoes", "Sports" };
	}

	
	
}

package icom5016.modstore.resources;

import icom5016.modstore.models.User;


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
		// TODO Auto-generated method stub
		return null;
	}
	
}

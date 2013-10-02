package icom5016.modstore.resources;

public class DataFetchFactory {

	//Fetch Drawer Options
	public static String[] fetchDrawerOptions(){
		return new String[]{ "Home", "My Orders", "Settings", "About" };
	}
	
	public static String[] fetchMainCategories(){
		return new String[]{"Books", "Electronics", "Computers", "Clothing", "Shoes", "Sports" };
	}

	public static String[] fetchSubCategories(String categoryTitle) {
		//Super static
		return new String[]{"Yay","this","is","static","don't","forget"};
	}
	
}

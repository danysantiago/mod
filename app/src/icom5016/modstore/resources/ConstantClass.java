package icom5016.modstore.resources;

public class ConstantClass {
	
	//LogIn/Out Constant
	public final static String USER_PREFERENCES_FILENAME = "userFile";
	public final static String USER_IS_LOGIN = "userLogStatus";
	public final static String USER_USERNAME_KEY = "userUsername";
	public final static String USER_FIRSTNAME_KEY = "userFirstName";
	public final static String USER_MIDDLENAME_KEY = "userMiddleName";
	public final static String USER_LASTNAME_KEY = "userLastName";
	public final static String USER_EMAIL_KEY = "userEmail";
	public final static String USER_IS_ADMIN_KEY = "userIsAdmin";
	
	//Search Constants
	public final static String SEARCH_FRAGMENT_BOOL_ID = "searchBool";
	public final static String SEARCH_FRAGMENT_QUERY_ID = "searchQuery";
	
	//Category Constants
	public final static String MAINCATEGORY_FRAGMENT_CATEGORY_KEY = "searchCategory";
	public final static String MAINCATEGORY_FRAGMENT_MAIN_VALUE = "mainCategoryList";
	
	//Drawer Lists
	public final static String[] DRAWER_GUEST_LIST = new String[]{"Home", "Categories", "About", "Log in","Register",}; 
	public final static String[] DRAWER_USER_LIST = new String[]{"Home", "Categories", "My Items", "Sell Item", "Settings", "About","Log Out"};
	
	//Log-In Register Key
	public final static String LOGINREGISTER_FLAG = "loginOrRegister";
	
	//MainActivity FRAGEMENT Key-Value
	public final static String MAINACTIVITY_FRAGMENT_KEY = "mainActivityKey";
	public final static int MAINACTIVITY_FRAGMENT_CATEGORY = 0;
	public final static int MAINACTIVITY_FRAGMENT_MY_ITEMS = 1;
	public final static int MAINACTIVITY_FRAGMENT_SELL_ITEMS = 2;
	
	

}
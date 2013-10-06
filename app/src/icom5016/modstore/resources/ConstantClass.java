package icom5016.modstore.resources;

public class ConstantClass {
	
	//Pre
	public final static String USER_PREFERENCES_FILENAME = "preferencesUserFile";
	//LogIn/Out Constant
	public final static String USER_IS_LOGIN = "userLogStatus";
	public final static String USER_USERNAME_KEY = "userUsername";
	public final static String USER_FIRSTNAME_KEY = "userFirstName";
	public final static String USER_MIDDLENAME_KEY = "userMiddleName";
	public final static String USER_LASTNAME_KEY = "userLastName";
	public final static String USER_EMAIL_KEY = "userEmail";
	public final static String USER_IS_ADMIN_KEY = "userIsAdmin";
	public final static String USER_GUID_KEY = "userGUID";
	
	//Search Constants
	public final static String SEARCH_FRAGMENT_BOOL_KEY = "searchBool";
	public final static String SEARCH_FRAGMENT_QUERY_KEY = "searchQuery";
	
	//Category Constants
	public final static String CATEGORY_LIST_PARENT_KEY = "categoryListParentKey";
	
	//Drawer Lists
	public final static String[] DRAWER_GUEST_LIST = new String[]{"Home", "Categories", "About", "Log in","Register",}; 
	public final static String[] DRAWER_USER_LIST = new String[]{"Home", "Categories", "My Items", "Sell Item", "Settings", "About","Log Out"};
	public final static String[] DRAWER_ADMIN_LIST = new String[]{"Home", "Categories", "My Items", "Sell Item", "Settings", "About","Log Out", "Admin Menu"};
	
	//Log-In Register Key
	public final static String LOGINREGISTER_FLAG = "loginOrRegister";
	
	//MainActivity FRAGEMENT Key-Value
	public final static String MAINACTIVITY_FRAGMENT_KEY = "mainActivityKey";
	public final static int MAINACTIVITY_FRAGMENT_CATEGORY = 0;
	public final static int MAINACTIVITY_FRAGMENT_MY_ITEMS = 1;
	public final static int MAINACTIVITY_FRAGMENT_SELL_ITEMS = 2;
	
	//ForgotLogIn
	public final static String 	FORGOT_TAG = "forgotUserNameOrPassword";
	public final static String FORGOT_TYPE_KEY = "forgotTypeKey";
	public final static int FORGOT_TYPE_USERNAME = 0;
	public final static int FORGOT_TYPE_PASSWORD = 1;
	
	//SEARCH FILTER
	public final static String SEARCH_FILTER_DIALOG_TAG = "searchFilterDialogTag";
	public final static String[] SEARCH_FILTER_SORT = new String[]{"Best Match","Price: Low to High", "Price: High to Low", "Time: ending soonest", "Time: newly listed"};
	public static final String[] SEARCH_FILTER_RATING = new String[]{"Any", "5 Stars or More", "4 Stars or More", "3 Stars or More", "2 Stars or More", "1 Stars or More" };
	public static final String[] SEARCH_FILTER_CONDITION = new String[]{"Any", "Buy It Now", "Bid Only"};
	
	//Dialog Keys
	public static final String SEARCH_DIALOG_SORT_KEY = "dialogSortKey";
	public static final String SEARCH_DIALOG_CATEGORIES_KEY = "dialogCategoriesKey";
	public static final String SEARCH_DIALOG_RATING_KEY = "dialogRatingKey";
	public static final String SEARCH_DIALOG_CONDITION_KEY = "dialogConditionKey";
	public static final String SEARCH_DIALOG_START_PRICE_KEY = "dialogStartPriceKey";
	public static final String SEARCH_DIALOG_END_PRICE_KEY = "dialogEndPriceKey";
	

}

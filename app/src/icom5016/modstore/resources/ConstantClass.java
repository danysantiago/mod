package icom5016.modstore.resources;



public class ConstantClass {
	
	
	//FILENAMES Strings
	public final static String CATEGORIES_FILE = "categoriesFile.ini";
	public final static String USER_FILE = "userFile.ini";
	public final static String APP_PREFERENCES_FILE = "appPreferences.ini";
	
	//Categories File Variable Keys
	public static class CategoriesFile
	{
		public final static String ALL_CAT_JSON_KEY = "allCatJsonKey";
		public final static String LOAD_BOOLEAN_KEY = "loadBooleanKey";
		public final static int ALL_CATEGORIES = -2;
	}
	
	//Drawer Lists
	public final static String[] DRAWER_GUEST_LIST = new String[]{"Home","Shop By Category" ,"About", "Log in","Register",}; 
	public final static String[] DRAWER_USER_LIST = new String[]{"Home", "Shop By Category", "My Store", "Sell Product", "My Account", "About", "Log Out"};
	public final static String[] DRAWER_ADMIN_LIST = new String[]{"Home", "Shop By Category", "My Store", "Sell Product", "My Account", "About", "Log Out", "Admin Menu"};
	public static final String[] DRAWER_MYORDERS = new String[]{"Orders", "Bidding", "Selling"}; 
	
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
	
	
	
	//Log-In Register Key
	public final static String LOGINREGISTER_FLAG = "loginOrRegister";
	
	//MainActivity FRAGEMENT Key-Value
	public final static String MAINACTIVITY_FRAGMENT_KEY = "mainActivityKey";
	public final static int MAINACTIVITY_FRAGMENT_CATEGORY = 0;
	public final static int MAINACTIVITY_FRAGMENT_MY_ITEMS = 1;
	public final static int MAINACTIVITY_FRAGMENT_ITEMS_FOR_SALE = 3;
	public final static int MAINACTIVITY_FRAGMENT_ITEMS_SOLD = 4;
	
	//ForgotLogIn
	public final static String 	FORGOT_TAG = "forgotUserNameOrPassword";
	public final static String FORGOT_TYPE_KEY = "forgotTypeKey";
	public final static int FORGOT_TYPE_USERNAME = 0;
	public final static int FORGOT_TYPE_PASSWORD = 1;
	
	//SEARCH FILTER
	public final static String SEARCH_FILTER_DIALOG_TAG = "searchFilterDialogTag";
	public final static String[] SEARCH_FILTER_SORT = new String[]{"Best Match","Price: Low to High", "Price: High to Low", "Time: ending soonest", "Time: newly listed"};
	public static final String[] SEARCH_FILTER_RATING = new String[]{"Any", "5 Stars or More", "4 Stars or More", "3 Stars or More", "2 Stars or More", "1 Stars or More" };
	public static final String[] SEARCH_FILTER_CONDITION = new String[]{"Any", "Both", "Buy It Now", "Bid Only"};
	
	public static final String[] SEARCH_FILTER_SORT_URL_PARMS = new String[]{"best", "price_asc", "price_desc", "time_asc", "time_desc"};
	public static final String[] SEARCH_FILTER_CONDITION_URL_PARMS = new String[]{"all", "both", "buy", "bid"};
	
	//Dialog Keys
	public static final String SEARCH_DIALOG_SORT_KEY = "dialogSortKey";
	public static final String SEARCH_DIALOG_CATEGORIES_INDEX_KEY = "dialogCategoriesKey";
	public static final String SEARCH_DIALOG_CATEGORIES_ID_KEY = "dialogCategoriesIdKey";
	public static final String SEARCH_DIALOG_RATING_KEY = "dialogRatingKey";
	public static final String SEARCH_DIALOG_CONDITION_KEY = "dialogConditionKey";
	public static final String SEARCH_DIALOG_START_PRICE_KEY = "dialogStartPriceKey";
	public static final String SEARCH_DIALOG_END_PRICE_KEY = "dialogEndPriceKey";
	
	//ProductList Constant
	public static final String PRODUCT_LIST_CATEGORY_KEY ="productListCategoryKey";
	
	//My Orders Constants
	public static final String[] BUYING_SPINNER = new String[]{"All Lists", "Bidding", "Didn't Win"};
	public static final String[] SELLING_SPINNER = new String[]{"All Lists", "Active","Sold", "Not Sold"};
	public static final String ORDERID_KEY = "orderIdKey";
	public static final String SELLING_ACTIVE = "activeKey";
	public static final String SELLING_SOLD = "soldKey";
	public static final String SELLING_NOTSOLD = "notSoldKey";
	public static final String SELLING_TYPE_VIEW_KEY = "sellingTypeViewKey";
	public static final String BUYING_BIDDING = "biddingKey";
	public static final String BUYING_NOTWIN = "didNotWinKey";
	
	//Credit Card Constant
	public static final String[] CREDITCARD_LIST = new String[]{
		"Visa", "MasterCard", "AmericanExpress","Discover", "Ebay", "Google Wallet", "Paypal"
	};
	
	
	//Bidding, Selling, Product Keys
	public static final String PRODUCT_KEY = "productFragKey";
	public static final String PRODUCT_NOTIFICATION_KEY = "productNotificationKey";
	
	//Selling Viewer Activity
	public static final String SELLINGVIEWERACTIVITY_ITEM_KEY = "sellingViwerActivityKey";
	public final static int SELLINGVIEWERACTIVITY_FRAGMENT_SELL_ITEMS = 2;
	
	//Sell Product Fragment
	public static final String PRODUCT_SELL_PROD_KEY = "productObj";
	public static final String SELLER_KEY = "sellerId";
	public static final String[] CART_DIALOG_OPTIONS = new String[]{"View", "Change Quantity", "Remove"};
	
	
}

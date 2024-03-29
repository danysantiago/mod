package icom5016.modstore.http;

public class Server {
	
	//public static final String BASE_URL = "http://192.168.1.9:8080";
	public static final String BASE_URL = "http://ec2-54-226-36-4.compute-1.amazonaws.com:8080";

	public static class User {
		public static final String LOGIN = BASE_URL+"/login";
		
		public static final String RATING = BASE_URL+"/rating";
		public static final String GETALL = BASE_URL+"/users";
		public static final String REGISTER = BASE_URL+"/users/register";
		public static final String UPDATE_PASSWORD = BASE_URL+"/users/updatePassword";
		public static final String UPDATE = BASE_URL+"/users";
		public static final String RATING_CHECK = BASE_URL+"/rating/check";
		public static final String DELETE = BASE_URL+"/users";
	}
	
	public static class Cart{
		public static final String CART = BASE_URL+"/cart";
		public static final String DELETE = BASE_URL+"/cart/delete";
	}

	public static class Products {
	  public static final String GET = BASE_URL+"/products/";
	  public static final String GETCATEGORIES = BASE_URL + "/products/category/";
	  public static final String GETSEARCH = BASE_URL+"/products/search/";
	  public static final String GETBIDDING = BASE_URL+"/products/bidding/";
	  public static final String GETSELLING = BASE_URL+"/products/selling/";
	  public static final String GETWHATHOT = BASE_URL+"/products/whatshot";
	  public static final String ADD = BASE_URL+"/products";
	  public static final String UPDATE = BASE_URL+"/products";
	}
	
	public static class Categories {
	  public static final String GET = BASE_URL+ "/categories/";
	  public static final String GETALL = BASE_URL+ "/categories";
	}
	
	public static class CreditCards {
		public static final String GET = BASE_URL + "/cc/";
		public static final String GETALL = BASE_URL + "/cc";
		public static final String INSERT = BASE_URL + "/cc";
		public static final String UPDATE = BASE_URL + "/cc";
	}
	
	public static class Addresses {
		public static final String GET = BASE_URL + "/addresses";
		public static final String GETALL = BASE_URL + "/addresses";
		public static final String INSERT = BASE_URL + "/addresses";
		public static final String UPDATE = BASE_URL + "/addresses";
	}
	
	public static class OrderDetails {
		public static final String GETALL = BASE_URL + "/orderdetails";
	}
	
	public static class Orders{
		public static final String GETORDERS = BASE_URL+"/orders/";
		public static final String GETORDERDETAILS = BASE_URL+"/orders/details/";
		public static final String BIDS = BASE_URL+"/bids";
		public static final String CHANGE_TNUM = BASE_URL+"/tracking_number";
	}
	
	public static class Images {
		public static final String GET = BASE_URL+"/images/";
	}
	
	public static class Charts {
		public static final String SALES_BY_MONTH = BASE_URL+"/charts/sales/months";
		public static final String SALES_BY_WEEK = BASE_URL+"/charts/sales/weeks";
		public static final String SALES_BY_DAY = BASE_URL+"/charts/sales/days";
		public static final String PRODUCTS_BY_MONTH = BASE_URL+"/charts/sales/products/months";
		public static final String PRODUCTS_BY_WEEK = BASE_URL+"/charts/sales/products/weeks";
		public static final String PRODUCTS_BY_DAY = BASE_URL+"/charts/sales/products/days";
	}
	
	public static class Checkout {
		public static final String CART = BASE_URL+"/checkout/cart";
		public static final String BUY_IT_NOW = BASE_URL+"/checkout/now";
	}
}

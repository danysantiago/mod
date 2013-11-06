package icom5016.modstore.http;

public class Server {
	
	public static final String BASE_URL = "http://ec2-54-226-36-4.compute-1.amazonaws.com:8080";

	public static class User {
		public static final String LOGIN = BASE_URL+"/login";
	}
	public static class Products {
	  public static final String GET = BASE_URL+"/products/";
	  public static final String GETCATEGORIES = BASE_URL + "/products/category/";
	  public static final String GETALL = BASE_URL+"/products";
	}
	
	public static class Categories {
	  public static final String GET = BASE_URL+ "/categories/";
	  public static final String GETALL = BASE_URL+ "/categories";
	}
	
	public static class CreditCards {
		public static final String GET = BASE_URL + "/cc/";
		public static final String GETALL = BASE_URL + "/cc";
	}
	
	public static class Addresses {
		public static final String GET = BASE_URL + "/addresses/";
		public static final String GETALL = BASE_URL + "/addresses";
	}
	
	public static class OrderDetails {
		public static final String GETALL = BASE_URL + "/orderdetails";
	}
	
	public static class Orders{
		public static final String POSTBUYLIST = BASE_URL+"/ordersbuy";
		public static final String POSTSELLLIST = BASE_URL+"/orderssell";
		public static final String GETORDERS = BASE_URL+"/orders/";
	}
}

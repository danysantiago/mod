package icom5016.modstore.http;

public class Server {
	
	public static final String BASE_URL = "http://ec2-54-226-36-4.compute-1.amazonaws.com:8080";

	public static class Products {
	  public static final String GET = "/products/";
	}
	
	public static class Categories {
	  public static final String GET = "/categories/";
	}
}
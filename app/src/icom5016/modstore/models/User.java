package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	/* Instance Fields */
	private String username;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private int guid;
	private boolean isAdmin;
	//private String token;
	
	
	public User(String username, String firstName, String middleName, String lastName, String email, int guid ,boolean isAdmin) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.isAdmin = isAdmin;
		this.guid = guid;
	}
	
	public User(JSONObject jsonObject) throws JSONException {
		
			this.guid = jsonObject.getInt("user_id");
			this.username = jsonObject.getString("user_name");
			this.firstName = jsonObject.getString("first_name");
			this.middleName = jsonObject.getString("middle_name");
			this.lastName = jsonObject.getString("last_name");
			this.email = jsonObject.getString("email");
			int admin = jsonObject.getInt("is_admin");
			this.isAdmin = admin == 1;
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getGuid() {
		return guid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}

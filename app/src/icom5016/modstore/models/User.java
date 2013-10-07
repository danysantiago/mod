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
	
	public User(JSONObject jsonObject) {
		try {
			this.username = jsonObject.getString("username");
			this.firstName = jsonObject.getString("firstName");
			this.middleName = jsonObject.getString("middleName");
			this.lastName = jsonObject.getString("lastName");
			this.email = jsonObject.getString("email");
			this.isAdmin = jsonObject.getBoolean("isAdmin");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

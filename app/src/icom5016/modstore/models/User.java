package icom5016.modstore.models;

import icom5016.modstore.resources.AndroidResourceFactory;

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
	private boolean isDelete;
	
	
	public User(String username, String firstName, String middleName, String lastName, String email, int guid ,boolean isAdmin) {
		super();
		this.username = AndroidResourceFactory.stringEncode(username);
		this.firstName = AndroidResourceFactory.stringEncode(firstName);
		this.middleName = AndroidResourceFactory.stringEncode(middleName);
		this.lastName = AndroidResourceFactory.stringEncode(lastName);
		this.email = email;
		this.isAdmin = isAdmin;
		this.guid = guid;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User(JSONObject jsonObject) throws JSONException {
		this.guid = jsonObject.getInt("user_id");
		this.username = AndroidResourceFactory.stringEncode(jsonObject.getString("user_name"));
		this.firstName = AndroidResourceFactory.stringEncode(jsonObject.getString("first_name"));
		this.middleName = AndroidResourceFactory.stringEncode(jsonObject.getString("middle_name"));
		this.lastName = AndroidResourceFactory.stringEncode(jsonObject.getString("last_name"));
		this.email = jsonObject.getString("email");
		int admin = jsonObject.getInt("is_admin");
		this.isAdmin = admin == 1;
		
		if(jsonObject.has("deleted")) {
			int deleted = jsonObject.getInt("deleted");
			this.isDelete = deleted == 1;
		} else {
			isDelete = false;
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

	public boolean isDelete() {
		return isDelete;
	}

}

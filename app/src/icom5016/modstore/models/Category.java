package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
	
	private int parentId;
	private int id;
	private String name;
	
	
	public Category(JSONObject json) throws NumberFormatException, JSONException{
		this.parentId = json.getInt("parentId");
		this.id = json.getInt("id");
		this.name = json.getString("name");
	}
	
	
	
	public Category(int parentId, int id, String name) {
		super();
		this.parentId = parentId;
		this.id = id;
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
}

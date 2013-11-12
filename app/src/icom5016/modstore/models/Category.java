package icom5016.modstore.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
	
	private int parentId;
	private int id;
	private String name;
	
	//Optional Category
	private Category parent;
	
	
	
	public Category(JSONObject json) throws NumberFormatException, JSONException{
		this.parentId = json.getInt("parent_category_id");
		this.id = json.getInt("category_id");
		this.name = json.getString("name");
		
		if(json.has("parent"))
			this.parent = new Category(json.getJSONObject("parent"));
	}
	
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
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
	
	public String toString() {
		return getName();
	}
	public void setName(String name){
		this.name = name;
	}
}

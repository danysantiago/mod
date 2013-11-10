package icom5016.modstore.models;

import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCard {	
	private int creditcardId;
	private int addressId;
	private int type;
	private String number;
	private String name;
	private String expire;
	private String securityCode;
	private boolean isDefault;
	private String createdTs;
	
	
	//Optional
	private Address address;
	
	
	public CreditCard() {
		super();
	}
	
	public CreditCard(int cid, int aid, int type, String number, String name, String expire, String securityCode, boolean isDefault) {
		this.creditcardId = cid;
		this.type = type;
		this.number = number;
		this.name = name;
		this.expire = expire;
		this.securityCode = securityCode;
		this.isDefault = isDefault;
	}
	
	public CreditCard(JSONObject json) throws JSONException {
		
			this.creditcardId = json.getInt("creditcard_id");
			this.addressId = json.getInt("address_id");
			this.type = json.getInt("type");
			this.number = json.getString("number");
			this.name = json.getString("name");
			this.expire = json.getString("expiration_date");
			this.securityCode = json.getString("security_code");
			int is_default = json.getInt("is_primary");
			this.isDefault = is_default == 1;
			this.createdTs = json.getString("created_ts");
			
			if(json.has("address")){
				this.address = new Address(json.getJSONObject("address"));
			}
			
	}

	public String getTypeString(){
		return ConstantClass.CREDITCARD_LIST[this.type];
	}

	// Product, Order, Details, Credit Card
	public String getExpirationDateFormated(){
		//TODO: Format
		return this.expire;
	}
	
	public String getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}

	public int getCreditcardId() {
		return creditcardId;
	}

	public void setCreditcardId(int creditcardId) {
		this.creditcardId = creditcardId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return AndroidResourceFactory.stringEncode(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	
	
}

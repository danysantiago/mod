package icom5016.modstore.resources;

public class CreditCard {	
	
	int type; /* 0 = VISA, 1 = MASTERCARD */
	public String number;
	public String name;
	public String expire;
	
	public CreditCard() {
		super();
	}
	
	public CreditCard(int type, String number, String name, String expire) {
		this.type = type;
		this.number = number;
		this.name = name;
		this.expire = expire;
	}
}

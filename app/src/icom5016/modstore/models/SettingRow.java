package icom5016.modstore.models;

public class SettingRow {
	public String title;
	public String value;
	
	public SettingRow(String title, String value) {
		this.title = title;
		this.value = value;
	}
	
	public boolean showTitleOnly() {
		return value == null;
	}
}

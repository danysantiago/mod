package icom5016.modstore.resources;

import icom5016.modstore.models.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class AndroidResourceFactory {

	//Fragment Manager
	public static void setNewFragment(Activity activeActivity, Fragment fragment, int id){
		FragmentManager fragmentManager = activeActivity.getFragmentManager();
        fragmentManager.beginTransaction().replace(id, fragment).commit();
	}
	
	public static boolean validateEmail(String email){
		//TODO: Validate
		return true;
	}
	
	public static Date ISODateToDate(String date) {
		Date d;
		TimeZone tz = TimeZone.getTimeZone("UTC");
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
	    df.setTimeZone(tz);
	    
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			d = null;
		}

	    return d;
	}
	
	public static String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy KK:mma");
		return df.format(date);
	}
	
	public static List<Category> sortCategories(List<Category> unsorted) {
		List<Category> sorted = new ArrayList<Category>();
		getCategoriesRecurv(unsorted, sorted, 0, -1);
		return sorted;
	}
	
	private static void getCategoriesRecurv(List<Category> unsorted, List<Category> cats, int level, int lookId) {

		String name;
		
		for(Category e: unsorted){
			if (e.getParentId() == lookId){
				
				name = repeat("   ", level) + e.getName();
				cats.add(new Category(e.getParentId(), e.getId(), name));
				getCategoriesRecurv(unsorted, cats, level + 1,e.getId());
			}
		}
	}
	
	private static String repeat(String c, int n) {
		String out = "";
		
		for (int i = 0; i < n; i++) {
			out += c;
		}
		
		return out;
	}
	
}
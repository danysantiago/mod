package icom5016.modstore.resources;

import icom5016.modstore.models.Category;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class AndroidResourceFactory {

	//Fragment Manager
	public static void setNewFragment(FragmentActivity activeActivity, Fragment fragment, int id){
		FragmentManager fragmentManager = activeActivity.getSupportFragmentManager();
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
	
	public static String dateToStringNoDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
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
	
	/*public static String stringEncode(String text) {
		try {
			String s = new String(text.getBytes("ISO-8859-1"), "UTF-8");
			return s;
		} catch (UnsupportedEncodingException e) {
			Log.e("encode", e.getMessage());
			return text;
		}
	}*/
	
	public static String stringEncode(String text) {
		try {
			byte[] bytes = text.getBytes("ISO-8859-1");
			if (!validUTF8(bytes))
				return text;   
			
			return new String(bytes, "UTF-8");  
		} catch (UnsupportedEncodingException e) {
			Log.e("encode", e.getMessage());
			return text;
		}
	}

	// From Internet!
		 public static boolean validUTF8(byte[] input) {
		  int i = 0;
		  // Check for BOM
		  if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
		    && (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
		   i = 3;
		  }

		  int end;
		  for (int j = input.length; i < j; ++i) {
		   int octet = input[i];
		   if ((octet & 0x80) == 0) {
		    continue; // ASCII
		   }

		   // Check for UTF-8 leading byte
		   if ((octet & 0xE0) == 0xC0) {
		    end = i + 1;
		   } else if ((octet & 0xF0) == 0xE0) {
		    end = i + 2;
		   } else if ((octet & 0xF8) == 0xF0) {
		    end = i + 3;
		   } else {
		    // Java only supports BMP so 3 is max
		    return false;
		   }

		   while (i < end) {
		    i++;
		    octet = input[i];
		    if ((octet & 0xC0) != 0x80) {
		     // Not a valid trailing byte
		     return false;
		    }
		   }
		  }
		  return true;
		 }
}
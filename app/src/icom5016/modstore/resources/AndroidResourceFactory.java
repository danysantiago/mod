package icom5016.modstore.resources;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
	
	public static String stringEncode(String text) {
		try {
			return new String(text.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return text;
		}
	}
}
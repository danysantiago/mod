package icom5016.modstore.resources;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * This class handles the file cache used in the app.
 * TODO: Consider implementing size limit
 * @author Daniel Santiago
 */
public class FileCache {
	
	//Directory for cache files
	//dot as first character means hidden folder (Android = Unix)
	public static final String cacheDirName = ".mod";
	
	private File cacheDir;

	//Constructor
	public FileCache(Context context){
		//Get directory, create one if there is none
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),cacheDirName);
        else
            cacheDir = context.getCacheDir();
		
        if(!cacheDir.exists())
            cacheDir.mkdirs();
	}
	
	public File getCacheDir(){
		return cacheDir;
	}

	public Bitmap get(String url) {
		//Get bitmap image saved as hash code from url
		String fileName = String.valueOf(url.hashCode());
		File cacheFile = new File(cacheDir, fileName);
		
		return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
	}

}

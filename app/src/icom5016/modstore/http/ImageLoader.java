package icom5016.modstore.http;



import icom5016.modstore.resources.FileCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

/**
 * Class used to "lazy load" the images into a list view.
 * A simple SD cache has been used (FileCache class)
 * @author Daniel Santiago 
 */
public class ImageLoader {

	private FileCache fileCache;
	
	public ImageLoader(Context context){
		fileCache = new FileCache(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void DisplayImage(String url, ImageView imageView) {
		this.DisplayImage(url, imageView, true);
	}

	public void DisplayImage(String url, ImageView imageView, boolean cacheEnabled) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//For Gingerbread, sadly serial download only
			(new ImageGet(url, imageView, cacheEnabled)).execute();
		} else {
			//For HONEYCOMB or up, we have multi-thread downloads =)
			(new ImageGet(url, imageView, cacheEnabled)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	//ASyncTask that does the actual downloading and saving of file, if new
	private class ImageGet extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private ImageView imageView;
		private boolean cacheEnabled;

		public ImageGet(String url, ImageView imageView, boolean cacheEnabled) {
			this.url = url;
			this.imageView = imageView;
			this.cacheEnabled = cacheEnabled;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			// Try to get bitmap from cache
			Bitmap bitmap = null;
			if(cacheEnabled) {
				bitmap = fileCache.get(url);
			}
			
			if (bitmap != null) {
				//Load it if found
				return bitmap;
			} else {
				//If not, download it
				try {
					
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					
					//Check http status
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode != HttpStatus.SC_OK) {
						// Error, status code did not return 200
						return null;
					}
					
					//Get bitmap entity
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream inputStream = null;
						try {
							inputStream = entity.getContent();
							bitmap = BitmapFactory.decodeStream(inputStream);
							
							//Save bitmap in cache
							String fileName = String.valueOf(url.hashCode());
							FileOutputStream fout = new FileOutputStream(new File(fileCache.getCacheDir(), fileName));
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
							
							return bitmap;
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// Another error, no image was given by url get
						return null;
					}
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
		}

	}


}

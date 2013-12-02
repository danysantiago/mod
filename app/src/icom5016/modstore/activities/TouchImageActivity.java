package icom5016.modstore.activities;

import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.views.TouchImageView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TouchImageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_image_view);
		TouchImageView img = (TouchImageView) findViewById(R.id.imageView);
		img.setMaxZoom(4f);
		
		//Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
		
		ImageLoader imageLoader = new ImageLoader(this);
		imageLoader.DisplayImage(getIntent().getExtras().getString("imageUrl"), img, false);
	}
}

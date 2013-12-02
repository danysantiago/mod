package icom5016.modstore.adapter;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Product;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WhatsHotAdapter extends ArrayAdapter<Product> {
	private ImageLoader imageloader;

	public WhatsHotAdapter(Context context, JSONArray jsonArr) throws JSONException {
		super(context, R.layout.listview_whatshot);
		
		imageloader = new ImageLoader(context);
		
		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new Product(jsonArr.getJSONObject(i)));
		}
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		Product product = (Product) this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_whatshot, parent, false);
		
		TextView index = (TextView) row.findViewById(R.id.whrow_index);
		index.setText(Integer.toString(position+1)+".");
		
		ImageView image = (ImageView) row.findViewById(R.id.whrow_image);
		
		TextView title = (TextView) row.findViewById(R.id.whrow_title);
		
		title.setText(product.getName());
		
		if (product.getImageSrcUrl() != null) {
			String url = Server.Images.GET + product.getImageSrcUrl();

			imageloader.DisplayImage(url, image);
			image.setTag(url);
		}

		row.setTag(product);

		return row;
	}
}

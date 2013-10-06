package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
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

public class ProductAdapter extends ArrayAdapter<Product> {
		
		private ImageLoader imageloader;

		public ProductAdapter(Context context, JSONArray jsonArr) throws JSONException {
			super(context, R.layout.listview_product_row_1);
			
			imageloader = new ImageLoader(context);
			
			for(int i = 0; i < jsonArr.length(); i++) {
				this.add(new Product(jsonArr.getJSONObject(i)));
			}
			
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			Product product = this.getItem(position);

			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
			
			row = inflater.inflate(R.layout.listview_product_row_1, parent, false);

			TextView name = (TextView) row.findViewById(R.id.name_textView);
			name.setText(product.getName());

			TextView description = (TextView) row.findViewById(R.id.description_textView);
			description.setText(product.getDescription());

			TextView price = (TextView) row.findViewById(R.id.price_textView);
			price.setText(product.getPrice());
			
			ImageView image = (ImageView) row.findViewById(R.id.imageView);
			imageloader.DisplayImage("http://files.gamebanana.com/img/ico/sprays/1up_orcaexample.png", image);
			
			row.setTag(product);

			return row;
		}
	}
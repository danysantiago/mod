package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.ProductSearching;

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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class ProductAdapter extends ArrayAdapter<Product> {
		
		private ImageLoader imageloader;

		public ProductAdapter(Context context, JSONArray jsonArr) throws JSONException {
			super(context, R.layout.listview_product);
			
			imageloader = new ImageLoader(context);
			
			for(int i = 0; i < jsonArr.length(); i++) {
				this.add(new ProductSearching(jsonArr.getJSONObject(i)));
			}
			
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			ProductSearching product = (ProductSearching) this.getItem(position);

			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
			
			row = inflater.inflate(R.layout.listview_product, parent, false);
			
			//Layout Vars
			TextView title = (TextView) row.findViewById(R.id.prodrow_title);
			ImageView image = (ImageView) row.findViewById(R.id.prodrow_image);
			TextView price = (TextView) row.findViewById(R.id.prodrow_price);
			TextView date = (TextView) row.findViewById(R.id.prodrow_date);
			TextView type = (TextView) row.findViewById(R.id.prodrow_type);
			
			if(product.getStartingBidPrice() != -1.0){
				type.setText("Bid Now");
				price.setText(product.getActualBidString());
				date.setText(product.getAuctionEndsTsString());
			}
			else{
				type.setText("Buy It Now");
				price.setText(product.getBuyItNowPriceString());
				date.setVisibility(View.INVISIBLE);
				row.findViewById(R.id.prodrow_date_tittle).setVisibility(View.GONE);
			}
			
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
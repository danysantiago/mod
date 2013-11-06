package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.models.ProductSelling;
import icom5016.modstore.resources.ConstantClass;

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

public class SellingListAdapter extends ArrayAdapter<ProductSelling> {

	private ImageLoader imageloader;
	private String type;

	public SellingListAdapter(Context context, JSONArray list, String type ) throws JSONException {
		super(context, R.layout.listview_orderdetails_row);
		this.type = type;
		imageloader = new ImageLoader(context);
		
		for(int i=0; i<list.length(); i++){
			this.add(new ProductSelling(list.getJSONObject(i)));
		}
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ProductSelling product = this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_orderdetails_row, parent, false);
		
		//Layout Vars
		TextView title = (TextView) row.findViewById(R.id.prodrow_orders_title);
		ImageView image = (ImageView) row.findViewById(R.id.prodrow_orders_image);
		TextView quantity = (TextView) row.findViewById(R.id.prodrow_orders_quantity);
		TextView tracking = (TextView) row.findViewById(R.id.prodrow_orders_tracking);
		TextView price = (TextView) row.findViewById(R.id.prodrow_orders_price);
		TextView date = (TextView) row.findViewById(R.id.prodrow_orders_date);
		TextView type = (TextView) row.findViewById(R.id.prodrow_orders_type);
		
		//Type
		type.setText(product.getSellType());
		
		//Title
		title.setText(product.getName());
		
		//Quantity
		quantity.setText(product.getQuantity());

		/*if(this.type.equals(ConstantClass.PRODUCT_ACTIVE))
			date.setText("End Date: "+product.getAuction_ends());
		else if(this.type.equals(ConstantClass.PRODUCT_SOLD)){
			date.setText("Sold");
		}*/
		
		//Tracking
		tracking.setVisibility(View.GONE);
		
		//Price
		if(product.getSellType().equals(ProductSelling.BUY_NOW))
		{
			price.setText(product.getPrice());
		}
		else if(product.getSellType().equals(ProductSelling.BID)){
			price.setText(product.getBid());
		}
		
		//Images
		imageloader.DisplayImage(product.getImage_src(), image);
		
		row.setTag(product);

		return row;
	}

}

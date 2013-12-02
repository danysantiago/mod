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

public class ProductDetailsAdapter extends ArrayAdapter<Product> {
	
	private ImageLoader imageloader;
	Context context;
	int layoutResourceId;
	boolean itemsSold;

	public ProductDetailsAdapter(Context context, int layoutResourceId, JSONArray jsonArr, boolean itemsSold) throws JSONException {
		super(context, R.layout.listview_product_row_2);
		
		imageloader = new ImageLoader(context);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.itemsSold = itemsSold;
		
		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new Product(jsonArr.getJSONObject(i)));
		}
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new ProductHolder();
            holder.imgThumbnail = (ImageView)row.findViewById(R.id.imageView5);
            holder.txtName = (TextView)row.findViewById(R.id.name_textView2);
            holder.txtDescription = (TextView)row.findViewById(R.id.description_textView2);
            holder.txtPrice = (TextView)row.findViewById(R.id.price_textView2);
            holder.txtEndingOn = (TextView)row.findViewById(R.id.txtProductEndingOn);
            holder.txtBid = (TextView)row.findViewById(R.id.txtProductBid);
            holder.txtQuantity = (TextView)row.findViewById(R.id.txtProductQuantity2);
            holder.lblBid = (TextView)row.findViewById(R.id.lblProductBid);
           
            row.setTag(holder);
        } else {
            holder = (ProductHolder)row.getTag();
        }
       
        Product product = this.getItem(position);
        
        imageloader.DisplayImage("http://files.gamebanana.com/img/ico/sprays/1up_orcaexample.png", holder.imgThumbnail);
        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.txtPrice.setText(product.getBuyItNowPriceString());
        holder.txtEndingOn.setText(product.getAuctionEndsTs());
        holder.txtQuantity.setText(String.valueOf(product.getQuantity()));
        
        if (product.getBuyItNowPrice() != -1) {
        	holder.lblBid.setVisibility(View.VISIBLE);
        	holder.txtBid.setVisibility(View.VISIBLE);
        	
        	holder.txtBid.setText(Double.toString(product.getStartingBidPrice()));
        	
        	if (itemsSold)
        		holder.lblBid.setText(R.string.label_endedon);
        } else {
        	holder.lblBid.setVisibility(View.GONE);
        	holder.txtBid.setVisibility(View.GONE);
        }
        
        return row;
    }
   
    static class ProductHolder {
    	ImageView imgThumbnail;
        TextView txtName;
        TextView txtDescription;
        TextView txtPrice;
        TextView txtEndingOn;
        TextView txtBid;
        TextView txtQuantity;
        TextView lblBid;
    }
}
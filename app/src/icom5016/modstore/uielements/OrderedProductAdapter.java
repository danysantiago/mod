package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.models.OrderDetail;

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

public class OrderedProductAdapter extends ArrayAdapter<OrderDetail> {
	
	private ImageLoader imageloader;
	Context context;
	int layoutResourceId;

	public OrderedProductAdapter(Context context, int layoutResourceId, JSONArray jsonArr) throws JSONException {
		super(context, R.layout.listview_product_row_2);
		
		imageloader = new ImageLoader(context);
		this.layoutResourceId = layoutResourceId;
		this.context = context;

		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new OrderDetail(jsonArr.getJSONObject(i)));
		}
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductODHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new ProductODHolder();
            holder.imgThumbnail = (ImageView)row.findViewById(R.id.imageView6);
            holder.txtName = (TextView)row.findViewById(R.id.name_textView3);
            holder.txtDescription = (TextView)row.findViewById(R.id.description_textView3);
            holder.txtPrice = (TextView)row.findViewById(R.id.price_textView3);
            holder.txtQuantity = (TextView)row.findViewById(R.id.txtProductQuantity3);
            holder.imgShipped = (ImageView)row.findViewById(R.id.imgProductShipped);
            holder.txtTrackingNumber = (TextView)row.findViewById(R.id.txtProductTrackingNum);
           
            row.setTag(holder);
        } else {
            holder = (ProductODHolder)row.getTag();
        }
       
        OrderDetail orderDetail = this.getItem(position);
        
        imageloader.DisplayImage("http://files.gamebanana.com/img/ico/sprays/1up_orcaexample.png", holder.imgThumbnail);
        holder.txtName.setText(orderDetail.getProduct().getName());
        holder.txtDescription.setText(orderDetail.getProduct().getDescription());
        holder.txtPrice.setText(orderDetail.getPrice());
        holder.txtQuantity.setText(String.valueOf(orderDetail.getQuantity()));
        
        if (!orderDetail.getTrackingNumber().isEmpty()) {
        	holder.imgShipped.setVisibility(View.VISIBLE);
        	holder.txtTrackingNumber.setVisibility(View.VISIBLE);
        	holder.txtTrackingNumber.setText(orderDetail.getTrackingNumber());
        } else {
        	holder.imgShipped.setVisibility(View.GONE);
        	holder.txtTrackingNumber.setVisibility(View.GONE);
        }
        
        return row;
    }
   
    static class ProductODHolder {
    	ImageView imgThumbnail;
        TextView txtName;
        TextView txtDescription;
        TextView txtPrice;
        TextView txtQuantity;
        
    	ImageView imgShipped;
        TextView txtTrackingNumber;
    }
}
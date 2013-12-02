package icom5016.modstore.adapters;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Category;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductListSpinnerAdapter extends ArrayAdapter<Category> {
	
	public ProductListSpinnerAdapter(Context context, List<Category> list) throws JSONException {
		super(context, R.layout.listview_product_list_spinner);
		this.addAll(list);
	}
	
	  @Override
	    public View getDropDownView(int position, View convertView,ViewGroup parent) {
	        return getCustomView(position, convertView, parent);
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        return getCustomView(position, convertView, parent);
	    }
	
	    
	    public View getCustomView(int position, View row, ViewGroup parent) {

		Category category = this.getItem(position);
		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		row = inflater.inflate(R.layout.listview_product_list_spinner, parent, false);

		TextView name = (TextView) row.findViewById(R.id.plSpinnerTextView);
		name.setText(category.getName());

		row.setTag(category);

		return row;
	}

}

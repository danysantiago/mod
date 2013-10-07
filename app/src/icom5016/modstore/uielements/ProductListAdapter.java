package icom5016.modstore.uielements;

import org.json.JSONArray;
import org.json.JSONException;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Category;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Category> {
	
	public ProductListAdapter(Context context, JSONArray jsonArr) throws JSONException {
		super(context, R.layout.listview_product_list_spinner);
		
		//Add Rows
		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new Category(jsonArr.getJSONObject(i)));
		}
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

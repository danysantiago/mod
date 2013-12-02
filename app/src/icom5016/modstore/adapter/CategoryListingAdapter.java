package icom5016.modstore.adapter;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Category;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryListingAdapter extends ArrayAdapter<Category> {

	public CategoryListingAdapter(Context context, List<Category> category) {
		super(context, R.layout.listview_catlisting);
		this.addAll(category);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		Category category = (Category) this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_catlisting, parent, false);
		
		TextView tv = (TextView) row.findViewById(R.id.catrow_title);
		tv.setText(category.getName());
		
		return row;
	}

}

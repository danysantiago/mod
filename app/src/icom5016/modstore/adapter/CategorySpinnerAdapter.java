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

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

	public CategorySpinnerAdapter(Context context, int resource, List<Category> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		Category cat = this.getItem(position);

		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		
		row = inflater.inflate(R.layout.listview_filter_spinner, parent, false);
		
		TextView tv = (TextView) row.findViewById(R.id.search_filter_spinner);
		tv.setText(cat.getName());
		
		row.setTag(cat);

		return row;
	}
}

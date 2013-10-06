package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Category;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends ArrayAdapter<Category> {

	public CategoryListAdapter(Context context, JSONArray jsonArr) throws JSONException {
		super(context, R.layout.listview_categories_row);
		
		//Add Rows
		for(int i = 0; i < jsonArr.length(); i++) {
			this.add(new Category(jsonArr.getJSONObject(i)));
		}
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {

		Category category = this.getItem(position);
		LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		row = inflater.inflate(R.layout.listview_categories_row, parent, false);

		TextView name = (TextView) row.findViewById(R.id.categoryListRowText);
		name.setText(category.getName());

		row.setTag(category);

		return row;
	}
	
	
}

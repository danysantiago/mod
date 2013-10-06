package icom5016.modstore.uielements;

import org.json.JSONArray;

import icom5016.modstore.activities.R;
import android.content.Context;
import android.widget.ArrayAdapter;

public class CategoryListAdapter extends ArrayAdapter<String> {

	public CategoryListAdapter(Context context, JSONArray jsonArr) {
		super(context, R.layout.listview_product_row_1);
		// TODO Auto-generated constructor stub
	}

}

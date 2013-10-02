package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainCategoryFragment extends Fragment {
	//Intances
	private String categoryTitle;
	private String[] subCategories;

	
	public MainCategoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		
		this.categoryTitle = getArguments().getString(ConstantClass.MAINCATEGORY_FRAGMENT_CATEGORY_KEY);
		this.subCategories = DataFetchFactory.fetchSubCategories(this.categoryTitle);
		View view = inflater.inflate(R.layout.fragment_maincategory, container,false);
		//Change Text View for Demo Purpose
		TextView textView = (TextView) view.findViewById(R.id.textview_maincategory);
		textView.setText(categoryTitle+"\n\nSubcategories: "+this.subCategories[0]+" "+this.subCategories[1]+" "+this.subCategories[2]+" "+this.subCategories[3]);
		return view;
	}
	
}

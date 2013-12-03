package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.CategoryListingAdapter;
import icom5016.modstore.listeners.CategoryListingListener;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.ConstantClass;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListFragment extends Fragment {
	
	private ListView catlistLv;
	private MainActivity ma;
	private Bundle params;
	private List<Category> listingCat;
	private TextView catlistTv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_catlist, container,false);
		
		this.catlistLv = (ListView) view.findViewById(R.id.catlist_lv);
		this.catlistTv = (TextView) view.findViewById(R.id.catlist_tv);
		this.ma = (MainActivity) this.getActivity();
		this.params = this.getArguments();
	
		this.loadCategories(this.params);
		return view;
	}
	
	
	private void loadCategories(Bundle bundle){
		//Http Load
		ma.loadAllCategories();
		
		int parentId = bundle.getInt(ConstantClass.CATEGORY_LIST_PARENT_KEY);
		
		this.listingCat = ma.loadCategoriesById(parentId);
		
		if(parentId != -1){
			Category parent = ma.loadCategoryById(parentId);
			catlistTv.setText("Subcategories in "+parent.getName()+":");
		}
		if(this.listingCat.size() == 0){ //Mean No SubCategories Enter Listing Fragment
			ProductListingFragment plf = new ProductListingFragment();
			plf.setArguments(this.getProductParams(parentId));
			this.ma.fragmentStack.pop();
			this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), plf);
			return;
		}
		
		this.catlistLv.setAdapter(new CategoryListingAdapter(ma, listingCat));
		this.catlistLv.setOnItemClickListener(new CategoryListingListener(ma));
		this.catlistLv.setVisibility(View.VISIBLE);
		
		
		
	}


	private Bundle getProductParams(int parentId) {
		Bundle ret = new Bundle();
		ret.putString(ConstantClass.SEARCH_DIALOG_CATEGORIES_ID_KEY, Integer.toString(parentId));
		return ret;
	}
}

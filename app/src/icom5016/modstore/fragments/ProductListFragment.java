package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class ProductListFragment extends Fragment {
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_about, container,false);
		return view;
	}
	
	// Intances
		private int categoryID;
		private ProgressBar productListProgressBar;
		private ListView productListView;
		private Spinner subCategorySpinner;
		private ImageView productListPlaceHolder;
		private LinearLayout productListLayout;

		public ProductListFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_product_list, container, false);
			
			this.productListProgressBar = (ProgressBar) view.findViewById(R.id.productListProgressBar);
			this.productListPlaceHolder = (ImageView) view.findViewById(R.id.placehoderProductList);
			this.productListLayout = (LinearLayout) view.findViewById(R.id.productLinearLayout);
			
			return view;
		}

}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;

import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainFragment extends Fragment {

	private ListView list1;
	private ListView list2;
	
	Random r = new Random();
	
	public MainFragment(){
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_main, container,false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		Product[] pList = new Product[4];
		pList[0] = new Product(0, 2, 2, "A Product 1", "Some lazy short description,", "Shiny Inc.", "Star12", "2x3x2", 20.99, 1, -1, "", null);
		pList[1] = new Product(1, 2, 2, "1993 Useless Rock", "A wonderful and hard rock, dated to 1993.", "Rocky", "1bdg35adc", "2.2x1.2x1.4", 99.99, 1, -1, "", null);
		pList[2] = new Product(2, 2, 2, "A Cat", "Just a cat, found on the street, please feed it well.", "Da Street", "v1", "Varies", 10.00, 1, -1, "", null);
		pList[3] = new Product(3, 2, 2, "Another Product", "Some lazy short description for another product", "Awesome P", "A129", "1x1x1", 120.00, 1, -1, "", null);
		
		list1 = (ListView) view.findViewById(R.id.listView1);
		MainAdapter adapter1 = new MainAdapter(getActivity(), R.layout.listview_home_row_2);
		for(int i = 0; i < 10; i++) {
			adapter1.add(pList[r.nextInt(4)]);
		}
		list1.setAdapter(adapter1);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				MainInterfaceActivity activity = (MainInterfaceActivity) getActivity();
				Product product = (Product) list1.getAdapter().getItem(position);
				ProductFragment pf = ProductFragment.getInstance(product);
				activity.fragmentStack.push(pf);
				AndroidResourceFactory.setNewFragment(activity, activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());
			}
		});
		
		list2 = (ListView) view.findViewById(R.id.listView2);
		MainAdapter adapter2 = new MainAdapter(getActivity(), R.layout.listview_home_row_2);
		for(int i = 0; i < 10; i++) {
			adapter2.add(pList[r.nextInt(4)]);
		}
		list2.setAdapter(adapter2);
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				MainInterfaceActivity activity = (MainInterfaceActivity) getActivity();
				Product product = (Product) list2.getAdapter().getItem(position);
				ProductFragment pf = ProductFragment.getInstance(product);
				activity.fragmentStack.push(pf);
				AndroidResourceFactory.setNewFragment(activity, activity.fragmentStack.peek(), MainInterfaceActivity.getContentFragmentId());				
			}
		});

	}
	
	public class MainAdapter extends ArrayAdapter<Product> {
		
		int[] images = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4};

		public MainAdapter(Context context, int resource) {
			super(context, resource);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();

			View view = inflater.inflate(R.layout.listview_home_row_2, parent,false);
			TextView productNameTV = (TextView) view.findViewById(R.id.productNameTextView);
			TextView priceTV = (TextView) view.findViewById(R.id.priceTextView);
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			
			Product p = this.getItem(position);
			
			productNameTV.setText(p.getName());
			priceTV.setText(p.getPrice());
			imageView.setImageResource(images[p.getPid()]);
			
			
			return view;
		}
		
	}
}

package icom5016.modstore.fragments;

import java.util.Random;

import icom5016.modstore.activities.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends Fragment {

	private ListView list;
	
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
		
		list = (ListView) view.findViewById(R.id.listView1);
		
		MainAdapter adapter = new MainAdapter(getActivity(), R.layout.listview_home_row_2);
		for(int i = 0; i < 10; i++) {
			adapter.add(r.nextInt(2));
		}
		
		list.setAdapter(adapter);

	}
	
	public class MainAdapter extends ArrayAdapter<Integer> {

		public MainAdapter(Context context, int resource) {
			super(context, resource);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();

			View view;
			
			if(this.getItem(position) == 0) {
				view = inflater.inflate(R.layout.listview_home_row_1, parent,false);
			} else {
				view = inflater.inflate(R.layout.listview_home_row_2, parent,false);
			}
			
			return view;
		}
		
	}
}

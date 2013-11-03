package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;

public class SellingListFragment extends Fragment {
	//User Instance Field
			private User activeUser;
			private MainInterfaceActivity mainActivity;
			private Spinner spinner;
			private View mainLayout;
			private ScrollView sv_container;
			
			
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState){
				View view = inflater.inflate(R.layout.fragment_spinner_listing, container,false);
				
				//Instance Vars
				this.mainActivity = (MainInterfaceActivity) this.getActivity();
				this.activeUser = this.mainActivity.getActiveUser();
				this.spinner = (Spinner) view.findViewById(R.id.spinner_buysell_frag);
				this.mainLayout = view;
				this.sv_container = (ScrollView) view.findViewById(R.id.buysell_container);
				
				//Setup Spinner
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity, R.layout.spinner_buysell_list, ConstantClass.SELLING_SPINNER);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				this.spinner.setAdapter(adapter);
				
				
				//Set Spinner Listener
				this.spinner.setOnItemSelectedListener(new OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							
							//Inflater
							LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							ViewGroup parent_view = (ViewGroup) mainLayout.findViewById(R.id.buysell_container);
							sv_container.removeAllViews();
							
							switch(pos){
							case 0:
								inflater.inflate(R.layout.selling_all_listing, parent_view);
								break;
							case 1:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								break;
							case 2:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								break;
							default:
								inflater.inflate(R.layout.buying_all_listing, parent_view);
								break;
							
							}
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							//NoOp
							
						}
				
					}
				);
				
				return view;
			}
}
package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.SellingListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.unused.BuySellListListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
								try {
									doHttpSellingList(true, true);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							case 1:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								try {
									doHttpSellingList(true, false);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							case 2:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								try {
									doHttpSellingList(false, true);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							default:
								inflater.inflate(R.layout.buying_all_listing, parent_view);
								try {
									doHttpSellingList(true, true);
								} catch (JSONException e) {
									e.printStackTrace();
								}
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


			private void doHttpSellingList(boolean active, boolean sold) throws JSONException{
				Bundle params = new Bundle();
				params.putString("url", Server.Products.GETSELLING+"?userId="+this.activeUser.getGuid()+
						"&active="+ Boolean.toString(active) +"&sold="+Boolean.toString(sold));
				params.putString("method", "GET");
				params.putString("method", "GET");
				
				
				HttpRequest request = new HttpRequest(params, new HttpCallback() {
					
					@Override
					public void onSucess(JSONObject json) {
						try {
							JSONArray activeList = json.getJSONArray("active_list");
							JSONArray soldList = json.getJSONArray("sold_list");
							if(activeList.length()>0 && soldList.length()>0){
								
								//Sold Var
								ProgressBar pbSold = (ProgressBar) mainLayout.findViewById(R.id.sell_sold_pb);
								TextView tvSold = (TextView) mainLayout.findViewById(R.id.sell_sold_load_tv);
								GridView gvSold = (GridView) mainLayout.findViewById(R.id.sell_sold_loader);
								ListView lvSold = (ListView) mainLayout.findViewById(R.id.sell_sold_lv);
								
								
								if(soldList.length() == 0){
									pbSold.setVisibility(View.GONE);
									tvSold.setText(R.string.listbuysell_no_item);
								}
								else{
									gvSold.setVisibility(View.GONE);
									lvSold.setAdapter(new SellingListAdapter(mainActivity, soldList, ConstantClass.SELLING_SOLD ));
									lvSold.setOnItemClickListener(new BuySellListListener(mainActivity));
									lvSold.setVisibility(View.VISIBLE);
								}
								
								//Active Var
								ProgressBar pbActive = (ProgressBar) mainLayout.findViewById(R.id.sell_active_pb);
								TextView tvActive = (TextView) mainLayout.findViewById(R.id.sell_active_load_tv);
								GridView gvActive = (GridView) mainLayout.findViewById(R.id.sell_active_loader);
								ListView lvActive = (ListView) mainLayout.findViewById(R.id.sell_active_lv);
								
								
								if(activeList.length() == 0){
									pbActive.setVisibility(View.GONE);
									tvActive.setText(R.string.listbuysell_no_item);
								}
								else{
									gvActive.setVisibility(View.GONE);
									lvActive.setAdapter(new SellingListAdapter(mainActivity, activeList, ConstantClass.SELLING_ACTIVE));
									lvActive.setOnItemClickListener(new BuySellListListener(mainActivity));
									lvActive.setVisibility(View.VISIBLE);
								}
								
								
							}
							else if(soldList.length() > 0){
								ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
								TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
								ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
								pbIndividual.setVisibility(View.GONE);
								if(soldList.length() == 0){
									tvIndividual.setVisibility(View.VISIBLE);
								}
								else{
									lvIndividual.setAdapter(new SellingListAdapter(mainActivity, soldList, ConstantClass.SELLING_SOLD));
									lvIndividual.setOnItemClickListener(new BuySellListListener(mainActivity));
									lvIndividual.setVisibility(View.VISIBLE);
								}
								
								

							}
							else if(activeList.length() > 0){
								ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
								TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
								ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
								
								pbIndividual.setVisibility(View.GONE);
								if(activeList.length() == 0){
									tvIndividual.setVisibility(View.VISIBLE);
								}
								else{
									lvIndividual.setAdapter(new SellingListAdapter(mainActivity, activeList, ConstantClass.SELLING_ACTIVE));
									lvIndividual.setOnItemClickListener(new BuySellListListener(mainActivity));
									lvIndividual.setVisibility(View.VISIBLE);
								}
							}
							
							
						} catch (JSONException e) {
							Toast.makeText(mainActivity, R.string.errmsg_bad_json,
									Toast.LENGTH_SHORT).show();
						}
						
					}
					
					@Override
					public void onFailed() {
						Toast.makeText(mainActivity, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onDone() {
						//No-Op
					}
				});
				request.execute();
			    
			}
}
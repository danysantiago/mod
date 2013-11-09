package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.SellingListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.SellingListListener;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;

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
import android.widget.GridLayout;
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
									doHttpSellingList(true, true, true);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							case 1:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								try {
									doHttpSellingList(true, false, false);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							case 2:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								try {
									doHttpSellingList(false, true, false);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							case 3:
								inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
								try {
									doHttpSellingList(false, false, true);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;
							default:
								inflater.inflate(R.layout.buying_all_listing, parent_view);
								try {
									doHttpSellingList(true, true, true);
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


			private void doHttpSellingList(boolean active, boolean sold, boolean notsold) throws JSONException{
				Bundle params = new Bundle();
				params.putString("url", Server.Products.GETSELLING+"?userId="+this.activeUser.getGuid()+
						"&active="+ Boolean.toString(active) +"&sold="+Boolean.toString(sold)+
						 "&not_won="+Boolean.toString(notsold));
				params.putString("method", "GET");
				
				
				HttpRequest request = new HttpRequest(params, new HttpCallback() {
					
					@Override
					public void onSucess(JSONObject json) {
						try {
							
							if(json.has("sold") && json.has("not_won") && json.has("active")){
								
								//Sold Var
								JSONArray soldList = json.getJSONArray("sold");
								ProgressBar pbSold = (ProgressBar) mainLayout.findViewById(R.id.sell_sold_pb);
								TextView tvSold = (TextView) mainLayout.findViewById(R.id.sell_sold_load_tv);
								GridLayout gvSold = (GridLayout) mainLayout.findViewById(R.id.sell_sold_loader);
								ListView lvSold = (ListView) mainLayout.findViewById(R.id.sell_sold_lv);
								
								
								if(soldList.length() == 0){
									pbSold.setVisibility(View.GONE);
									tvSold.setText(R.string.listbuysell_no_item);
								}
								else{
									gvSold.setVisibility(View.GONE);
									lvSold.setAdapter(new SellingListAdapter(mainActivity, soldList, ConstantClass.SELLING_SOLD ));
									lvSold.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_SOLD));
									lvSold.setVisibility(View.VISIBLE);
								}
								
								//Active Var
								JSONArray activeList = json.getJSONArray("active");
								ProgressBar pbActive = (ProgressBar) mainLayout.findViewById(R.id.sell_active_pb);
								TextView tvActive = (TextView) mainLayout.findViewById(R.id.sell_active_load_tv);
								GridLayout gvActive = (GridLayout) mainLayout.findViewById(R.id.sell_active_loader);
								ListView lvActive = (ListView) mainLayout.findViewById(R.id.sell_active_lv);
								
								
								if(activeList.length() == 0){
									pbActive.setVisibility(View.GONE);
									tvActive.setText(R.string.listbuysell_no_item);
								}
								else{
									gvActive.setVisibility(View.GONE);
									lvActive.setAdapter(new SellingListAdapter(mainActivity, activeList, ConstantClass.SELLING_ACTIVE));
									lvActive.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_ACTIVE));
									lvActive.setVisibility(View.VISIBLE);
								}
								
								//Not Sold List
								JSONArray notsoldList = json.getJSONArray("not_won");
								ProgressBar pbNotSold = (ProgressBar) mainLayout.findViewById(R.id.sell_notsold_pb);
								TextView tvNotSold = (TextView) mainLayout.findViewById(R.id.sell_notsold_load_tv);
								GridLayout gvNotSold = (GridLayout) mainLayout.findViewById(R.id.sell_notsold_loader);
								ListView lvNotSold = (ListView) mainLayout.findViewById(R.id.sell_notsold_lv);
								
								
								if(notsoldList.length() == 0){
									pbNotSold.setVisibility(View.GONE);
									tvNotSold.setText(R.string.listbuysell_no_item);
								}
								else{
									gvNotSold.setVisibility(View.GONE);
									lvNotSold.setAdapter(new SellingListAdapter(mainActivity, notsoldList, ConstantClass.SELLING_NOTSOLD));
									lvNotSold.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_NOTSOLD));
									lvNotSold.setVisibility(View.VISIBLE);
								}
								
								
								
							}
							else if(json.has("sold")){
								
								JSONArray soldList = json.getJSONArray("sold");
								ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
								TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
								ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
								GridLayout gvIndividual = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
								pbIndividual.setVisibility(View.GONE);
								if(soldList.length() == 0){
									tvIndividual.setVisibility(View.VISIBLE);
								}
								else{
									gvIndividual.setVisibility(View.GONE);
									lvIndividual.setAdapter(new SellingListAdapter(mainActivity, soldList, ConstantClass.SELLING_SOLD));
									lvIndividual.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_SOLD));
									lvIndividual.setVisibility(View.VISIBLE);
								}
								
								

							}
							else if(json.has("active")){
								JSONArray activeList = json.getJSONArray("active");
								ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
								TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
								ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
								GridLayout gvIndividual = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
								
								pbIndividual.setVisibility(View.GONE);
								if(activeList.length() == 0){
									tvIndividual.setVisibility(View.VISIBLE);
								}
								else{
									gvIndividual.setVisibility(View.GONE);
									lvIndividual.setAdapter(new SellingListAdapter(mainActivity, activeList, ConstantClass.SELLING_ACTIVE));
									lvIndividual.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_ACTIVE));
									lvIndividual.setVisibility(View.VISIBLE);
								}
							}
							else if(json.has("not_won")){
								JSONArray activeList = json.getJSONArray("not_won");
								ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
								TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
								ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
								GridLayout gvIndividual = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
								
								pbIndividual.setVisibility(View.GONE);
								if(activeList.length() == 0){
									tvIndividual.setVisibility(View.VISIBLE);
								}
								else{
									gvIndividual.setVisibility(View.GONE);
									lvIndividual.setAdapter(new SellingListAdapter(mainActivity, activeList, ConstantClass.SELLING_NOTSOLD));
									lvIndividual.setOnItemClickListener(new SellingListListener(mainActivity, ConstantClass.SELLING_NOTSOLD ));
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
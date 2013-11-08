package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.BuyingListAdapter;
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
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BiddingListFragment extends Fragment {
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity, R.layout.spinner_buysell_list, ConstantClass.BUYING_SPINNER);
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
							
							inflater.inflate(R.layout.buying_all_listing, parent_view);
							try {
								doHttpBuyingList(true, true);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						case 1:
							//Bidding
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(true, false);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						case 2:
							//Didn't Win
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(false, true);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						default:
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(true, true);
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
		
		private void doHttpBuyingList(boolean bidding, boolean notwin) throws JSONException{
			Bundle params = new Bundle();
			params.putString("url", Server.Products.GETBIDDING+"?userId="+this.activeUser.getGuid()+
					"&bidding="+ Boolean.toString(bidding) +"&not_won="+Boolean.toString(notwin));
			params.putString("method", "GET");
			

			
			HttpRequest request = new HttpRequest(params, new HttpCallback() {
				
				@Override
				public void onSucess(JSONObject json) {
					try {
						
						
						
						if(json.has("bidding") && json.has("not_won")){
							
							//Bidding Var
							JSONArray biddingList = json.getJSONArray("bidding");
							ProgressBar pbBidding = (ProgressBar) mainLayout.findViewById(R.id.buy_bidding_pb);
							TextView tvBidding = (TextView) mainLayout.findViewById(R.id.buy_bidding_load_tv);
							GridLayout gvBidding = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
							ListView lvBidding = (ListView) mainLayout.findViewById(R.id.buy_bidding_lv);
							
							
							if(biddingList.length() == 0){
								pbBidding.setVisibility(View.GONE);
								tvBidding.setText(R.string.listbuysell_no_item);
							}
							else{
								gvBidding.setVisibility(View.GONE);
								lvBidding.setAdapter(new BuyingListAdapter(mainActivity, biddingList, ConstantClass.BUYING_BIDDING));
								lvBidding.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvBidding.setVisibility(View.VISIBLE);
							}
				
							
							//Didn't Win
							JSONArray notwinList = json.getJSONArray("not_won");
							ProgressBar pbNotwin = (ProgressBar) mainLayout.findViewById(R.id.buy_nwin_pb);
							TextView tvNotwin = (TextView) mainLayout.findViewById(R.id.buy_nwin_load_tv);
							GridLayout gvNotwin = (GridLayout) mainLayout.findViewById(R.id.buy_nwin_loader);
							ListView lvNotwin = (ListView) mainLayout.findViewById(R.id.buy_nwin_lv);
							
							
							if(notwinList.length() == 0){
								pbNotwin.setVisibility(View.GONE);
								tvNotwin.setText(R.string.listbuysell_no_item);
							}
							else{
								gvNotwin.setVisibility(View.GONE);
								lvNotwin.setAdapter(new BuyingListAdapter(mainActivity, notwinList,  ConstantClass.BUYING_NOTWIN));
								lvNotwin.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvNotwin.setVisibility(View.VISIBLE);
							}
							
							
							
							
						}
						else if(json.has("bidding")){
							JSONArray biddingList = json.getJSONArray("bidding");
							ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
							TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
							ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
							GridLayout gvBidding = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
							pbIndividual.setVisibility(View.GONE);
							if(biddingList.length() == 0){
								tvIndividual.setVisibility(View.VISIBLE);
							}
							else{
								gvBidding.setVisibility(View.GONE);
								lvIndividual.setAdapter(new BuyingListAdapter(mainActivity, biddingList,  ConstantClass.BUYING_BIDDING));
								lvIndividual.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvIndividual.setVisibility(View.VISIBLE);
							}
							
							

						}
						else if(json.has("not_won")){
							JSONArray notwinList = json.getJSONArray("not_won");
							ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
							TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
							ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
							GridLayout gvBidding = (GridLayout) mainLayout.findViewById(R.id.buy_bidding_loader);
							pbIndividual.setVisibility(View.GONE);
							if(notwinList.length() == 0){
								tvIndividual.setVisibility(View.VISIBLE);
							}
							else{
								gvBidding.setVisibility(View.GONE);
								lvIndividual.setAdapter(new BuyingListAdapter(mainActivity, notwinList,  ConstantClass.BUYING_NOTWIN));
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
			});
			request.execute();
		    
		}
		
	
}

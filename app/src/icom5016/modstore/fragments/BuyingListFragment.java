package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapters.BuySellListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.BuySellListListener;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BuyingListFragment extends Fragment {
		//User Instance Field
		private User activeUser;
		private MainInterfaceActivity mainActivity;
		private Spinner spinner;
		private View mainLayout;
		private ScrollView sv_container;
		
		//Contantans
		private final int BUYLIST_ALL = 7;
		private final int BUYLIST_BID = 1;
		private final int BUYLIST_PURCHASE = 2;
		private final int BUYLIST_DIDNOTWIN = 4;
		
		
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
								doHttpBuyingList(BUYLIST_ALL);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						case 1:
							//Bidding
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(BUYLIST_BID);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						case 2:
							//Purchase
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(BUYLIST_PURCHASE);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						case 3:
							//Didn't Win
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(BUYLIST_DIDNOTWIN);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							break;
						default:
							inflater.inflate(R.layout.buying_selling_any_listing, parent_view);
							try {
								doHttpBuyingList(BUYLIST_BID);
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
		
		private void doHttpBuyingList(int listing) throws JSONException{
			Bundle params = new Bundle();
			params.putString("url", Server.Orders.POSTBUYLIST);
			params.putString("method", "POST");
			
			JSONObject credentials = new JSONObject();
			credentials.put("userid", this.activeUser.getGuid());
			credentials.put("list_req", listing);
			
			HttpRequest request = new HttpRequest(params, credentials, new HttpCallback() {
				
				@Override
				public void onSucess(JSONObject json) {
					try {
						int listRequest = json.getInt("list_req");
						
						
						if(listRequest == BUYLIST_ALL){
							
							//Bidding Var
							ProgressBar pbBidding = (ProgressBar) mainLayout.findViewById(R.id.buy_bidding_pb);
							TextView tvBidding = (TextView) mainLayout.findViewById(R.id.buy_bidding_load_tv);
							GridView gvBidding = (GridView) mainLayout.findViewById(R.id.buy_bidding_loader);
							ListView lvBidding = (ListView) mainLayout.findViewById(R.id.buy_bidding_lv);
							JSONArray biddingList = json.getJSONArray("bidding_list");
							
							if(biddingList.length() == 0){
								pbBidding.setVisibility(View.GONE);
								tvBidding.setText(R.string.listbuysell_no_item);
							}
							else{
								gvBidding.setVisibility(View.GONE);
								lvBidding.setAdapter(new BuySellListAdapter(mainActivity, biddingList));
								lvBidding.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvBidding.setVisibility(View.VISIBLE);
							}
							
							//Purchase Var
							ProgressBar pbPurchase = (ProgressBar) mainLayout.findViewById(R.id.buy_purchase_pb);
							TextView tvPurchase = (TextView) mainLayout.findViewById(R.id.buy_purchase_load_tv);
							GridView gvPurchase = (GridView) mainLayout.findViewById(R.id.buy_purchase_loader);
							ListView lvPurchase = (ListView) mainLayout.findViewById(R.id.buy_purchase_lv);
							JSONArray purchaseList = json.getJSONArray("purchase_list");
							
							if(purchaseList.length() == 0){
								pbPurchase.setVisibility(View.GONE);
								tvPurchase.setText(R.string.listbuysell_no_item);
							}
							else{
								gvPurchase.setVisibility(View.GONE);
								lvPurchase.setAdapter(new BuySellListAdapter(mainActivity, purchaseList));
								lvPurchase.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvPurchase.setVisibility(View.VISIBLE);
							}
							
							//Didn't Win
							ProgressBar pbNotwin = (ProgressBar) mainLayout.findViewById(R.id.buy_nwin_pb);
							TextView tvNotwin = (TextView) mainLayout.findViewById(R.id.buy_nwin_load_tv);
							GridView gvNotwin = (GridView) mainLayout.findViewById(R.id.buy_nwin_loader);
							ListView lvNotwin = (ListView) mainLayout.findViewById(R.id.buy_nwin_lv);
							JSONArray notwinList = json.getJSONArray("notwin_list");
							
							if(notwinList.length() == 0){
								pbNotwin.setVisibility(View.GONE);
								tvNotwin.setText(R.string.listbuysell_no_item);
							}
							else{
								gvNotwin.setVisibility(View.GONE);
								lvNotwin.setAdapter(new BuySellListAdapter(mainActivity, notwinList));
								lvNotwin.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvNotwin.setVisibility(View.VISIBLE);
							}
							
							
							
							
						}
						else if(listRequest == BUYLIST_BID){
							JSONArray biddingList = json.getJSONArray("bidding_list");
							ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
							TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
							ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
							pbIndividual.setVisibility(View.GONE);
							if(biddingList.length() == 0){
								tvIndividual.setVisibility(View.VISIBLE);
							}
							else{
								lvIndividual.setAdapter(new BuySellListAdapter(mainActivity, biddingList));
								lvIndividual.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvIndividual.setVisibility(View.VISIBLE);
							}
							
							

						}
						else if(listRequest == BUYLIST_DIDNOTWIN){
							JSONArray notwinList = json.getJSONArray("notwin_list");
							ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
							TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
							ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
							
							pbIndividual.setVisibility(View.GONE);
							if(notwinList.length() == 0){
								tvIndividual.setVisibility(View.VISIBLE);
							}
							else{
								lvIndividual.setAdapter(new BuySellListAdapter(mainActivity, notwinList));
								lvIndividual.setOnItemClickListener(new BuySellListListener(mainActivity));
								lvIndividual.setVisibility(View.VISIBLE);
							}
						}
						else if(listRequest == BUYLIST_PURCHASE){
							JSONArray purchaseList = json.getJSONArray("purchase_list");
							ProgressBar pbIndividual = (ProgressBar) mainLayout.findViewById(R.id.buysell_any_pb);
							TextView tvIndividual = (TextView) mainLayout.findViewById(R.id.buysell_any_noitem);
							ListView lvIndividual = (ListView) mainLayout.findViewById(R.id.buysell_any_lv);
							
							pbIndividual.setVisibility(View.GONE);
							if(purchaseList.length() == 0){
								tvIndividual.setVisibility(View.VISIBLE);
							}
							else{
								lvIndividual.setAdapter(new BuySellListAdapter(mainActivity, purchaseList));
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
					Toast.makeText(mainActivity, R.string.errmsg_no_connection, Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onDone() {
					//No-Op
				}
			});
			request.execute();
		    
		}
		
	
}

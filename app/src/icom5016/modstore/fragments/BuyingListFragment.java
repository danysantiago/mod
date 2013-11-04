package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainInterfaceActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;

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
import android.widget.ScrollView;
import android.widget.Spinner;
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

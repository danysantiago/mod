package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.WhatsHotAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.listeners.ProductListingListener;
import icom5016.modstore.resources.ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements View.OnClickListener{
	
	private MainActivity ma;
	private LinearLayout btnSearch;
	private LinearLayout btnShopByDept;
	private TextView tvWelcome;
	private ListView lvWhatHot;
	private ProgressDialog pd;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_main, container,false);
		this.ma = (MainActivity) this.getActivity();
		
		tvWelcome = (TextView) view.findViewById(R.id.mainWelcomeTv);
		btnSearch = (LinearLayout) view.findViewById(R.id.mfSearch);
		btnShopByDept =(LinearLayout) view.findViewById(R.id.mfShopByDept);
		
		lvWhatHot = (ListView) view.findViewById(R.id.mfWhatHoyLv);
		pd = ma.processDialog;
		
		//Set Name
		if(this.ma.getActiveUser() !=  null){
			tvWelcome.setText("Welcome, "+this.ma.getActiveUser().getFirstName());
		}
		
		btnSearch.setOnClickListener(this);
		btnShopByDept.setOnClickListener(this);
		
		pd.setMessage(getResources().getString(R.string.pd_mactivity));
		pd.show();
		this.doHttpWhatHot();
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mfSearch:
			this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new SearchFragment());
			break;
		case R.id.mfShopByDept:
			//Shop By Category
    		CategoryListFragment clf = new CategoryListFragment();
    		Bundle bnd = new Bundle();
    		bnd.putInt(ConstantClass.CATEGORY_LIST_PARENT_KEY, -1);
    		clf.setArguments(bnd);
    		this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), clf);
			break;
		}
	}
	
	private void doHttpWhatHot() {
		Bundle params = new Bundle();
		params.putString("method", "GET");
		params.putString("url", Server.Products.GETWHATHOT);	
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					JSONArray list = json.getJSONArray("results");
					lvWhatHot.setAdapter(new WhatsHotAdapter(ma, list));
					lvWhatHot.setOnItemClickListener(new ProductListingListener(ma));
					
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onDone() {
				pd.dismiss();
			}
		});
		request.execute();
	}

}

package icom5016.modstore.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

public class MyStoreFragment extends Fragment implements View.OnClickListener {
	
	private MainActivity ma;
	private LinearLayout orders;
	private LinearLayout bidding;
	private LinearLayout selling;
	private LinearLayout rating;
	private ProgressBar ratingPb;
	private RatingBar ratingBar;
	private Dialog ratingDialog;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mystore, container, false);
		this.orders = (LinearLayout) view.findViewById(R.id.mystoreOrders);
		this.orders.setOnClickListener(this);
		
		this.bidding = (LinearLayout) view.findViewById(R.id.mystoreBidding);
		this.bidding.setOnClickListener(this);
		
		this.selling = (LinearLayout) view.findViewById(R.id.mystoreSelling);
		this.selling.setOnClickListener(this);
		
		this.rating = (LinearLayout) view.findViewById(R.id.mystoreMyRating);
		this.rating.setOnClickListener(this);
		
		this.ma = (MainActivity) this.getActivity();
				
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mystoreOrders:
				ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new MyOrdersListFragment());
				break;
			case R.id.mystoreBidding:
				ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new BiddingListFragment());
				break;
			case R.id.mystoreSelling:
				ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), new SellingListFragment());
				break;
			case R.id.mystoreMyRating:
				showRatingDialog();
				break;
			}
		
	}

	private void showRatingDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(ma);
		LayoutInflater inflator = this.ma.getLayoutInflater();
		View view = inflator.inflate(R.layout.dialog_rating, null,false);
		
		this.ratingBar = (RatingBar) view.findViewById(R.id.myStoreRatingBar);
		this.ratingPb = (ProgressBar) view.findViewById(R.id.myStoreRatingProgressBar);
		
		build.setView(view);
		build.setTitle(R.string.mystore_myrating);
		build.setNegativeButton(R.string.close, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//NoOp
			}
			
		});
		
		this.ratingDialog = build.create();
		this.ratingDialog.show();
		doHttpRating();
	}

	private void doHttpRating() {
		User user = DataFetchFactory.getUserFromSPref(getActivity());
		
		Uri.Builder url = Uri.parse(Server.User.RATING).buildUpon();
		url.appendQueryParameter("userId", ""+user.getGuid());
		
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", url.toString());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try{
					float avgSellerRating = (float) json.getInt("avg_seller_rating");
					ratingPb.setVisibility(View.GONE);
					ratingBar.setRating(avgSellerRating);
					ratingBar.setVisibility(View.VISIBLE);
				}catch(JSONException e){
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, R.string.errmsg_no_connection, Toast.LENGTH_SHORT).show();
				ratingDialog.dismiss();
			}
		});
		request.execute();
	}
	
}

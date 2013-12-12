package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.adapter.OrderDetailsListAdapter;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.models.OrderDetail;
import icom5016.modstore.models.Orders;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderDetailsListFragment extends Fragment {
	
	private int orderId;
	//Placeholders
	private ProgressBar pbPlaceHolder;
	private ImageView ivPlaceHolder;
	
	//Main Container
	private LinearLayout llMainContainer;
	
	//TextView and ListView
	private TextView tvAddress;
	private TextView tvDate;
	private TextView tvTotal;
	private TextView tvCCNum;
	
	private ListView lvDetails;
	
	private MainActivity ma;
	private RatingBar ratingBar;
	private Dialog ratingDialog;
	private AlertDialog.Builder ratingBuilder;
	private ProgressBar ratingPb;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_myorders_details, container,false);
		
		this.orderId = this.getArguments().getInt(ConstantClass.ORDERID_KEY);
		
		//Change Orderm Number
		TextView orderNum = (TextView) view.findViewById(R.id.order_tv);
		orderNum.setText(orderNum.getText()+Integer.toString(orderId));
		
		//PlaceHolders
		this.pbPlaceHolder = (ProgressBar) view.findViewById(R.id.odProgressBar);
		this.ivPlaceHolder = (ImageView) view.findViewById(R.id.odPlaceHolderImage);
		
		//Main Container
		this.llMainContainer = (LinearLayout) view.findViewById(R.id.odMainLayout);
		
		//Set vars
		this.tvAddress = (TextView) view.findViewById(R.id.tvOrderAddress);
		this.tvDate = (TextView) view.findViewById(R.id.tvOrderDetailsDate);
		this.tvTotal = (TextView) view.findViewById(R.id.tvOrderTotal);
		this.tvCCNum = (TextView) view.findViewById(R.id.tvOrderCCNum);
		this.lvDetails = (ListView) view.findViewById(R.id.odListView);
		
		
		this.ma = (MainActivity) this.getActivity();
		
		doHttpOrderDetails();
		
		return view;
	}


	private void doHttpOrderDetails() {
		//Make PB Visible
		this.ivPlaceHolder.setVisibility(View.GONE);
		this.pbPlaceHolder.setVisibility(View.VISIBLE);
		//Params
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.GETORDERDETAILS+"?orderId="+this.orderId);
		params.putString("method", "GET");
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try {
					Orders order = new Orders(json.getJSONObject("order"));
					tvTotal.setText(tvTotal.getText()+" "+order.getOrderTotalString());
					tvDate.setText(tvDate.getText()+" "+order.getDateFormatedString());
					tvAddress.setText(order.getAddress().toString());
					
					CreditCard cc = order.getCreditCard();
					tvCCNum.setText(cc.getTypeString()+" - "+
							cc.getNumber().substring(cc.getNumber().length()-4));
					
					lvDetails.setAdapter(new OrderDetailsListAdapter(ma, json.getJSONArray("details")));
					
					lvDetails.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
							OrderDetail orderDetails = (OrderDetail) lvDetails.getAdapter().getItem(pos);
							showRatingDialog(orderDetails);
						}
						
					});
					
					
					
					pbPlaceHolder.setVisibility(View.GONE);
					llMainContainer.setVisibility(View.VISIBLE);
					
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onFailed() {
				ivPlaceHolder.setVisibility(View.VISIBLE);
			}
			@Override 
			public void onDone() {
				pbPlaceHolder.setVisibility(View.GONE);
			}
			
		});
		
		request.execute();
		
	}
	
	private void showRatingDialog(final OrderDetail orderDetails) {
		AlertDialog.Builder build = new AlertDialog.Builder(ma);
		LayoutInflater inflator = this.ma.getLayoutInflater();
		View view = inflator.inflate(R.layout.dialog_rating, null,false);
		
		this.ratingBar = (RatingBar) view.findViewById(R.id.myStoreRatingBar);
		this.ratingPb = (ProgressBar) view.findViewById(R.id.myStoreRatingProgressBar);
		
		build.setView(view);
		build.setTitle("Rate Seller");
		build.setNegativeButton("Close", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//NoOp
			}
			
		});
		
		this.ratingBuilder = build;
		try {
			doHttpRating(orderDetails);
		} catch (JSONException e) {
			Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
		}
		
	}


	private void doHttpRatingSubmit(OrderDetail od) throws JSONException {
		User user = DataFetchFactory.getUserFromSPref(getActivity());
		
		Bundle params = new Bundle();
		params.putString("method", "POST");
		params.putString("url", Server.User.RATING );
		
		JSONObject credentials = new JSONObject();
		credentials.put("userId", user.getGuid());
		credentials.put("orderDetailsId", od.getId() );
		credentials.put("sellerId", od.getProduct().getUserId());
		credentials.put("ratingValue", Float.toString(ratingBar.getRating()));
		
		HttpRequest request = new HttpRequest(params ,credentials,new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try{
					String status = json.getString("status");
					if(status.equals("ok")){
						Toast.makeText(ma, "Seller Rated" , Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(ma, "Rating Failed" , Toast.LENGTH_SHORT).show();
					}
					
				}catch(JSONException e){
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, "No Connection", Toast.LENGTH_SHORT).show();
				//ratingDialog.dismiss();
			}
		});
		request.execute();
	}


	private void doHttpRating(final OrderDetail od) throws JSONException {
		User user = DataFetchFactory.getUserFromSPref(getActivity());
		
		Bundle params = new Bundle();
		params.putString("method", "POST");
		params.putString("url", Server.User.RATING_CHECK );
		
		JSONObject credentials = new JSONObject();
		credentials.put("userId", user.getGuid());
		credentials.put("orderDetailsId", od.getId() );
		credentials.put("sellerId", od.getProduct().getUserId());
		
		HttpRequest request = new HttpRequest(params , credentials,new HttpCallback() {

			@Override
			public void onSucess(JSONObject json) {
				try{
					String status = json.getString("status");
					if(status.equals("rated")){
						float avgSellerRating = (float) json.getInt("your_rating");
						ratingPb.setVisibility(View.GONE);
						ratingBar.setRating(avgSellerRating);
						ratingBar.setVisibility(View.VISIBLE);
						ratingDialog.setTitle("Your Rating");
					}
					else if(status.equals("ok")){
						ratingPb.setVisibility(View.GONE);
						ratingBar.setVisibility(View.VISIBLE);
						ratingBar.setFocusable(true);
						ratingBar.setClickable(true);
						ratingBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
									try {
										doHttpRatingSubmit(od);
									} catch (JSONException e) {
										Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
									}
									
							}
						});
						
						ratingDialog = ratingBuilder.create();
						ratingDialog.show();
					}
				}catch(JSONException e){
					Toast.makeText(ma, R.string.errmsg_bad_json , Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(ma, "No Connection", Toast.LENGTH_SHORT).show();
				
			}

		});
		request.execute();
	}

}

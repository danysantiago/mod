package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductFragment extends Fragment {
	
	private int productId;
	
	private Product product;
	private User seller;
	private User user;
	
	private int stock;
	private float avgSellerRating;
	private float currentBid;
	private int winnerId;
	
	private boolean isOwner;
	private boolean isEnded;
	private boolean isSold;
	private boolean isBidProduct;
	private boolean isWinner;
	
	private RelativeLayout progressContainer;
	private ScrollView productContainer;
	
	private LinearLayout soldItemView;
	private LinearLayout auctionEndedView;
	private LinearLayout outOfStockView;
	private LinearLayout wonItemView;
	
	private TextView productNameTV;
	private TextView priceTV;
	private TextView brandTV;
	private TextView modelTV;
	private TextView dimensionsTV;
	private TextView descriptionTV;
	private TextView sellerTV;
	private TextView endDateTV;
	private RatingBar ratingBar;
	private LinearLayout ratingLayout;
	private LinearLayout bidLayout;
	private Button buyButton;
	private Button bidButton;
	
	private ProgressBar pd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_product, container,false);
		
		productNameTV = (TextView) view.findViewById(R.id.productNameTextView);
		priceTV = (TextView) view.findViewById(R.id.priceTextView);
		brandTV = (TextView) view.findViewById(R.id.brandTextView);
		modelTV = (TextView) view.findViewById(R.id.modelTextView);
		dimensionsTV = (TextView) view.findViewById(R.id.dimensionsTextView);
		descriptionTV = (TextView) view.findViewById(R.id.descriptionTextView);
		sellerTV = (TextView) view.findViewById(R.id.sellerTextView);
		endDateTV = (TextView) view.findViewById(R.id.endDateTextView);
		
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar); 
		
		ratingLayout = (LinearLayout) view.findViewById(R.id.ratingLayout);
		
		buyButton = (Button) view.findViewById(R.id.btnProductAdd);
		bidButton = (Button) view.findViewById(R.id.bidButton);
		
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		
		soldItemView = (LinearLayout) view.findViewById(R.id.soldItemView);
		auctionEndedView = (LinearLayout) view.findViewById(R.id.auctionEndedView);
		outOfStockView = (LinearLayout) view.findViewById(R.id.outOfStockView);
		wonItemView = (LinearLayout) view.findViewById(R.id.wonItemView);
		
		progressContainer = (RelativeLayout) view.findViewById(R.id.progressBarContainer);
		productContainer = (ScrollView) view.findViewById(R.id.productContainer);
		
		progressContainer.setVisibility(View.VISIBLE);
		productContainer.setVisibility(View.GONE);
		
		productId = getArguments().getInt(ConstantClass.PRODUCT_KEY);
//		
//		if(product.getAuction_ends().length() > 0) {
//			buyButton.setVisibility(View.GONE);
//			
//			TextView endDate = (TextView) view.findViewById(R.id.endDateTextView);
//			endDate.setText(product.getAuction_ends());
//			priceTV.setText("Highest Bid: " + product.getBid());
//		} else {
//			bidLayout.setVisibility(View.GONE);
//			
//			priceTV.setText("Price: " + product.getPrice());
//		}
		
//		productNameTV.setText(product.getName());
//		brandTV.setText("Brand: " + product.getBrand());
//		modelTV.setText("Model: " + product.getModel());
//		dimensionsTV.setText("Dimensions: " + product.getDimensions());
//		descriptionTV.setText(product.getDescription());
//		sellerTV.setText("Seller: " + "Juan Del Pueblo (8)");
		
//		ratingLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				LinearLayout ll = new LinearLayout(getActivity());
//				RatingBar rb = new RatingBar(getActivity());
//				rb.setNumStars(5);
//				
//				ll.addView(rb);
//				
//				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		        builder.setTitle("Rate Seller")
//		           .setView(ll)
//				   .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
//					   public void onClick(DialogInterface dialog, int id) {
//				       }
//				   })
//				   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//					   public void onClick(DialogInterface dialog, int id) {
//				       }
//				   });
//		        builder.create().show();
//
//			}
//		});
//		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		user = DataFetchFactory.getUserFromSPref(getActivity());
		
		Uri.Builder url = Uri.parse(Server.Products.GET).buildUpon();
		url.appendQueryParameter("userId", ""+user.getGuid());
		url.appendQueryParameter("productId",""+productId);
		
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", url.toString());
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					product = new Product(json.getJSONObject("product"));
					seller = new User(json.getJSONObject("seller"));
						
					JSONObject jsonProduct = json.getJSONObject("product");
					
					avgSellerRating = (float) jsonProduct.getInt("avg_seller_rating");
					stock = jsonProduct.getInt("stock");
					if(!jsonProduct.getString("winning_user_id").equals("null")) {
						winnerId = jsonProduct.getInt("winning_user_id");
					}
					if(!jsonProduct.getString("actual_bid").equals("null")) {
						currentBid = (float) jsonProduct.getDouble("actual_bid");
						isBidProduct = true;
					}
					
					if(user.getGuid() == (seller.getGuid())) {
						isOwner = true;
					}
					
					if(user.getGuid() == winnerId) {
						isWinner = true;
					}
					
					if(stock == 0) {
						isSold = true;
						
						if(isOwner) {
							soldItemView.setVisibility(View.VISIBLE);
						} else {
							outOfStockView.setVisibility(View.VISIBLE);
						}
					}
					
					if(isBidProduct && product.getAuctionEndDate().before(new Date())) {
						outOfStockView.setVisibility(View.GONE);
						if(isWinner) {
							wonItemView.setVisibility(View.VISIBLE);
						} else {
							auctionEndedView.setVisibility(View.VISIBLE);
						}
						isEnded = true;
					}
					
					progressContainer.setVisibility(View.GONE);
					productContainer.setVisibility(View.VISIBLE);
					
					if(isBidProduct) {
						buyButton.setVisibility(View.GONE);
						if(isEnded) {
							bidButton.setVisibility(View.GONE);
							endDateTV.setText("Auction ended: " + product.getAuctionEndsTsString());
							priceTV.setText("Won for: $" + currentBid);
						} else {
							bidButton.setVisibility(View.VISIBLE);
							endDateTV.setText("Auction ends: " + product.getAuctionEndsTsString());
							priceTV.setText("Current Highest Bid: $" + currentBid);
						}
					} else {
						bidButton.setVisibility(View.GONE);
						endDateTV.setVisibility(View.GONE);
						if(isEnded) {
							buyButton.setVisibility(View.GONE);
							endDateTV.setText("Auction ended: " + product.getAuctionEndsTsString());
							priceTV.setText("Bought for: $" + product.getBuyItNowPrice());
						} else {
							buyButton.setVisibility(View.VISIBLE);
							priceTV.setText("Price: $" + product.getBuyItNowPrice());
						}
					}
					
					productNameTV.setText(product.getName());
					brandTV.setText("Brand: " + product.getBrand());
					modelTV.setText("Model: " + product.getModel());
					dimensionsTV.setText("Dimensions: " + product.getDimensions());
					descriptionTV.setText(product.getDescription());
					sellerTV.setText("Seller: " + seller.getUsername());
					ratingBar.setRating(avgSellerRating);
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "Error parsing.", Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Unable to load Product Data, please trya again.", Toast.LENGTH_LONG).show();
				pd.setVisibility(View.GONE);
			}
		});
		request.execute();
	}

}

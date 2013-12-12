package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import java.text.NumberFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductFragment extends Fragment {
	
	private int productId;
	private MainActivity ma;
	private ProgressDialog pDialog;
	
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
	private HorizontalScrollView imagesScrollContainer;
	private LinearLayout imagesContainer;
	
	private LinearLayout soldItemView;
	private LinearLayout auctionEndedView;
	private LinearLayout outOfStockView;
	private LinearLayout wonItemView;
	private LinearLayout highBidView;
	private LinearLayout buyNowButtonsContainer;

	
	private TextView productNameTV;
	private TextView priceTV;
	private TextView quantityTV;
	private TextView brandTV;
	private TextView modelTV;
	private TextView dimensionsTV;
	private TextView descriptionTV;
	private TextView sellerTV;
	private TextView endDateTV;
	private RatingBar ratingBar;
	private LinearLayout ratingLayout;
	private LinearLayout bidLayout;
	private Button addButton;
	private Button buyButton;
	private Button bidButton;
	private RelativeLayout noImageTextView;
	
	private ProgressBar pd;

	private ImageLoader imageLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_product, container,false);
		
		ma = (MainActivity) getActivity();
		pDialog = new ProgressDialog(getActivity());
		
		imageLoader = new ImageLoader(getActivity());
		
		productNameTV = (TextView) view.findViewById(R.id.productNameTextView);
		priceTV = (TextView) view.findViewById(R.id.priceTextView);
		quantityTV = (TextView) view.findViewById(R.id.quantityTextView);
		brandTV = (TextView) view.findViewById(R.id.brandTextView);
		modelTV = (TextView) view.findViewById(R.id.modelTextView);
		dimensionsTV = (TextView) view.findViewById(R.id.dimensionsTextView);
		descriptionTV = (TextView) view.findViewById(R.id.descriptionTextView);
		sellerTV = (TextView) view.findViewById(R.id.sellerTextView);
		endDateTV = (TextView) view.findViewById(R.id.endDateTextView);
		noImageTextView = (RelativeLayout) view.findViewById(R.id.noImageTextView);
		
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar); 
		
		ratingLayout = (LinearLayout) view.findViewById(R.id.ratingLayout);
		
		buyNowButtonsContainer = (LinearLayout) view.findViewById(R.id.buyNowButtonsContainer);
		addButton = (Button) view.findViewById(R.id.btnProductAdd);
		addButton.setOnClickListener(new AddButtonListener());
		buyButton = (Button) view.findViewById(R.id.btnProductBuyNow);
		buyButton.setOnClickListener(new BuyButtonListener());
		bidButton = (Button) view.findViewById(R.id.bidButton);
		bidButton.setOnClickListener(new BidButtonListener());
		
		
		pd = (ProgressBar) view.findViewById(R.id.progressBar);
		
		soldItemView = (LinearLayout) view.findViewById(R.id.soldItemView);
		auctionEndedView = (LinearLayout) view.findViewById(R.id.auctionEndedView);
		outOfStockView = (LinearLayout) view.findViewById(R.id.outOfStockView);
		wonItemView = (LinearLayout) view.findViewById(R.id.wonItemView);
		highBidView = (LinearLayout) view.findViewById(R.id.highBidView);
		
		progressContainer = (RelativeLayout) view.findViewById(R.id.progressBarContainer);
		productContainer = (ScrollView) view.findViewById(R.id.productContainer);
		imagesScrollContainer = (HorizontalScrollView) view.findViewById(R.id.imagesScrollContainer);
		imagesContainer = (LinearLayout) view.findViewById(R.id.imagesContainer);
		
		progressContainer.setVisibility(View.VISIBLE);
		productContainer.setVisibility(View.GONE);
		
		productId = getArguments().getInt(ConstantClass.PRODUCT_KEY);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		user = DataFetchFactory.getUserFromSPref(getActivity());
		
		
		Uri.Builder url = Uri.parse(Server.Products.GET).buildUpon();
		if(user != null) {
			url.appendQueryParameter("userId", ""+user.getGuid());
		}
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
					
					JSONArray images = json.getJSONArray("images");
					avgSellerRating = (float) jsonProduct.getInt("avg_seller_rating");
					stock = jsonProduct.getInt("stock");
					
					if (!product.getAuctionEndsTs().equals("null")) {
						isBidProduct = true;
					}
					
					if(!jsonProduct.getString("winning_user_id").equals("null")) {
						winnerId = jsonProduct.getInt("winning_user_id");
					}
					
					if(!jsonProduct.getString("actual_bid").equals("null")) {
						currentBid = (float) jsonProduct.getDouble("actual_bid");
					} else {
						currentBid = (float) product.getStartingBidPrice();
					}
					
					if(user != null && user.getGuid() == (seller.getGuid())) {
						isOwner = true;
					}
					
					if(user != null && user.getGuid() == winnerId) {
						isWinner = true;
					}
					
					if(stock == 0) {
						isSold = true;
						
						if(isOwner) {
							soldItemView.setVisibility(View.VISIBLE);
						} else {
							outOfStockView.setVisibility(View.VISIBLE);
						}
					} else {
						if(isOwner) {
							
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
					} else if (isBidProduct && isWinner) {
						highBidView.setVisibility(View.VISIBLE);
					}
					
					progressContainer.setVisibility(View.GONE);
					productContainer.setVisibility(View.VISIBLE);
					
					if(isBidProduct) {
						buyNowButtonsContainer.setVisibility(View.GONE);
						if(isEnded) {
							bidButton.setVisibility(View.GONE);
							endDateTV.setText("Auction ended: " + product.getAuctionEndsTsString());
							if (currentBid == product.getStartingBidPrice()) {
								priceTV.setText("Initial Bid Price: " + product.getStartingBidPriceString());
							} else {
								priceTV.setText("Won for: " + num_format(currentBid));
							}
						} else {
							bidButton.setVisibility(View.VISIBLE);
							endDateTV.setText("Auction ends: " + product.getAuctionEndsTsString());
							priceTV.setText("Current Highest Bid: " + num_format(currentBid));
						}
					} else {
						bidButton.setVisibility(View.GONE);
						endDateTV.setVisibility(View.GONE);
						if(isEnded) {
							buyNowButtonsContainer.setVisibility(View.GONE);
							endDateTV.setText("Auction ended: " + product.getAuctionEndsTsString());
							priceTV.setText("Bought for: " + product.getBuyItNowPriceString());
						} else {
							buyNowButtonsContainer.setVisibility(View.VISIBLE);
							priceTV.setText("Price: " + product.getBuyItNowPriceString());
						}
					}
					
					if(isWinner) {
						ratingBar.setOnClickListener(new RatingListener());
					}
					
					productNameTV.setText(product.getName());
					quantityTV.setText(""+product.getQuantity());
					brandTV.setText(product.getBrand());
					modelTV.setText(product.getModel());
					dimensionsTV.setText(product.getDimensions());
					descriptionTV.setText(product.getDescription());
					sellerTV.setText(seller.getUsername());
					ratingBar.setRating(avgSellerRating);
					
					if(images.length() > 0) {
						imagesScrollContainer.setVisibility(View.VISIBLE);
						noImageTextView.setVisibility(View.GONE);
						
						for(int i = 0; i < images.length(); i++) {
							ImageView imageView = new ImageView(getActivity());
							LayoutParams params = new LayoutParams(
							        LayoutParams.WRAP_CONTENT,      
							        LayoutParams.WRAP_CONTENT
							);
							params.setMargins(5, 0, 5, 0);
							imageView.setLayoutParams(params);
							imageView.setScaleType(ScaleType.CENTER_INSIDE);
							imageView.setBackgroundResource(R.drawable.image_view_bg);
							String url = Server.Images.GET + images.getJSONObject(i).getString("image_src");
							imageLoader.DisplayImage(url, imageView);
							imageView.setTag(url);
							
							imageView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									ImageView imgV = new ImageView(getActivity());
									imageLoader.DisplayImage(v.getTag().toString(), imgV);
									
									AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
									builder.setView(imgV);
							        builder.create().show();
								}
							});
							
							imagesContainer.addView(imageView);
						}
					} else {
						imagesScrollContainer.setVisibility(View.GONE);
						noImageTextView.setVisibility(View.VISIBLE);
					}
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
	
	public class RatingListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			LinearLayout ll = new LinearLayout(getActivity());
			RatingBar rb = new RatingBar(getActivity());
			rb.setNumStars(5);
			
			ll.addView(rb);
			ll.setGravity(Gravity.CENTER);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Rate Seller")
	           .setView(ll)
			   .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
			       }
			   })
			   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
			       }
			   });
	        builder.create().show();
		}
		
	}
	
	public class BuyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			if(user == null){
				Toast.makeText(ma, "Must be Log In", Toast.LENGTH_SHORT).show();
				return;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Buy Now");
	        builder.setMessage("Buy " + product.getName() + " now?");
	        builder.setPositiveButton("Procced to Checkout", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				   Bundle bnd = new Bundle();
				   bnd.putInt(ConstantClass.CHECKOUT_TYPE_KEY, ConstantClass.CHECKOUT_TYPE_BUY);
				   bnd.putInt(ConstantClass.PRODUCT_KEY, productId);
				   CheckoutFragment cf = new CheckoutFragment();
				   cf.setArguments(bnd);
				   ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), cf);
				   
		       }
		    });
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
		       }
		    });
	        builder.create().show();
		}
		
	}
	
	public class AddButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			if(user == null){
				Toast.makeText(ma, "Must be Log In", Toast.LENGTH_SHORT).show();
				return;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Add to Cart");
	        builder.setMessage("Add " + product.getName() + " to cart?");
	        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				   try {
					 pDialog.setMessage(getResources().getString(R.string.cart_insert_pdialog));
					 pDialog.show();
					doHttpAddToCart(productId, user.getGuid());
				} catch (JSONException e) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
		       }
		    });
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
		       }
		    });
	        builder.create().show();
		}
		
	}
	
	public class BidButtonListener implements OnClickListener {
		

		@Override
		public void onClick(View v) {
			
			if(user == null){
				Toast.makeText(ma, "Must be Log In", Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			View bidDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bid_layout, null);
			final EditText amountBox = (EditText) bidDialogView.findViewById(R.id.amountEditText);
			amountBox.setText(""+(currentBid + 1));
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Bid");
	        builder.setView(bidDialogView);
	        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				   try {
					doHttpBid(user.getGuid(),Double.parseDouble(amountBox.getText().toString().trim()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		       }
		    });
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
		       }
		    });
	        builder.create().show();
			
		}
		
	}
	
	private void doHttpBid(int userId,double bidAmount) throws JSONException{
		Bundle params = new Bundle();
		params.putString("url", Server.Orders.BIDS);
		params.putString("method", "POST");
		//Credentials
		JSONObject credentials = new JSONObject();
		credentials.put("productId", Integer.toString(productId));
		credentials.put("userId", userId);
		credentials.put("bidAmount", Double.toString(bidAmount));
		
		HttpRequest request = new HttpRequest(params, credentials ,new HttpCallback() {
		
			@Override
			public void onSucess(JSONObject json) {
				
				String acknowledgeCode = null;
				try {
					acknowledgeCode = json.getString("status");
				} catch (JSONException e1) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
				
				if(acknowledgeCode != null)
				{
					if(acknowledgeCode.equals("OK")){
						Toast.makeText(ma, "Congratulation Your the Highest Bidder", Toast.LENGTH_SHORT).show();
						ma.fragmentStack.pop();
						ProductFragment pf = new ProductFragment();
						Bundle bnd = new Bundle();
						bnd.putInt(ConstantClass.PRODUCT_KEY, productId);
						pf.setArguments(bnd);
						ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
					}else if(acknowledgeCode.equals("INVALID_USER")){
						Toast.makeText(ma, "Failed: Invalid User", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("NO_DEFAULT_CC")){
						Toast.makeText(ma, "No Default Credit Card Found", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("NO_DEFAULT_ADDR")){
						Toast.makeText(ma, "No Default Address Found", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("INVALID_PRODUCT")){
						Toast.makeText(ma, "Failed: Invalid Product", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("PRODUCT_FROM_BUYER")){
						Toast.makeText(ma, "Failed: Bidding in Your Own Product", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("AUCTION_ENDED")){
						Toast.makeText(ma, "Actuion Ended", Toast.LENGTH_SHORT).show();
						ma.fragmentStack.pop();
						ProductFragment pf = new ProductFragment();
						Bundle bnd = new Bundle();
						bnd.putInt(ConstantClass.PRODUCT_KEY, productId);
						pf.setArguments(bnd);
						ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
					}else if(acknowledgeCode.equals("OUTBIDDED")){
						Toast.makeText(ma, "Outbid", Toast.LENGTH_SHORT).show();
						ma.fragmentStack.pop();
						ProductFragment pf = new ProductFragment();
						Bundle bnd = new Bundle();
						bnd.putInt(ConstantClass.PRODUCT_KEY, productId);
						pf.setArguments(bnd);
						ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), pf);
					}
					else {
						Toast.makeText(ma, "Unable to Complete Bid", Toast.LENGTH_SHORT).show();
					}
				}
				
				
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(ma, "Unable to Complete Bid", Toast.LENGTH_SHORT).show();
				
			}
			public void onDone(){
				pDialog.dismiss();
			}
		});
		request.execute();
	}
	
	private String num_format(float num) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return "$" + nf.format(num);
	}
	
	public void doHttpAddToCart(int productId, int userId) throws JSONException{
		Bundle params = new Bundle();
		params.putString("url", Server.Cart.CART);
		params.putString("method", "POST");
		//Credentials
		JSONObject credentials = new JSONObject();
		credentials.put("productId", Integer.toString(productId));
		credentials.put("userId", userId);
		HttpRequest request = new HttpRequest(params, credentials ,new HttpCallback() {
		
			@Override
			public void onSucess(JSONObject json) {
				
				String acknowledgeCode = null;
				try {
					acknowledgeCode = json.getString("status");
				} catch (JSONException e1) {
					Toast.makeText(ma, R.string.errmsg_bad_json,
							Toast.LENGTH_SHORT).show();
				}
				
				if(acknowledgeCode != null)
				{
					if(acknowledgeCode.equals("OUT_OF_STOCK")){
						Toast.makeText(ma, "Out of Stock", Toast.LENGTH_SHORT).show();
					}else if(acknowledgeCode.equals("PRODUCT_FROM_BUYER")){
						Toast.makeText(ma, "You can't buy your own product", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(ma, R.string.cart_insert_success, Toast.LENGTH_SHORT).show();
						addButton.setText("Add More");
					}
				}
				
				
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(ma, "Unable to add to Cart", Toast.LENGTH_SHORT).show();
				
			}
			public void onDone(){
				pDialog.dismiss();
			}
		});
		request.execute();
	}

}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Product;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProductFragment extends Fragment {

	private Product product;
	
	private RatingBar ratingBar;
	
	public static ProductFragment getInstance(Product product){
		ProductFragment pf = new ProductFragment();
		pf.setProduct(product);
		return pf;
		
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_product, container,false);
		
		TextView productNameTV = (TextView) view.findViewById(R.id.productNameTextView);
		TextView priceTV = (TextView) view.findViewById(R.id.priceTextView);
		TextView brandTV = (TextView) view.findViewById(R.id.brandTextView);
		TextView modelTV = (TextView) view.findViewById(R.id.modelTextView);
		TextView dimensionsTV = (TextView) view.findViewById(R.id.dimensionsTextView);
		TextView descriptionTV = (TextView) view.findViewById(R.id.descriptionTextView);
		TextView sellerTV = (TextView) view.findViewById(R.id.sellerTextView);
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
		LinearLayout ratingLayout = (LinearLayout) view.findViewById(R.id.ratingLayout);
		
		productNameTV.setText(product.getName());
		priceTV.setText("Price: " + product.getPrice());
		brandTV.setText("Brand: " + product.getBrand());
		modelTV.setText("Model: " + product.getModel());
		dimensionsTV.setText("Dimensions: " + product.getDimensions());
		descriptionTV.setText(product.getDescription());
		sellerTV.setText("Seller: " + "Juan Del Pueblo (8)");
		
		ratingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout ll = new LinearLayout(getActivity());
				RatingBar rb = new RatingBar(getActivity());
				rb.setNumStars(5);
				
				ll.addView(rb);
				
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
		});
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

}

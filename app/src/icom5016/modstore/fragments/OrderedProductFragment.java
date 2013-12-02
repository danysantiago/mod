package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.OrderDetail;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OrderedProductFragment extends Fragment {
	TextView lblName;
	TextView lblPrice;
	TextView lblBrand;
	TextView lblModel;
	TextView lblDimensions;
	TextView lblQuantity;
	TextView lblDescription;
	TextView lblUser;
	TextView lblAddress;
	TextView lblTrackingNum;
	EditText txtTrackingNum;
	Button btnUpdateTrackingNum;
	
	OrderDetail orderDetail;
	boolean buyerView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_orderedproduct, container, false);
		
		lblName = (TextView)view.findViewById(R.id.lblOPName);
		lblPrice = (TextView)view.findViewById(R.id.lblOPPrice);
		lblBrand = (TextView)view.findViewById(R.id.lblOPBrand);
		lblModel = (TextView)view.findViewById(R.id.lblOPModel);
		lblDimensions = (TextView)view.findViewById(R.id.lblOPDimensions);
		lblQuantity = (TextView)view.findViewById(R.id.lblOPQuantity);
		lblDescription = (TextView)view.findViewById(R.id.lblOPDescription);
		lblUser = (TextView)view.findViewById(R.id.lblOPUser);
		lblAddress = (TextView)view.findViewById(R.id.txtOPAddress);
		lblTrackingNum = (TextView)view.findViewById(R.id.lblOPTrackingNum);
		txtTrackingNum = (EditText)view.findViewById(R.id.txtOPTrackingNum);
		btnUpdateTrackingNum = (Button)view.findViewById(R.id.btnTrackingNumUpdate);
		
		loadOrderDetail();
		
		return view;
	}
	
	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}
	
	public void setBuyerView(boolean buyerView) {
		this.buyerView = buyerView;
	}
	
	private void loadOrderDetail() {
//		if (orderDetail != null) {
//			lblName.setText(orderDetail.getProduct().getName());
//			lblPrice.setText("Price: " + orderDetail.getPrice());
//			lblBrand.setText("Brand: " + orderDetail.getProduct().getBrand());
//			lblModel.setText("Model: " + orderDetail.getProduct().getModel());
//			lblDimensions.setText("Dimensions: " + orderDetail.getProduct().getDimensions());
//			lblQuantity.setText("Quantity: " + orderDetail.getQuantity());
//			lblDescription.setText(orderDetail.getProduct().getDescription());
//			lblAddress.setText(orderDetail.getShippingAddress().toString());
//			
//			if (buyerView) {
//				lblUser.setText("Bougth From: " + orderDetail.getSeller().getUsername());
//				lblTrackingNum.setText(orderDetail.getTrackingNumber());
//				
//				lblTrackingNum.setVisibility(View.VISIBLE);
//				txtTrackingNum.setVisibility(View.GONE);
//				btnUpdateTrackingNum.setVisibility(View.GONE);
//			} else {
//				lblUser.setText("Sold To: " + orderDetail.getBuyer().getUsername());
//				txtTrackingNum.setText(orderDetail.getTrackingNumber());
//				
//				lblTrackingNum.setVisibility(View.GONE);
//				txtTrackingNum.setVisibility(View.VISIBLE);
//				btnUpdateTrackingNum.setVisibility(View.VISIBLE);
//			}
//		}
	}
}

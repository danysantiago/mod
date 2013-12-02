package icom5016.modstore.fragments;


import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.dialog.DateTimePickerDialog;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.Product;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SellProductFragment extends Fragment {
	
	//Vars
	private EditText txtName;
	private EditText txtDescription;
	private EditText txtBrand;
	private EditText txtModel;
	private EditText txtDimensions;
	private EditText txtQuantity;
	private EditText txtBuyoutPrice;
	private EditText txtBidPrice;
	private EditText txtEndAuction;
	private Spinner cboCategory;
	private Button btnSelectPhoto;
	private Button btnAdd;
	private CheckBox chkAuctionEnabled;
	
	private Calendar myCalendar = Calendar.getInstance();
	private OnDateChangedListener dateChangedListener;
	private OnTimeChangedListener timeSetListener;
	private DialogInterface.OnClickListener onDialogSet, onDialogCancel;
	
	private Uri selectedPhoto;
	@SuppressWarnings("unused")
	private byte selectedPhotoBytes[];
	
	private Product product;
	
	private MainActivity myActivity;
	private List<Category> listingCat;
	
	private static final int SELECT_PICTURE = 1;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sellproduct, container, false);
		if (savedInstanceState != null) Log.d("productStatus", savedInstanceState.toString());

		
		
		Bundle b = this.getArguments();
		if (b != null && b.containsKey(ConstantClass.PRODUCT_SELL_PROD_KEY)) { 
			product = (Product)b.getSerializable(ConstantClass.PRODUCT_SELL_PROD_KEY);
		}
		
		myActivity = (MainActivity) this.getActivity();
		
		txtName = (EditText)view.findViewById(R.id.txtProductName);
		txtDescription = (EditText)view.findViewById(R.id.txtProductDescription);
		txtBrand = (EditText)view.findViewById(R.id.txtProductBrand);
		txtModel = (EditText)view.findViewById(R.id.txtProductModel);
		txtDimensions = (EditText)view.findViewById(R.id.txtProductDimensions);
		txtQuantity = (EditText)view.findViewById(R.id.txtProductQuantity);
		txtBuyoutPrice = (EditText)view.findViewById(R.id.txtProductBuyoutPrice);
		txtBidPrice = (EditText)view.findViewById(R.id.txtProductBidPrice);
		txtEndAuction = (EditText)view.findViewById(R.id.txtProductEndAuction);
		cboCategory = (Spinner)view.findViewById(R.id.cboProductCategory);
		btnSelectPhoto = (Button)view.findViewById(R.id.btnProductSelectPhoto);
		btnAdd = (Button)view.findViewById(R.id.btnProductAdd);
		chkAuctionEnabled = (CheckBox)view.findViewById(R.id.chkAuctionEnabled);
		
		dateChangedListener = new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}
		};
		
		timeSetListener = new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
			}
		};
		
		onDialogSet = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					txtName.requestFocus();
					updateLabel();
			}
		};
		
		onDialogCancel = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					txtName.requestFocus();
			}
		};
		
		txtEndAuction.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment d = new DateTimePickerDialog();
					((DateTimePickerDialog)d).setOnTimeListener(timeSetListener);
					((DateTimePickerDialog)d).setOnDateListener(dateChangedListener);
					((DateTimePickerDialog)d).setOnSet(onDialogSet);
					((DateTimePickerDialog)d).setOnCancel(onDialogCancel);
					((DateTimePickerDialog)d).setCalendar(myCalendar);
					
					d.show(myActivity.getFragmentManager(), "NewDateTimePicker");
				}
			}
			
		});
		
		chkAuctionEnabled.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					txtQuantity.setEnabled(false);
					txtQuantity.setText("1");
					txtEndAuction.setEnabled(true);
					txtBidPrice.setEnabled(true);
					txtBidPrice.requestFocus();
				} else {
					txtQuantity.setEnabled(true);
					txtBidPrice.setText("");
					txtEndAuction.setText("");
					txtEndAuction.setEnabled(false);
					txtBidPrice.setEnabled(false);
				}
			}
			
		});

		btnSelectPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
			}
		});
		
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// User wants to add. Do a PUT to Node or whatever. 
			}
		});
		
		txtEndAuction.setEnabled(false);
		txtBidPrice.setEnabled(false);

		this.load();
		
		
		return view;
	}
	
	private void load() {
		myActivity.loadAllCategories();
		this.listingCat = myActivity.loadCategoriesById(ConstantClass.CategoriesFile.ALL_CATEGORIES);
		List<Category> cats = AndroidResourceFactory.sortCategories(listingCat);
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(myActivity, android.R.layout.simple_list_item_1, cats);
	    cboCategory.setAdapter(adapter);
		this.loadProduct();
		
	}

	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedPhoto = data.getData();
                (Toast.makeText(myActivity, "Image location: " + selectedPhoto.toString(), Toast.LENGTH_LONG)).show();
                
                // FOR THE FUTURE: How to convert this image Uri to Byte Array (to Upload it to server)

				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(myActivity.getContentResolver(), selectedPhoto);
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                selectedPhotoBytes= stream.toByteArray();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
    
	private void loadProduct() {
		if (product != null) {
			txtName.setText(product.getName());
			txtDescription.setText(product.getDescription());
			txtBrand.setText(product.getBrand());
			txtModel.setText(product.getModel());
			txtDimensions.setText(product.getDimensions());
			txtQuantity.setText(String.valueOf(product.getQuantity()));
			txtBuyoutPrice.setText((product.getBuyItNowPrice() != -1) ? String.valueOf(product.getBuyItNowPrice()) : "");
			
			if (product.getStartingBidPrice() != -1) {
				chkAuctionEnabled.setChecked(true);
				txtBidPrice.setText(String.valueOf(product.getStartingBidPrice()));
				myCalendar.setTime(product.getAuctionEndDate());
				updateLabel();
				txtEndAuction.setEnabled(true);
			} else {
				txtEndAuction.setEnabled(false);
			}
			
			SpinnerAdapter tempAdapter = cboCategory.getAdapter();
			
			for (int i = 0; i < tempAdapter.getCount(); i++) {
				Category tempCat = (Category)tempAdapter.getItem(i);
				if (tempCat.getId() == product.getCategoryId()) {
					cboCategory.setSelection(i);
					break;
				}
			}
			
			btnAdd.setText(R.string.product_updateit);
			
			// Disable things that cant be edited. 
			txtQuantity.setEnabled(false);
			txtBidPrice.setEnabled(false);
			chkAuctionEnabled.setEnabled(false);
		}
		
	}
	
	private void updateLabel() {
		String myFormat = "MM/dd/yyyy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		txtEndAuction.setText(sdf.format(myCalendar.getTime()));
		txtName.requestFocus();
	}
	
}

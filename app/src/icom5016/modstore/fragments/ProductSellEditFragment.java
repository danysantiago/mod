package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.Product;
import icom5016.modstore.uielements.DateTimePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class ProductSellEditFragment extends Fragment {
	EditText txtName;
	EditText txtDescription;
	EditText txtBrand;
	EditText txtModel;
	EditText txtDimensions;
	EditText txtQuantity;
	EditText txtBuyoutPrice;
	EditText txtBidPrice;
	EditText txtEndAuction;
	Spinner cboCategory;
	Button btnSelectPhoto;
	Button btnAdd;
	CheckBox chkAuctionEnabled;
	
	Calendar myCalendar = Calendar.getInstance();
	OnDateChangedListener dateChangedListener;
	OnTimeChangedListener timeSetListener;
	DialogInterface.OnClickListener onDialogClose;
	
	Uri selectedPhoto;
	byte selectedPhotoBytes[];
	
	ProgressDialog pd;
	
	Product product;
	
	private static final int SELECT_PICTURE = 1;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if (savedInstanceState != null) Log.d("productStatus", savedInstanceState.toString());
		View view = inflater.inflate(R.layout.fragment_sellproduct, container, false);
		
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
		
		onDialogClose = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					txtName.requestFocus();
					updateLabel();
			}
		};
		
		txtEndAuction.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment d = new DateTimePicker();
					((DateTimePicker)d).setOnTimeListener(timeSetListener);
					((DateTimePicker)d).setOnDateListener(dateChangedListener);
					((DateTimePicker)d).setOnSet(onDialogClose);
					((DateTimePicker)d).setOnCancel(onDialogClose);
					((DateTimePicker)d).setCalendar(myCalendar);
					
					d.show(getActivity().getFragmentManager(), "NewDateTimePicker");
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
		
		cboCategory.setVisibility(View.GONE);
		txtEndAuction.setEnabled(false);
		txtBidPrice.setEnabled(false);
		
		pd = ProgressDialog.show(getActivity(), "Loading", "Loading Categories...", true, false);

		requestCategories();
		
		return view;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	private void loadProduct() {
		if (product != null) {
			txtName.setText(product.getName());
			txtDescription.setText(product.getDescription());
			txtBrand.setText(product.getBrand());
			txtModel.setText(product.getModel());
			txtDimensions.setText(product.getDimensions());
			txtQuantity.setText(String.valueOf(product.getQuantity()));
//			txtBuyoutPrice.setText(String.valueOf(product.getPriceNumber()));
//			if (product.getBid_price() != -1) {
//				txtBidPrice.setText(String.valueOf(product.getBid_price()));
//			}
//			txtEndAuction.setText(product.getAuction_ends());
//			
			SpinnerAdapter tempAdapter = cboCategory.getAdapter();
			
			for (int i = 0; i < tempAdapter.getCount(); i++) {
				Category tempCat = (Category)tempAdapter.getItem(i);
//				if (tempCat.getId() == product.getCid()) {
//					cboCategory.setSelection(i);
//					break;
//				}
			}
			
			btnAdd.setText(R.string.product_updateit);
			
			// Disable things that cant be edited. 
			txtQuantity.setEnabled(false);
			txtBidPrice.setEnabled(false);
		}
	}
	
	private void updateLabel() {
		String myFormat = "MM/dd/yyyy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		txtEndAuction.setText(sdf.format(myCalendar.getTime()));
		txtName.requestFocus();
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedPhoto = data.getData();
                (Toast.makeText(getActivity(), "Image location: " + selectedPhoto.toString(), Toast.LENGTH_LONG)).show();
                
                // FOR THE FUTURE: How to convert this image Uri to Byte Array (to Upload it to server)

				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedPhoto);
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

	private void requestCategories() {
		//Perform http request
		Bundle params = new Bundle();
		
		params.putString("method", "GET");
		params.putString("url", Server.Categories.GETALL);
		
		HttpRequest request = new HttpRequest(params, new HttpCallback() {
			@Override
			public void onSucess(JSONObject json) {
				List<Category> cats = getCategories(json);
				
				if (cats.size() > 0) {
					if (getActivity() != null) {
						//Pass JSON to Adapter
						ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_list_item_1, cats);
					    cboCategory.setAdapter(adapter);
		
						//Show list view
						cboCategory.setVisibility(View.VISIBLE);
						
						pd.dismiss();
					} else {
						pd.dismiss();
						//Toast.makeText(this, "There was a problem loading the Categories. [ERR: 1]", Toast.LENGTH_SHORT).show();
					}
				} else {
					pd.dismiss();
					Toast.makeText(getActivity(), "No Categories were found.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailed() {
				pd.dismiss();
				Toast.makeText(getActivity(), "Couldn't load the Categories [ERR: 1]", Toast.LENGTH_SHORT).show();
			}
		});
		
		request.execute();
	}
	
	private List<Category> getCategories(JSONObject json) {
		List<Category> cats = new ArrayList<Category>();
		List<Category> outCats = new ArrayList<Category>();
		JSONArray jsonArr;
		
		try {
			jsonArr = json.getJSONArray("categories");
			
			for (int i = 0; i < jsonArr.length(); i++) {
				cats.add(new Category(jsonArr.getJSONObject(i)));
			}
			
			getCategoriesRecurv(cats, outCats, 0, -1);
			return outCats;
		} catch (JSONException e) {
			e.printStackTrace();
			
			return new ArrayList<Category>();
		}
	}
	
	private void getCategoriesRecurv(List<Category> inCats, List<Category> cats, int level, int lookId) {
		String name;

		for (Category c : inCats) {
			if (c.getParentId() == lookId) {
				name = repeat("   ", level) + c.getName();
				
				cats.add(new Category(c.getParentId(), c.getId(), name));
				
				getCategoriesRecurv(inCats, cats, level + 1, c.getId());
			}
		}
	}
	
	private String repeat(String c, int n) {
		String out = "";
		
		for (int i = 0; i < n; i++) {
			out += c;
		}
		
		return out;
	}
}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.R;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.resources.DataFetchFactory;

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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProductEditFragment extends Fragment {
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
	
	Calendar myCalendar = Calendar.getInstance();
	OnDateSetListener dateSetListener;
	
	Uri selectedPhoto;
	byte selectedPhotoBytes[];
	
	private static final int SELECT_PICTURE = 1;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_productedit, container, false);
		
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
		
		dateSetListener = new DatePickerDialog.OnDateSetListener() {
		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		        myCalendar.set(Calendar.YEAR, year);
		        myCalendar.set(Calendar.MONTH, monthOfYear);
		        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        updateLabel();
		    }
		};
		
		txtEndAuction.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					new DatePickerDialog(getActivity(), dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
		
		cboCategory.setVisibility(View.GONE);
		
		requestCategories();
		
		return view;
	}
	
	private void updateLabel() {
		String myFormat = "MM/dd/yyyy";
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
					//Pass JSON to Adapter
					ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_list_item_1, cats);
				    cboCategory.setAdapter(adapter);
	
					//Show list view
					cboCategory.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Couldn't load the Categories [ERR: 1]", Toast.LENGTH_SHORT).show();
			}
		});
		
		request.execute();
	}
	
	private List<Category> getCategories(JSONObject json) {
		List<Category> cats = new ArrayList<Category>();
		getCategoriesRecurv(json, cats, 0, -1);
		return cats;
	}
	
	private void getCategoriesRecurv(JSONObject json, List<Category> cats, int level, int lookId) {
		JSONArray jsonArr;
		JSONObject obj;
		String name;
		int parentId, id;

		try {
			jsonArr = json.getJSONArray("categories");
			
			for (int i = 0; i < jsonArr.length(); i++) {
				obj = jsonArr.getJSONObject(i);
				parentId = obj.getInt("parentId");
				
				if (parentId == lookId) {
					name = obj.getString("name");
					id = obj.getInt("id");
					
					name = repeat("   ", level) + name;
					
					cats.add(new Category(parentId, id, name));
					
					getCategoriesRecurv(json, cats, level + 1, id);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
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

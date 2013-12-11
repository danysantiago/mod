package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.dialog.DateTimePickerDialog;
import icom5016.modstore.http.HttpRequest;
import icom5016.modstore.http.HttpRequest.HttpCallback;
import icom5016.modstore.http.MyMultipartEntity;
import icom5016.modstore.http.MyMultipartEntity.ProgressListener;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.Category;
import icom5016.modstore.models.Product;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.AndroidResourceFactory;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SellProductFragment extends Fragment {

	// Vars
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
	private ImageView imageView;

	private Calendar myCalendar = Calendar.getInstance();
	private OnDateChangedListener dateChangedListener;
	private OnTimeChangedListener timeSetListener;
	private DialogInterface.OnClickListener onDialogSet, onDialogCancel;

	private String selectedPhoto;

	private Product product;

	private MainActivity myActivity;
	private List<Category> listingCat;
	
	private User u;
	private boolean isEditing = false;

	private static final int SELECT_PICTURE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sellproduct, container,
				false);
		if (savedInstanceState != null)
			Log.d("productStatus", savedInstanceState.toString());

		Bundle b = this.getArguments();
		if (b != null && b.containsKey(ConstantClass.PRODUCT_SELL_PROD_KEY)) {
			product = (Product) b
					.getSerializable(ConstantClass.PRODUCT_SELL_PROD_KEY);
		}

		myActivity = (MainActivity) this.getActivity();

		u = DataFetchFactory.getUserFromSPref(getActivity());
		
		txtName = (EditText) view.findViewById(R.id.txtProductName);
		txtDescription = (EditText) view
				.findViewById(R.id.txtProductDescription);
		txtBrand = (EditText) view.findViewById(R.id.txtProductBrand);
		txtModel = (EditText) view.findViewById(R.id.txtProductModel);
		txtDimensions = (EditText) view.findViewById(R.id.txtProductDimensions);
		txtQuantity = (EditText) view.findViewById(R.id.txtProductQuantity);
		txtBuyoutPrice = (EditText) view
				.findViewById(R.id.txtProductBuyoutPrice);
		txtBidPrice = (EditText) view.findViewById(R.id.txtProductBidPrice);
		txtEndAuction = (EditText) view.findViewById(R.id.txtProductEndAuction);
		cboCategory = (Spinner) view.findViewById(R.id.cboProductCategory);
		btnSelectPhoto = (Button) view.findViewById(R.id.btnProductSelectPhoto);
		btnAdd = (Button) view.findViewById(R.id.btnProductAdd);
		chkAuctionEnabled = (CheckBox) view
				.findViewById(R.id.chkAuctionEnabled);
		imageView = (ImageView) view.findViewById(R.id.imageView);

		dateChangedListener = new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
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
					((DateTimePickerDialog) d)
							.setOnTimeListener(timeSetListener);
					((DateTimePickerDialog) d)
							.setOnDateListener(dateChangedListener);
					((DateTimePickerDialog) d).setOnSet(onDialogSet);
					((DateTimePickerDialog) d).setOnCancel(onDialogCancel);
					((DateTimePickerDialog) d).setCalendar(myCalendar);

					d.show(myActivity.getFragmentManager(), "NewDateTimePicker");
				}
			}

		});

		chkAuctionEnabled
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
				if (selectedPhoto == null) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							SELECT_PICTURE);
				} else {
					selectedPhoto = null;
					imageView.setVisibility(View.GONE);
					btnSelectPhoto.setText("Select a Photo");
				}
			}
		});

		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isEditing) {
					uploadProduct();
				} else {
					try {
						editProductHttp();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

		txtEndAuction.setEnabled(false);
		txtBidPrice.setEnabled(false);

		imageView.setVisibility(View.GONE);

		this.load();

		return view;
	}

	private void load() {
		myActivity.loadAllCategories();
		this.listingCat = myActivity
				.loadCategoriesById(ConstantClass.CategoriesFile.ALL_CATEGORIES);
		List<Category> cats = AndroidResourceFactory.sortCategories(listingCat);
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(myActivity,
				android.R.layout.simple_list_item_1, cats);
		cboCategory.setAdapter(adapter);
		this.loadProduct();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				selectedPhoto = getRealPathFromURI(getActivity(),
						data.getData());

				Toast.makeText(myActivity,
						"Image location: " + selectedPhoto.toString(),
						Toast.LENGTH_LONG).show();

				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(
							myActivity.getContentResolver(), data.getData());
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);

					btnSelectPhoto.setText("Remove Photo");
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

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
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
			txtBuyoutPrice.setText((product.getBuyItNowPrice() != -1) ? String
					.valueOf(product.getBuyItNowPrice()) : "");

			if (product.getStartingBidPrice() != -1) {
				chkAuctionEnabled.setChecked(true);
				txtBidPrice.setText(String.valueOf(product
						.getStartingBidPrice()));
				myCalendar.setTime(product.getAuctionEndDate());
				updateLabel();
				txtEndAuction.setEnabled(true);
			} else {
				txtEndAuction.setEnabled(false);
			}

			SpinnerAdapter tempAdapter = cboCategory.getAdapter();

			for (int i = 0; i < tempAdapter.getCount(); i++) {
				Category tempCat = (Category) tempAdapter.getItem(i);
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
			
			isEditing  = true;
		}

	}

	private void updateLabel() {
		String myFormat = "MM/dd/yyyy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		txtEndAuction.setText(sdf.format(myCalendar.getTime()));
		txtName.requestFocus();
	}

	private void uploadProduct() {
		HttpUpload request = new HttpUpload(getActivity());
		request.execute();
	}
	
	private void editProductHttp() throws JSONException {
		String name = txtName.getText().toString();
		String description = txtDescription.getText().toString();
		String brand = txtBrand.getText().toString();
		String model = txtModel.getText().toString();
		String dimensions = txtDimensions.getText().toString();
		String category = "" + (cboCategory.getSelectedItemPosition()+1);
		
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("description", description);
		json.put("brand", brand);
		json.put("model", model);
		json.put("dimensions", dimensions);
		json.put("category_id", category);
		json.put("product_id", product.getId());

		Bundle params = new Bundle();
		params.putString("method", "PUT");
		params.putString("url", Server.Products.UPDATE);
		
		HttpRequest request = new HttpRequest(params, json, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				Toast.makeText(getActivity(), "Product Updated Sucessfuly", Toast.LENGTH_SHORT).show();
				MainActivity ma = (MainActivity) getActivity();
				ma.onBackPressed();
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Product Update Failed", Toast.LENGTH_SHORT).show();
			}
		});
		request.execute();
		
	}

	public class HttpUpload extends AsyncTask<Void, Integer, Void> {

		private Context context;

		private HttpClient client;

		private ProgressDialog pd;
		private long totalSize;
		
		private boolean sucess = false;

		private static final String url = Server.Products.ADD;

		public HttpUpload(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// Set timeout parameters
			int timeout = 10000;
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpConnectionParams.setSoTimeout(httpParameters, timeout);

			// We'll use the DefaultHttpClient
			client = new DefaultHttpClient(httpParameters);

			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("Uploading Picture...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String name = txtName.getText().toString();
			String description = txtDescription.getText().toString();
			String brand = txtBrand.getText().toString();
			String model = txtModel.getText().toString();
			String dimensions = txtDimensions.getText().toString();
			String quantity = txtQuantity.getText().toString();
			String category = "" + (cboCategory.getSelectedItemPosition()+1);
			String buyPrice = txtBuyoutPrice.getText().toString();
			String startingPrice = null;
			String ends = null;
			
			if(chkAuctionEnabled.isChecked()) {
				startingPrice = txtBidPrice.getText().toString();
				ends = "" + myCalendar.get(Calendar.YEAR) + "-" + (myCalendar.get(Calendar.MONTH)+1) + "-" +
						myCalendar.get(Calendar.DAY_OF_MONTH) + " " + myCalendar.get(Calendar.HOUR_OF_DAY) + ":" +
						myCalendar.get(Calendar.MINUTE) + ":00";
			}
			
			if(buyPrice.length() == 0) {
				buyPrice = null;
			}
			
			
			try {
				// Create the POST object
				HttpPost post = new HttpPost(url);

				// Create the multipart entity object and add a progress listener
				// this is a our extended class so we can know the bytes that
				// have been transfered
                MultipartEntity entity = new MyMultipartEntity(new ProgressListener() {
	                @Override
	                public void transferred(long num) {
	                    //Call the onProgressUpdate method with the percent completed
	                    publishProgress((int) ((num / (float) totalSize) * 100));
	                    Log.d("DEBUG", num + " - " + totalSize);
	                }
                });

				// Add the file to the content's body
				if(selectedPhoto != null) {
					
					File tempFile = null;
	                File photoFile = new File(selectedPhoto);
	                
	                if(photoFile.length() > 1048576){

	                    int scaleFactor = 4;
	
	                    // Get the dimensions of the bitmap
	                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	                    bmOptions.inJustDecodeBounds = true;
	                    BitmapFactory.decodeFile(selectedPhoto, bmOptions);
	                    int photoW = bmOptions.outWidth;
	                    int photoH = bmOptions.outHeight;
	
//	                    int targetW = (int) (photoW * (1.0-((float) compressRatio)/100.0));
//	                    int targetH = (int) (photoH * (1.0-((float) compressRatio)/100.0));

	                    // Determine how much to scale down the image
//	                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	                    Log.d("DEBUG", "Scale factor: " + scaleFactor);
	
	                    // Decode the image file into a Bitmap sized to fill the View
	                    bmOptions.inJustDecodeBounds = false;
	                    bmOptions.inSampleSize = scaleFactor;
	                    bmOptions.inPurgeable = true;
	
	                    Bitmap bitmap = BitmapFactory.decodeFile(selectedPhoto, bmOptions);
	
	                    File dirFile = new File(Environment.getExternalStorageDirectory() + "/.mod");
	                    if(!dirFile.exists()) {
                            dirFile.mkdir();
	                    }
	
	                    tempFile = new File(Environment.getExternalStorageDirectory() + "/.mod/temp");
	
	                    try {
                            int imgQlty = scaleFactor == 3 ? 96 : 100;
                            bitmap.compress(CompressFormat.JPEG, imgQlty, new FileOutputStream(tempFile));
	                    } catch (FileNotFoundException e) {
                            Log.e("DbUploadPic", "Error compressing photo.", e);
	                    }
	                    
	                    Log.d("DEBUG", "Compressed from " + photoFile.length() +" to " + tempFile.length());

	                } else {
                        tempFile = photoFile;
	                }
	                
					ContentBody cbFile = new FileBody(tempFile, "image/jpeg");
					entity.addPart("image", cbFile);
				}

				entity.addPart("user_id", new StringBody(""+u.getGuid()));
				entity.addPart("category_id", new StringBody(category));
				entity.addPart("description", new StringBody(description));
				entity.addPart("name", new StringBody(name));
				entity.addPart("brand", new StringBody(brand));
				entity.addPart("model", new StringBody(model));
				entity.addPart("dimensions", new StringBody(dimensions));
				entity.addPart("quantity", new StringBody(quantity));
				
				if(buyPrice != null) {
					entity.addPart("buy_price", new StringBody(buyPrice));
				}
				
				if(startingPrice != null) {
					entity.addPart("starting_bid_price", new StringBody(startingPrice));
				}
				
				if(ends != null) {
					entity.addPart("auction_end_ts", new StringBody(ends));
				}

				// After adding everything we get the content's lenght
				totalSize = entity.getContentLength();

				// We add the entity to the post request
				post.setEntity(entity);

				// Execute post request
				HttpResponse response = client.execute(post);
				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// If everything goes ok, we can get the response
					String fullRes = EntityUtils.toString(response.getEntity());
					Log.d("DEBUG", fullRes);
					
					sucess = true;

				} else {
					Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
				}

			} catch (ClientProtocolException e) {
				// Any error related to the Http Protocol (e.g. malformed url)
				e.printStackTrace();
			} catch (IOException e) {
				// Any IO error (e.g. File not found)
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Set the pertange done in the progress dialog
			pd.setProgress((int) (progress[0]));
		}

		@Override
		protected void onPostExecute(Void result) {
			// Dismiss progress dialog
			pd.dismiss();
			
			if(sucess) {
				Toast.makeText(getActivity(), "Product Sucessfully Created", Toast.LENGTH_SHORT).show();
				MainActivity ma = (MainActivity) getActivity();
				ma.onBackPressed();
			} else {
				Toast.makeText(getActivity(), "Create Product Failed", Toast.LENGTH_SHORT).show();
			}
		}

	}

}

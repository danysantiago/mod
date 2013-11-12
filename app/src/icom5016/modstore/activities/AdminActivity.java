package icom5016.modstore.activities;

import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminActivity extends MainInterfaceActivity {
	
	private ListView optionsListView;
	
	private User user;

	private String[] adminOptions = {"Users", "Sales by Time", "Sales by Products", "Revenue by Products"};
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_admin);
	
		user = DataFetchFactory.getUserFromSPref(this);
		
		if(!user.isAdmin()) {
			Toast.makeText(this, "You don't have admin privileges", Toast.LENGTH_LONG).show();
			finish();
		}
		
		optionsListView = (ListView) findViewById(R.id.optionsListView);
		
		optionsListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adminOptions ));
		
		optionsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
				switch (pos) {
				case 1:
					showSalesByTimeDialog();
					break;
				case 2:
					showSalesByProductDialog();
					break;
				default:
					break;
				}
				
			}


		});
	}
	
	private void showSalesByTimeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sales by Time");
        builder.setPositiveButton("By Months", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.SALES_BY_MONTH);
	       }

	    });
        builder.setNeutralButton("By Weeks", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.SALES_BY_WEEK);
	       }
	    });
		builder.setNegativeButton("By Days", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.SALES_BY_DAY);
	       }
	    });
		builder.show();
	}
	
	private void showSalesByProductDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Sales by Products");
	    builder.setPositiveButton("Last 6 Months", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.PRODUCTS_BY_MONTH);
	       }
	
	    });
	    builder.setNeutralButton("Last 7 Weeks", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.PRODUCTS_BY_WEEK);
	       }
	    });
		builder.setNegativeButton("Last 14 Days", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
			   showZoomImageActivity(Server.Charts.PRODUCTS_BY_DAY);
	       }
	    });
		builder.show();
	}
	
	private void showZoomImageActivity(String urlForImage) {
		Intent  i = new Intent(this, TouchImageActivity.class);
		i.putExtra("imageUrl", urlForImage);
		startActivity(i);
	}
}

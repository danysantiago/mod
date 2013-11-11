package icom5016.modstore.activities;

import icom5016.modstore.http.ImageLoader;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class AdminActivity extends MainInterfaceActivity {
	
	private ListView optionsListView;
	
	private User user;

	private String[] adminOptions = {"Users", "Sales by Time", "Sales by Products", "Revenue by Products"};

	private ImageLoader imageLoader;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_admin);
		
		imageLoader = new ImageLoader(this);
		
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
			   showImageDialog(Server.Charts.SALES_BY_MONTH);
	       }

	    });
        builder.setNeutralButton("By Weeks", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
	       }
	    });
		builder.setNegativeButton("By Days", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int id) {
	       }
	    });
		builder.show();
	}
	
	private void showImageDialog(String salesByMonth) {
		ImageView chart = new ImageView(context);
		imageLoader.DisplayImage(Server.Charts.SALES_BY_MONTH, chart);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(chart);
		builder.show();
		
	}
}

package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.activities.TouchImageActivity;
import icom5016.modstore.http.Server;
import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminFragment extends Fragment {

	private ListView optionsListView;
	private User user;
	private String[] adminOptions = {"Users", "Sales by Time", "Sales by Products", "Revenue by Products"};
	private MainActivity ma;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_admin, container, false);
		
		this.ma = (MainActivity) this.getActivity();
		user = DataFetchFactory.getUserFromSPref(ma);
		
		if(!user.isAdmin()) {
			Toast.makeText(ma, "You don't have admin privileges", Toast.LENGTH_LONG).show();
			ma.finish();
		}
		
		optionsListView = (ListView) view.findViewById(R.id.optionsListView);
		
		optionsListView.setAdapter(new ArrayAdapter<String>(ma, android.R.layout.simple_list_item_1, adminOptions ));
		
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
		return view;
	}
	
	private void showSalesByTimeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ma);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(ma);
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
		Intent  i = new Intent(ma, TouchImageActivity.class);
		i.putExtra("imageUrl", urlForImage);
		startActivity(i);
	}
}

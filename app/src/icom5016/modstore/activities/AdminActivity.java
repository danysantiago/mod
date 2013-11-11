package icom5016.modstore.activities;

import icom5016.modstore.models.User;
import icom5016.modstore.resources.DataFetchFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminActivity extends MainInterfaceActivity {
	
	private ListView optionsListView;
	
	private User user;

	private String[] adminOptions = {"Users", "Sales by Time", "Sales by Products", "Revenue by Products"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_admin);
		
		user = DataFetchFactory.getUserFromSPref(this);
		
		if(!user.isAdmin()) {
			Toast.makeText(this, "You don't have admin privileges", Toast.LENGTH_LONG).show();
			finish();
		}
		
		optionsListView = (ListView) findViewById(R.id.optionsListView);
		
		optionsListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adminOptions ));
	}
}

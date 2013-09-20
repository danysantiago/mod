package edu.uprm.mod.store;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
	
	/* Global Elements to be Used */
	Button btnLogin;
	TextView txtUsername, txtPassword;
	Context context;
	ActionBar actionBar;	
	MenuItem searchItem;
	SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		txtUsername = (TextView)findViewById(R.id.txtUsername);
		txtPassword = (TextView)findViewById(R.id.txtPassword);
		context = getApplicationContext();
		
		btnLogin.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View v) {
					Toast t = Toast.makeText(context, "User: " + txtUsername.getText().toString() + "\nPassword: " + txtPassword.getText().toString(), Toast.LENGTH_SHORT);
					t.show();
					
					//Starts activity
					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(i);
				}
			});
		
		Toast t = Toast.makeText(getApplicationContext(), "Application load!", Toast.LENGTH_SHORT);
		t.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_menu, menu);
	    
		actionBar = getActionBar();
		searchItem = (MenuItem)findViewById(R.id.action_search);
		searchView = new SearchView(this);
		searchView.setQueryHint("Search for products...");
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				Toast t = Toast.makeText(context, "Search text: " + query, Toast.LENGTH_SHORT);
				t.show();
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		menu.getItem(0).setActionView(searchView);
		
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}

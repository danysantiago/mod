package edu.uprm.mod.store;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExampleFragment extends Fragment {
	
	private Context context;
	
	private TextView responseTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		//On Create View is executed when the Fragment is being loaded, here we just inflate our desired XML layout
		return inflater.inflate(R.layout.fragment_example, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//Once the view has been created we can execute code here with constructed View objects.
		super.onViewCreated(view, savedInstanceState);
		
		context = getActivity();
		
		responseTextView = (TextView) getActivity().findViewById(R.id.response_textView);
		
		new HttpRequest().execute();
	}
	
	private class HttpRequest extends AsyncTask<Void, Void, Void>{
		
		private DefaultHttpClient client;
		
		private boolean success;
		private String errorMsg;
		
		private String exampleMsg;
		
		@Override
		protected void onPreExecute() {
			// We initialize things here
			
			// We specify connection parameters, timeout
			int timeout = 10000;
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpConnectionParams.setSoTimeout(httpParameters, timeout);

			client = new DefaultHttpClient(httpParameters);
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// Background method, request is processed here
			try {
			
				HttpGet get = new HttpGet(Server.BASE_URL + "/example");
				HttpResponse response = client.execute(get); //Execute get
				
				int statusCode = response.getStatusLine().getStatusCode(); // Get http status code
				
				if(statusCode == HttpStatus.SC_OK){
					String fullRes = EntityUtils.toString(response.getEntity()); // Get response full string
					
					JSONObject json = new JSONObject(fullRes);
					exampleMsg = json.getString("message");
					
					success = true;
				} else {
					success = false;
				}
			
			} catch (JSONException e) {
				Log.d("JSONLog", "Error Example JSON object", e);
			} catch (ClientProtocolException e) {
				success = false;
				Log.d("BackendEventLog", "Error Example GET", e);
			} catch (IOException e) {
				success = false;
				Log.d("BackendEventLog", "Error Example GET", e);
			}
			
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			//We clean up things here, we also run GUI code here because this method is run by the GUI's thread
			//else we would get security error
			
			if(success){ 
				responseTextView.setText(exampleMsg);
			} else {
				//Something went wrong, show toast message
				Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}

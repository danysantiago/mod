package edu.uprm.mod.store;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;

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

import edu.uprm.mod.store.HttpRequest.HttpCallback;

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
		
		Bundle httpParams = new Bundle();
		httpParams.putString("method", "GET");
		httpParams.putString("url", Server.BASE_URL + "/example");
		HttpRequest request = new HttpRequest(httpParams, new HttpCallback() {
			
			@Override
			public void onSucess(JSONObject json) {
				try {
					responseTextView.setText(json.getString("message"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed() {
				Toast.makeText(getActivity(), "Something bad happened,  http-wise.", Toast.LENGTH_SHORT).show();
			}
		});
		request.execute();
	}

}

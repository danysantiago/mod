package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddressAdapter extends ArrayAdapter<Address> {
	Context context;
    int layoutResourceId;   

    public AddressAdapter(Context context, int layoutResourceId, JSONObject json) {
        super(context, layoutResourceId);

		try {
			JSONArray jsonArr = json.getJSONArray("addresses");
			
			for (int i = 0; i < jsonArr.length(); i++) {
				this.add(new Address(jsonArr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Toast.makeText(context, "Couldn't load the Addresses [ERR: 1]", Toast.LENGTH_SHORT).show();
		}
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AddressHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new AddressHolder();
            holder.lblLine1 = (TextView)row.findViewById(R.id.lblAddrAddressLine1);
            holder.lblLine2 = (TextView)row.findViewById(R.id.lblAddrAddressLine2);
            holder.lblLocation = (TextView)row.findViewById(R.id.lblAddrLocation);
            holder.lblCountry = (TextView)row.findViewById(R.id.lblAddrCountry);
            holder.imgDefault = (ImageView)row.findViewById(R.id.imgAddrDefault);
           
            row.setTag(holder);
        } else {
            holder = (AddressHolder) row.getTag();
        }
       
        Address addr = this.getItem(position);
        holder.lblLine1.setText(addr.getLine1());
        holder.lblLine2.setText(addr.getLine2());
        holder.lblLocation.setText(addr.getLocation());
        holder.lblCountry.setText(addr.getCountry());
        
        if (addr.isDefault()) {
        	holder.imgDefault.setVisibility(View.VISIBLE);
        } else {
        	holder.imgDefault.setVisibility(View.GONE);
        }

        return row;
    }
   
    public static class AddressHolder {
        TextView lblLine1;
        TextView lblLine2;
        TextView lblLocation;
        TextView lblCountry;
        ImageView imgDefault;
    }
}
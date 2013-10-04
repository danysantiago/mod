package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.CreditCard;

import java.util.ArrayList;

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

public class CreditCardAdapter extends ArrayAdapter<CreditCard> {
	Context context;
    int layoutResourceId;   
    ArrayList<CreditCard> data = null;
    
    String error;
    public CreditCardAdapter(Context context, int layoutResourceId, JSONObject jsonObj) {
        super(context, layoutResourceId);
        
		ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
		
		try {
			JSONArray jsonArr = jsonObj.getJSONArray("creditcards");
			
			for (int i = 0; i < jsonArr.length(); i++) {
				creditCards.add(new CreditCard(jsonArr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			error = e.getMessage();
		}
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = creditCards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CreditCardHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new CreditCardHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtCCName);
            holder.txtNumber = (TextView)row.findViewById(R.id.txtCCNumber);
            holder.txtExpire = (TextView)row.findViewById(R.id.txtCCExpire);
            
            //holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            //holder.txtName = (TextView)row.findViewById(R.id.;
           
            row.setTag(holder);
        } else {
            holder = (CreditCardHolder)row.getTag();
        }
       
        CreditCard creditCard = data.get(position);
        holder.txtName.setText(creditCard.name);
        holder.txtNumber.setText(creditCard.number);
        holder.txtExpire.setText(creditCard.expire);
        //holder.imgIcon.setImageResource(weather.icon);
       
        return row;
    }
   
    static class CreditCardHolder {
        ImageView imgCreditCard;
        TextView txtName;
        TextView txtNumber;
        TextView txtExpire;
    }
}

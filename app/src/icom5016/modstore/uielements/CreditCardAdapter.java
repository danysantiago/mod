package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.fragments.CreditCardsFragment;
import icom5016.modstore.models.CreditCard;
import icom5016.modstore.resources.DataFetchFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardAdapter extends ArrayAdapter<CreditCard> {
	Context context;
    int layoutResourceId;   
    int creditCardImages[];

    public CreditCardAdapter(Context context, int layoutResourceId, JSONObject json) {
        super(context, layoutResourceId);

		try {
			JSONArray jsonArr = json.getJSONArray("creditcards");
			
			for (int i = 0; i < jsonArr.length(); i++) {
				this.add(new CreditCard(jsonArr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Toast.makeText(context, "Couldn't load the Credit Cards [ERR: 1]", Toast.LENGTH_SHORT).show();
			//((CreditCardsFragment)context.getF).showError();
		}
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.creditCardImages = DataFetchFactory.getCreditCardImages();
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
            holder.imgCreditCard = (ImageView)row.findViewById(R.id.imgCreditCard);
           
            row.setTag(holder);
        } else {
            holder = (CreditCardHolder)row.getTag();
        }
       
        CreditCard creditCard = this.getItem(position);
        holder.txtName.setText(creditCard.name);
        holder.txtNumber.setText(creditCard.number);
        holder.txtExpire.setText(creditCard.expire);
        
        if (creditCard.type < creditCardImages.length) {
        	holder.imgCreditCard.setImageResource(this.creditCardImages[creditCard.type]);
        } else {
        	// Set No Recognized Image...
        }

        return row;
    }
   
    static class CreditCardHolder {
        ImageView imgCreditCard;
        TextView txtName;
        TextView txtNumber;
        TextView txtExpire;
    }
}

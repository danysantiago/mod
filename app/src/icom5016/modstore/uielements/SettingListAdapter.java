package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.models.SettingRow;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingListAdapter extends ArrayAdapter<SettingRow> {
	Context context;
    int layoutResourceId;   

    public SettingListAdapter(Context context, int layoutResourceId, List<SettingRow> settingRow) {
        super(context, layoutResourceId, settingRow);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SettingRowHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new SettingRowHolder();
            holder.lblName = (TextView)row.findViewById(R.id.lblSettingName);
            holder.lblValue = (TextView)row.findViewById(R.id.lblSettingValue);
            holder.lblText = (TextView)row.findViewById(R.id.lblSettingText);
           
            row.setTag(holder);
        } else {
            holder = (SettingRowHolder)row.getTag();
        }
       
        SettingRow settingRow = this.getItem(position);
        if (settingRow.showTitleOnly()) {
        	holder.lblText.setText(settingRow.title);
        	
        	holder.lblName.setVisibility(View.GONE);
        	holder.lblValue.setVisibility(View.GONE);
        	holder.lblText.setVisibility(View.VISIBLE);
        } else {
	        holder.lblName.setText(settingRow.title);
	        holder.lblValue.setText(settingRow.value);
	        
        	holder.lblName.setVisibility(View.VISIBLE);
        	holder.lblValue.setVisibility(View.VISIBLE);
        	holder.lblText.setVisibility(View.GONE);
        }

        return row;
    }
   
    static class SettingRowHolder {
        TextView lblName;
        TextView lblValue;
        TextView lblText;
    }
}

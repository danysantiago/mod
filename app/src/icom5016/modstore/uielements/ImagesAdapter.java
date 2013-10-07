package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImagesAdapter extends ArrayAdapter<Integer> {
	Context context;
    int layoutResourceId;   

    public ImagesAdapter(Context context, int layoutResourceId, int imagesResourceID[]) {
        super(context, layoutResourceId);

		for (int i = 0; i < imagesResourceID.length; i++) {
			this.add(Integer.valueOf(imagesResourceID[i]));
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
        ImageHolder holder = null;
       
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new ImageHolder();
            holder.imgListImage = (ImageView)row.findViewById(R.id.imgListImage);
           
            row.setTag(holder);
        } else {
            holder = (ImageHolder)row.getTag();
        }
       
        holder.imgListImage.setImageResource(this.getItem(position));

        return row;
    }
   
    class ImageHolder {
        ImageView imgListImage;
    }
}
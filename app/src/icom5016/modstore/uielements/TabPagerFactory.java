package icom5016.modstore.uielements;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class TabPagerFactory implements TabContentFactory {


	    private final Context mContext;

	    public TabPagerFactory(Context context) {
	        mContext = context;
	    }

	    @Override
	    public View createTabContent(String tag) {
	        View v = new View(mContext);
	        v.setMinimumWidth(0);
	        v.setMinimumHeight(0);
	        return v;
	    }
}

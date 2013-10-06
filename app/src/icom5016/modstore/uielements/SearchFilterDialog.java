package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import icom5016.modstore.resources.DataFetchFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchFilterDialog extends DialogFragment {

				/*Dialog Views*/
	private Spinner sortSpinner;
    private Spinner categoriesSpinner;
    private Spinner ratingSpinner;
    private Spinner conditionSpinner;
    private EditText startPrice;
    private EditText endPrice;
	
    			/*Dialog Values*/
    private int sortSpinnerValue;
    private int categoriesSpinnerValue;
    private int ratingSpinnerValue;
    private int conditionSpinnerValue;
    private double startPriceValue;
    private double endPriceValue;
    
    
    
	public static SearchFilterDialog newInstance(int sortSpinnerValue, int categoriesSpinnerValue,
			int ratingSpinnerValue, int conditionSpinnerValue,
			double startPriceValue, double endPriceValue) {

    	SearchFilterDialog f = new SearchFilterDialog();
        Bundle args = new Bundle();
        //Set Args Here
        args.putInt(ConstantClass.SEARCH_DIALOG_SORT_KEY, sortSpinnerValue);
        args.putInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY, categoriesSpinnerValue);
        args.putInt(ConstantClass.SEARCH_DIALOG_RATING_KEY, ratingSpinnerValue);
        args.putInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY, conditionSpinnerValue);
        args.putDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY, startPriceValue);
        args.putDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY, endPriceValue);
        f.setArguments(args);
        return f;
    }
    
				/*Listeners */

	public SearchFilterDialogListener mListener;
	
	
	public interface SearchFilterDialogListener{
		public void onDialogOkClick(DialogFragment dialog);
		public void onDialogCancelClick(DialogFragment dialog);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SearchFilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SearchFilterDialogListener");
        }
	}
	
	
	
							/*Creating Dialog*/
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
	    View sfView	= inflater.inflate(R.layout.dialog_filter, null);
		
	    
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	   
		builder.setTitle(R.string.search_filter_title);
	    builder.setNegativeButton(R.string.search_filter_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
                mListener.onDialogCancelClick(SearchFilterDialog.this);
            }
        });
	    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            		updateValuesFromDialog();
            		mListener.onDialogOkClick(SearchFilterDialog.this);
            }
        });
	    builder.setView(sfView);
	    
	   //Fill View Dynamic Way
	    
	    	//Get Views
	    this.sortSpinner = (Spinner) sfView.findViewById(R.id.filterSortSpinner);
	    this.categoriesSpinner = (Spinner) sfView.findViewById(R.id.filterCategorySpinner);
	    this.ratingSpinner = (Spinner) sfView.findViewById(R.id.filterRatingSpinner);
	    this.conditionSpinner = (Spinner) sfView.findViewById(R.id.filterConditionSpinner);
	    this.startPrice = (EditText) sfView.findViewById(R.id.filterStartText);
	    this.endPrice = (EditText) sfView.findViewById(R.id.filterEndText);
	    
	    
	    initDialogAdapters();
	    setDialogValuesFromActivity();
	    
		return builder.create();

	}
	
	
	private void updateValuesFromDialog(){
		   this.sortSpinnerValue = this.sortSpinner.getSelectedItemPosition();
	       this.categoriesSpinnerValue = this.categoriesSpinner.getSelectedItemPosition();
	       this.ratingSpinnerValue = this.ratingSpinner.getSelectedItemPosition();
	       this.conditionSpinnerValue = this.conditionSpinner.getSelectedItemPosition();
	       if(!this.startPrice.getText().toString().trim().isEmpty())
	    	   this.startPriceValue = Double.parseDouble(this.startPrice.getText().toString().trim());
	       if(!this.endPrice.getText().toString().trim().isEmpty())
	    	   this.endPriceValue = Double.parseDouble(this.endPrice.getText().toString().trim());
	}

	private void initDialogAdapters() {
		this.sortSpinner.setAdapter(
					new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_SORT));
		this.categoriesSpinner.setAdapter(
					new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, DataFetchFactory.fetchAllCategories()));
		this.ratingSpinner.setAdapter(
				new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_RATING));
		this.conditionSpinner.setAdapter(
				new ArrayAdapter<String>(this.getActivity(), R.layout.listview_filter_spinner, ConstantClass.SEARCH_FILTER_CONDITION));
	}
	
	private void setDialogValuesFromActivity(){
	
		
	   //Obtain Values
	   Bundle args = this.getArguments();
       this.sortSpinnerValue = args.getInt(ConstantClass.SEARCH_DIALOG_SORT_KEY);
       this.categoriesSpinnerValue = args.getInt(ConstantClass.SEARCH_DIALOG_CATEGORIES_KEY);
       this.ratingSpinnerValue = args.getInt(ConstantClass.SEARCH_DIALOG_RATING_KEY);
       this.conditionSpinnerValue = args.getInt(ConstantClass.SEARCH_DIALOG_CONDITION_KEY);
       this.startPriceValue = args.getDouble(ConstantClass.SEARCH_DIALOG_START_PRICE_KEY);
       this.endPriceValue = args.getDouble(ConstantClass.SEARCH_DIALOG_END_PRICE_KEY);
       
       //Set Values
       this.sortSpinner.setSelection(this.sortSpinnerValue);
       this.categoriesSpinner.setSelection(this.categoriesSpinnerValue);
       this.ratingSpinner.setSelection(this.ratingSpinnerValue);
       this.conditionSpinner.setSelection(this.conditionSpinnerValue);
       
       //Verify Value
       if(this.startPriceValue > 0 ){
    	   this.startPrice.setText(Double.toString(startPriceValue));;
       }
       
       //Verify Value
       if(this.endPriceValue > 0 ){
    	   this.endPrice.setText(Double.toString(endPriceValue));
       }
	}

	public int getCategoriesSpinnerValue() {
		return categoriesSpinnerValue;
	}

	public int getRatingSpinnerValue() {
		return ratingSpinnerValue;
	}

	public int getConditionSpinnerValue() {
		return conditionSpinnerValue;
	}

	public double getEndPriceValue() {
		return endPriceValue;
	}

	public int getSortSpinnerValue() {
		return this.sortSpinnerValue;
	}

	public double getStartPriceValue() {
		return this.startPriceValue;
	}
}

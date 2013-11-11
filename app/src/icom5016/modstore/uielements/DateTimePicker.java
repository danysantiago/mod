package icom5016.modstore.uielements;

import icom5016.modstore.activities.R;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePicker extends DialogFragment {
	private TimePicker timePicker;
	private DatePicker datePicker;
	
	private OnClickListener onCancel;
	private OnClickListener onSet;
	
	private OnTimeChangedListener onTimeListener;
	private OnDateChangedListener onDateListener;
	
	private Calendar c;
	
	public DateTimePicker() {
		super();
		c = Calendar.getInstance();
	}
	
	public void setCalendar(Calendar c) {
		this.c = c;
	}
	
	public void setOnCancel(OnClickListener lis) {
		onCancel = lis;
	}
	
	public void setOnSet(OnClickListener lis) {
		onSet = lis;
	}
	
	
	public void setOnTimeListener(OnTimeChangedListener onTimeListener) {
		this.onTimeListener = onTimeListener;
	}

	public void setOnDateListener(OnDateChangedListener onDateListener) {
		this.onDateListener = onDateListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_datetime_picker, null);
	    
	    timePicker = (TimePicker)v.findViewById(R.id.timePicker);
	    datePicker = (DatePicker)v.findViewById(R.id.datePicker);
	    
	    timePicker.setOnTimeChangedListener(onTimeListener);

	    timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
	    timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
	    timePicker.setIs24HourView(false);
	    
	    datePicker.setCalendarViewShown(false);
	    datePicker.init(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), onDateListener);

	    builder.setView(v)
	    	.setTitle("Set Date and Time")
	    	.setPositiveButton("Set", onSet)
	    	.setNegativeButton("Cancel", onCancel);
	    
	    return builder.create();
	}
}

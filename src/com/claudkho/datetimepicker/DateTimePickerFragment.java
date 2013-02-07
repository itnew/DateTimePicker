package com.claudkho.datetimepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePickerFragment extends DialogFragment {
	private Dialog dialog;
	private DatePicker datePicker;
	private Calendar calendar_date=null;
	private TimePicker timePicker;
	private DateTimeListener mDateTimeListener = null;
	private String titleDate, titleTime;

	public void setCallBack(DateTimeListener ondatetime) {
		mDateTimeListener = ondatetime;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.date_time_picker);
		titleDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(System.currentTimeMillis());
		titleTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(System.currentTimeMillis()+900000);
		dialog.setTitle(titleDate+", "+titleTime);
		calendar_date = Calendar.getInstance();
		datePicker = (DatePicker)dialog.findViewById(R.id.datePicker1);
		datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				titleDate = dayOfMonth+" "+getMonthShortName(monthOfYear)+" "+year;
				dialog.setTitle(titleDate+", "+titleTime);
			}
		});
		timePicker= (TimePicker)dialog.findViewById(R.id.timePicker1);
		int calculate = timePicker.getCurrentMinute()+15;
		if (calculate > 59) {
			calculate = calculate - 60;
			timePicker.setCurrentMinute(calculate);
			calculate = timePicker.getCurrentHour()+1;
			if (calculate > 23)
				calculate = calculate-24;
			timePicker.setCurrentHour(calculate);
		} else {
			timePicker.setCurrentMinute(calculate);
		}
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker picker, int hour, int minute) {
				calendar_date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), hour, minute);
				titleTime = getHourIn12Format(hour)+":"+minute+" "+getAMPM(calendar_date);
				dialog.setTitle(titleDate+", "+titleTime);
			}
		});
		Button dialogButton = (Button) dialog.findViewById(R.id.button_done);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calendar_date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				if(dialog.isShowing())
					dialog.dismiss();
				if(mDateTimeListener!=null) {
					mDateTimeListener.onSet(calendar_date.get(Calendar.YEAR), getMonthShortName(calendar_date.get(Calendar.MONTH)), calendar_date.get(Calendar.DAY_OF_MONTH), getTime(), calendar_date.getTimeInMillis());
				}
			}
		});
		return dialog;
	}

	public interface DateTimeListener {
		public void onSet(int year,String month, int dayOfMonth, String time, long timestamp);
	}
	
	private String getTime() {
		String minute = ""+calendar_date.get(Calendar.MINUTE);
		if (minute.length() < 2)
			minute = "0"+minute;
		return getHourIn12Format(calendar_date.get(Calendar.HOUR_OF_DAY))+":"+minute+" "+getAMPM(calendar_date);
	}

	private String getMonthShortName(int monthNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, monthNumber);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
		simpleDateFormat.setCalendar(calendar);
		return simpleDateFormat.format(calendar.getTime());
	}

	private int getHourIn12Format(int hour24) {
		int hourIn12Format = 0;
		if(hour24==0)
			hourIn12Format = 12;
		else if(hour24<=12)
			hourIn12Format = hour24;
		else
			hourIn12Format = hour24-12;
		return hourIn12Format;
	}

	private String getAMPM(Calendar calendar) {
		return (calendar.get(Calendar.AM_PM)==(Calendar.AM))? "AM":"PM";
	}
} 
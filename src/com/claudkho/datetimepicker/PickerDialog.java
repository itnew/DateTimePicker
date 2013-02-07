package com.claudkho.datetimepicker;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.claudkho.datetimepicker.DateTimePickerFragment.DateTimeListener;

public class PickerDialog {
	public static void showDatePicker(OnDateSetListener ondate, FragmentManager sFM) {
		DatePickerFragment date = new DatePickerFragment();
		Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calender.get(Calendar.YEAR));
		args.putInt("month", calender.get(Calendar.MONTH));
		args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		date.setArguments(args);
		date.setCallBack(ondate);
		date.show(sFM, "Date Picker");
	}
	
	public static void showDateTimePicker(DateTimeListener ondatetime, FragmentManager sFM) {
		DateTimePickerFragment dateTime = new DateTimePickerFragment();
		dateTime.setCallBack(ondatetime);
		dateTime.show(sFM, "Date Time Picker");
	}
}
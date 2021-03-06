package com.yuanyu.soulmanager.ui;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Maps;
import com.yuanyu.soulmanager.R;
import com.yuanyu.soulmanager.data.CacheDb;
import com.yuanyu.soulmanager.data.FinishedTasksTable;
import com.yuanyu.soulmanager.ui.utils.FormattedTimeUtils;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsFragment extends Fragment {
	
	TextView mContentText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.statistics_fragment, container, false);
		
		mContentText = (TextView) v.findViewById(R.id.statistics_fragment_content);
		
		String content = getCurrentMonthTasksStatistics() + "\n";
		content += getLastMonthTasksStatistics();
		mContentText.setText(content);
		
		return v;
	}
	
	private String getCurrentMonthTasksStatistics() {
		long start = FormattedTimeUtils.getFirstMomentOfMonth();
		long end = FormattedTimeUtils.getLastMomentOfMonth();
		
		String[] columns = new String[] { FinishedTasksTable.Columns.TYPE,
				FinishedTasksTable.Columns.NAME, FinishedTasksTable.Columns.TIME };
		String where = FinishedTasksTable.Columns.TIME + " > " + start + " AND " +
				FinishedTasksTable.Columns.TIME + " < " + end;
		String orderBy = FinishedTasksTable.Columns.NAME;
		Cursor cursor = CacheDb.instance(getActivity()).getDbBlocking()
				.query(FinishedTasksTable.TABLE_NAME, columns, where, null, null, null, orderBy, null);
		
		Map<String, Integer> projects = Maps.newHashMap();
		Map<String, Integer> tasks = Maps.newHashMap();
		cursor.moveToFirst();
		int type;
		Integer count;
		String name;
		for(int i = 0; i < cursor.getCount(); i++) {
			type = cursor.getInt(cursor.getColumnIndex(FinishedTasksTable.Columns.TYPE));
			name = cursor.getString(cursor.getColumnIndex(FinishedTasksTable.Columns.NAME));
			switch(type) {
			case FinishedTasksTable.TYPE_PROJECT:
				count = projects.get(name);
				if(count == null) {
					projects.put(name, 1);
				}
				else {
					projects.put(name, count + 1);
				}
				break;
			case FinishedTasksTable.TYPE_TASK:
				count = tasks.get(name);
				if(count == null) {
					tasks.put(name, 1);
				}
				else {
					tasks.put(name, count + 1);
				}
				break;
			}
			cursor.moveToNext();
		}
		
		String result = "\nFinished projects this month:\n";
		if(projects.size() == 0) {
			result += "None\n";
		}
		for(Map.Entry<String, Integer> entry : projects.entrySet()) {
			result += entry.getKey() + " " + entry.getValue() + " times\n";
		}
		result += "\nFinished tasks this month: \n";
		if(tasks.size() == 0) {
			result += "None\n";
		}
		for(Map.Entry<String, Integer> entry : tasks.entrySet()) {
			result += entry.getKey() + " " + entry.getValue() + " times\n";
		}
		
		return result;
	}
	
	private String getLastMonthTasksStatistics() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		if(month == Calendar.JANUARY) {
			year--;
			month = Calendar.DECEMBER;
		}
		else {
			month--;
		}
		long start = FormattedTimeUtils.getFirstMomentOfMonth(year, month);
		long end = FormattedTimeUtils.getLastMomentOfMonth(year, month);
		
		String[] columns = new String[] { FinishedTasksTable.Columns.TYPE,
				FinishedTasksTable.Columns.NAME, FinishedTasksTable.Columns.TIME };
		String where = FinishedTasksTable.Columns.TIME + " > " + start + " AND " +
				FinishedTasksTable.Columns.TIME + " < " + end;
		String orderBy = FinishedTasksTable.Columns.NAME;
		Cursor cursor = CacheDb.instance(getActivity()).getDbBlocking()
				.query(FinishedTasksTable.TABLE_NAME, columns, where, null, null, null, orderBy, null);
		
		Map<String, Integer> projects = Maps.newHashMap();
		Map<String, Integer> tasks = Maps.newHashMap();
		cursor.moveToFirst();
		int type;
		Integer count;
		String name;
		for(int i = 0; i < cursor.getCount(); i++) {
			type = cursor.getInt(cursor.getColumnIndex(FinishedTasksTable.Columns.TYPE));
			name = cursor.getString(cursor.getColumnIndex(FinishedTasksTable.Columns.NAME));
			switch(type) {
			case FinishedTasksTable.TYPE_PROJECT:
				count = projects.get(name);
				if(count == null) {
					projects.put(name, 1);
				}
				else {
					projects.put(name, count + 1);
				}
				break;
			case FinishedTasksTable.TYPE_TASK:
				count = tasks.get(name);
				if(count == null) {
					tasks.put(name, 1);
				}
				else {
					tasks.put(name, count + 1);
				}
				break;
			}
			cursor.moveToNext();
		}
		
		String result = "\nFinished projects last month:\n";
		if(projects.size() == 0) {
			result += "None\n";
		}
		for(Map.Entry<String, Integer> entry : projects.entrySet()) {
			result += entry.getKey() + " " + entry.getValue() + " times\n";
		}
		result += "\nFinished tasks last month: \n";
		if(tasks.size() == 0) {
			result += "None\n";
		}
		for(Map.Entry<String, Integer> entry : tasks.entrySet()) {
			result += entry.getKey() + " " + entry.getValue() + " times\n";
		}
		
		return result;
	}
}

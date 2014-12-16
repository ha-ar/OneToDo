package com.vector.onetodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.androidquery.AQuery;
import com.roomorma.caldroid.CaldroidFragment;
import com.roomorma.caldroid.CaldroidListener;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;

@SuppressLint("SimpleDateFormat")
public class Calender extends Fragment {

	Button list;
	AQuery aq;
	private CaldroidFragment caldroidFragment;

	private void setCustomResourceForDates() {
	}

	/*
	 * private void setCustomResourceForDates() { Calendar cal =
	 * Calendar.getInstance();
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task_calender, container, false);
		aq = new AQuery(getActivity(), view);
		
		list = (Button) getActivity().findViewById(R.id.list_button);
		aq.id(list).visible().clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().getActionBar().show();
				getFragmentManager().popBackStack();
			}
		});
		aq.id(R.id.add_task_button).text(Constants.ADD)
				.typeface(TypeFaces.get(getActivity(), Constants.ICON_FONT))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), AddTask.class);
						intent.putExtra("position", 0);
						startActivity(intent);
						getActivity().overridePendingTransition(
								R.anim.bottom_up, R.anim.bottom_down);
					}
				});
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		/*
		 * @Override protected void onCreate(Bundle savedInstanceState) {
		 * super.onCreate(savedInstanceState);
		 * setContentView(R.layout.activity_main);
		 */

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();
		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		} else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			caldroidFragment.setArguments(args);
		}

		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.calendar_caldroid, caldroidFragment);
		t.commit();

		
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
//				Toast.makeText(getActivity(), formatter.format(date),
//						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onChangeMonth(int month, int year) {
//				String text = "month: " + month + " year: " + year;
//				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
//				Toast.makeText(getActivity(),
//						"Long click " + formatter.format(date),
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					
				}
			}

		};
		caldroidFragment.setCaldroidListener(listener);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		BaseActivity.aq_menu.id(R.id.menu_item1).text("By type");
		BaseActivity.aq_menu.id(R.id.menu_item2).text("By label");
		BaseActivity.aq_menu.id(R.id.menu_item3).visibility(View.GONE);
		MainActivity.menuchange=0;

	}
}

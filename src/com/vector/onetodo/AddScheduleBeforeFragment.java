package com.vector.onetodo;

import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class AddScheduleBeforeFragment extends Fragment {

	int position;
	AQuery aq;
	AlertDialog alert, location, label;
	static final String[] beforeArray = new String[] { "On Time", "15 Mins",
			"30 Mins", "2 Hours", "Custom" };
	static final String[] values = { "Mins", "Hours", "Days", "Weeks",
			"Months", "Years" };
	private static View previousSelected;

	public static AddScheduleBeforeFragment newInstance(int position) {
		AddScheduleBeforeFragment myFragment = new AddScheduleBeforeFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		position = getArguments().getInt("position", 0);
		View view;
		if (position == 0)
			view = inflater.inflate(R.layout.schedule_before_grid, container,
					false);
		else
			view = inflater.inflate(R.layout.schedule_location, container,
					false);
		aq = new AQuery(getActivity(), view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (position == 0) {
			final ArrayAdapter<String> beforeAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.grid_layout_textview, beforeArray);
			aq.id(R.id.before_grid_view_schedule).getGridView()
					.setAdapter(beforeAdapter);

			View dialoglayout = getActivity().getLayoutInflater().inflate(
					R.layout.custom_number_picker_dialog, null, false);

			final NumberPicker numberPicker = (NumberPicker) dialoglayout
					.findViewById(R.id.number_picker_dialog);
			numberPicker.setMinValue(0);
			numberPicker.setMaxValue(59);

			final NumberPicker customDays = (NumberPicker) dialoglayout
					.findViewById(R.id.days_picker_dialog);
			customDays.setMinValue(0);
			customDays.setMaxValue(values.length - 1);
			customDays.setDisplayedValues(values);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setView(dialoglayout);
			alert = builder.create();
			aq.id(R.id.before_grid_view_schedule).itemClicked(
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							if (previousSelected != null) {
								((TextView) previousSelected)
										.setTextColor(getResources().getColor(
												R.color.deep_sky_blue));
							}
							((TextView) view).setTextColor(Color.WHITE);
							view.setSelected(true);
							previousSelected = view;
							if (beforeArray[position].equals("Custom")) {
								alert.show();

							} else {
								TextView before = (TextView) getActivity()
										.findViewById(R.id.before_schedule);
								before.setText(beforeArray[position]);
								before.setTextColor(getResources().getColor(
										R.color.deep_sky_blue));
								ImageView reminderImage = (ImageView) getActivity()
										.findViewById(
												R.id.reminder_image_schedule);
								reminderImage.setBackground(getResources()
										.getDrawable(R.drawable.reminder_blue));

							}
						}

					});
			TextView cancelButton = (TextView) dialoglayout
					.findViewById(R.id.cencel);
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alert.cancel();
				}
			});
			TextView set = (TextView) dialoglayout.findViewById(R.id.set);
			set.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					numberPicker.clearFocus();
					customDays.clearFocus();
					TextView before = (TextView) getActivity().findViewById(
							R.id.before_schedule);
					before.setText(numberPicker.getValue() + " "
							+ values[customDays.getValue()]);
					numberPicker.getValue();
					alert.dismiss();
				}
			});
		}

	}

}

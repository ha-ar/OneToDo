package com.vector.onetodo;

import android.app.AlertDialog;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.Utils;

public class Setting extends Fragment {

	AQuery aq, aqd;
	AlertDialog alert;
	int check = -1;
	Editor editor;
	ToggleButton toggle;
	RadioButton RB, RB1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.settings, container, false);
		toggle = (ToggleButton) view.findViewById(R.id.switch_event);
		aq = new AQuery(getActivity(), view);
		editor = MainActivity.setting.edit();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		LayoutInflater inf = getActivity().getLayoutInflater();
		View dialog = inf
				.inflate(R.layout.time_week_format_dialog, null, false);
		aqd = new AQuery(dialog);
		RB = (RadioButton) dialog.findViewById(R.id.radio_1);
		RB1 = (RadioButton) dialog.findViewById(R.id.radio_2);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(dialog);
		alert = builder.create();
		if (Constants.date == true) {

			aq.id(R.id.dateformat2).text("DD.MM.YYYY");
		} else {

			aq.id(R.id.dateformat2).text("MM.DD.YYYY");
		}
		if (Constants.time == true) {

			aq.id(R.id.timeformat2).text("12 H");
		} else {

			aq.id(R.id.timeformat2).text("24 H");
		}
		if (Constants.week == true) {

			aq.id(R.id.weekstart2).text("Monday");
		} else {

			aq.id(R.id.weekstart2).text("Saturday");
		}

		aq.id(R.id.setting_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				getFragmentManager().popBackStack();
			}
		});

		aq.id(R.id.dateformat_lay).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				check = 1;
				if (Constants.date == true) {
					RB.setChecked(true);
					RB1.setChecked(false);
				} else {
					RB1.setChecked(true);
					RB.setChecked(false);
				}
				aqd.id(R.id.time_date_title).text("Date format");
				aqd.id(R.id.radio_1).text("     DD.MM.YYYY");
				aqd.id(R.id.radio_2).text("     MM.DD.YYYY");
				alert.show();
			}
		});
		aq.id(R.id.timeformat_lay).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				check = 2;
				if (Constants.time == true) {
					RB.setChecked(true);
					RB1.setChecked(false);
				} else {
					RB1.setChecked(true);
					RB.setChecked(false);
				}
				aqd.id(R.id.time_date_title).text("Time format");
				aqd.id(R.id.radio_1).text("     12 H");
				aqd.id(R.id.radio_2).text("     14 H");
				alert.show();
			}
		});
		aq.id(R.id.weekstart_lay).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				check = 3;
				if (Constants.week == true) {
					RB.setChecked(true);
					RB1.setChecked(false);
				} else {
					RB1.setChecked(true);
					RB.setChecked(false);
				}
				aqd.id(R.id.time_date_title).text("Week start");
				aqd.id(R.id.radio_1).text("     Monday");
				aqd.id(R.id.radio_2).text("     Saturday");
				alert.show();
			}
		});

		aqd.id(R.id.cancel).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				check = -1;
				alert.dismiss();
			}
		});

		aqd.id(R.id.ok).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (check == 1) {
					Constants.date = RB.isChecked();
					editor.putBoolean("date", Constants.date);
					editor.commit();
					if (Constants.date == true) {

						aq.id(R.id.dateformat2).text("DD.MM.YYYY");
					} else {

						aq.id(R.id.dateformat2).text("MM.DD.YYYY");
					}
				} else if (check == 2) {

					Constants.time = RB.isChecked();
					editor.putBoolean("time", Constants.time);
					editor.commit();
					if (Constants.time == true) {

						aq.id(R.id.timeformat2).text("12 H");
					} else {

						aq.id(R.id.timeformat2).text("24 H");
					}
				} else if (check == 3) {

					Constants.week = RB.isChecked();
					editor.putBoolean("week", Constants.week);
					editor.commit();
					if (Constants.week == true) {

						aq.id(R.id.weekstart2).text("Monday");
					} else {

						aq.id(R.id.weekstart2).text("Saturday");
					}
				}

				check = -1;
				alert.dismiss();
			}
		});

		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked == true) {
					aq.id(R.id.sound).textColorId(R.color._4d4d4d);
					aq.id(R.id.sound1).textColorId(R.color._777777);
					aq.id(R.id.vibrate).textColorId(R.color._4d4d4d);
					aq.id(R.id.dailyreview).textColorId(R.color._4d4d4d);
					aq.id(R.id.dailyreview1).textColorId(R.color._777777);

				} else {

					aq.id(R.id.sound).textColorId(R.color.hint_color);
					aq.id(R.id.sound1).textColorId(R.color.hint_color);
					aq.id(R.id.vibrate).textColorId(R.color.hint_color);
					aq.id(R.id.dailyreview).textColorId(R.color.hint_color);
					aq.id(R.id.dailyreview1).textColorId(R.color.hint_color);

				}
			}
		});

		setFont();
	}

	public void setFont() {

		Utils.RobotoRegular(getActivity(), aqd.id(R.id.cancel).getTextView());
		Utils.RobotoRegular(getActivity(), aqd.id(R.id.ok).getTextView());
		Utils.RobotoRegular(getActivity(), aqd.id(R.id.time_date_title)
				.getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.settings).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.general).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.dateformat).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.timeformat).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.weekstart).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.notification)
				.getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.appnotification)
				.getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.sound).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.vibrate).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.dailyreview)
				.getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.dateformat2).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.timeformat2).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.weekstart2).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.appnotification1)
				.getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.sound1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.dailyreview1)
				.getTextView());
	}

}
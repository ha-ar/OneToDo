package com.vector.onetodo;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TooManyListenersException;

import net.simonvt.datepicker.DatePicker;
import net.simonvt.datepicker.DatePicker.OnDateChangedListener;
import net.simonvt.timepicker.TimePicker;
import net.simonvt.timepicker.TimePicker.OnTimeChangedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;
import com.devspark.appmsg.AppMsg;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.ScaleAnimToHide;
import com.vector.onetodo.utils.ScaleAnimToShow;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

public class AddScheduleFragment extends Fragment {

	// iMageview menu_dots_task,scheduale_attachment edittext task_comment
	// linearlayout comment_box

	public static AQuery aq, aqloc, aq_label, aq_label_edit, aq_label_del;

	static List<java.lang.Object> names;
	int Label_postion = -1;
	View label_view;
	GradientDrawable label_color;
	String color;
	static String checkedId2 = null;
	EditText taskTitle;

	private Uri imageUri;

	ImageView last;
	String plabel = null;

	int pposition = -1;
	int itempos = -1;

	private AutoCompleteTextView locationTextView;

	private static final int TAKE_PICTURE = 1;

	private static View previousSelected;
	static final String[] repeatArray = new String[] { "Never", "Daily",
			"Weekly", "Monthly", "Yearly" };
	static LinearLayout lll;

	static ContactsCompletionView completionAssignView, completionShareView;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	private int[] collapsingViews = { R.id.sch_time_date_to_include,
			R.id.sch_time_date_from_include, R.id.sch_repeat_grid_layout,
			R.id.sch_label_grid
	/*
	 * R.id.repeat_linear_layout, R.id.before_grid_view_linear_schedule,
	 * R.id.repeat_schedule_text, R.id.label_grid_view_schedule,
	 * R.id.repeat_linear_schedule
	 */};

	public static String repeatdate = "", setmon1;
	private String currentDay, currentMon;
	private int[] allViews = { R.id.sch_time_to_layout,
			R.id.sch_time_from_layout, R.id.sch_title_layout,
			R.id.sch_location, R.id.sch_repeat_txt, R.id.sch_label_layout
	/*
	 * R.id.repeat_schedule_text, R.id.before_schedule, R.id.label_layout,
	 * R.id.schedule_date
	 */};

	public static HashMap<Integer, Integer> inflatingLayouts = new HashMap<Integer, Integer>();

	private final String[] labels_array = new String[] { "Personal", "Home",
			"Work", "New", "New", "New", "New", "New", "New" };

	String[] colors1 = { "#790000", "#005826", "#0D004C", "#ED145B", "#E0D400",
			"#0000FF", "#4B0049", "#005B7F", "#603913", "#005952" };

	Editor editor;
	EditText label_field = null;

	AlertDialog date_time_alert, add_new_label_alert, date_time, label_edit,
			location_del;

	protected static final int RESULT_CODE = 123;

	public static final int RESULT_GALLERY = 0;

	public static final int PICK_CONTACT = 2;

	public static View allView, viewl;

	public static Activity act;

	public static AddScheduleFragment newInstance(int position, int dayPosition) {
		AddScheduleFragment myFragment = new AddScheduleFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putInt("dayPosition", dayPosition);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.scheduale_fragment, container,
				false);
		aq = new AQuery(getActivity(), view);
		act = getActivity();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		editor = AddTask.label.edit();
		final int dayPosition = getArguments().getInt("dayPosition", 0);

		currentYear = Utils.getCurrentYear(dayPosition);
		currentMonDigit = Utils.getCurrentMonthDigit(dayPosition);
		currentDayDigit = Utils.getCurrentDayDigit(dayPosition);
		currentDay = Utils.getCurrentDay(dayPosition, Calendar.SHORT);
		currentMon = Utils.getCurrentMonth(dayPosition, Calendar.SHORT);
		currentHours = Utils.getCurrentHours();
		currentMin = Utils.getCurrentMins();

		allView = getView();

		inflatingLayouts.put(0, R.layout.add_schedule_title);
		inflatingLayouts.put(1, R.layout.add_schedule);
		inflatingLayouts.put(2, R.layout.add_schedule_date);
		inflatingLayouts.put(3, R.layout.add_schedule_location);
		inflatingLayouts.put(4, R.layout.add_schedule_before);
		inflatingLayouts.put(5, R.layout.add_schedule_repeat);
		inflatingLayouts.put(6, R.layout.scheduale_label);

		inflateLayouts();

		main();

	}

	void main() {

		// ****************Title
		aq.id(R.id.sch_title_edit)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());
		taskTitle = (EditText) aq.id(R.id.sch_title_edit).getView();

		taskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (taskTitle.getText().length() > 0)
					AddTask.btn.setAlpha(1);

				for (String words : Constants.CONTACTS_EVOKING_WORDS) {
					String[] typedWords = s.toString().split(" ");

					try {
						String name = typedWords[typedWords.length - 1];
						/*
						 * if (name.equalsIgnoreCase(words))
						 * showCurrentView(aq.id(R.id.contacts_layout_include)
						 * .getView());
						 */
					} catch (ArrayIndexOutOfBoundsException aiobe) {

					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// *********************End Title

		// ********************* Time Date
		// ******************************ALL DAY sWITCH

		aq.id(R.id.sch_time_to_layout)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.sch_time_from_layout)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		ToggleButton toggle = (ToggleButton) getActivity().findViewById(
				R.id.sch_allday_switch);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked == true) {
					aq.id(R.id.sch_allday_txt).textColorId(R.color.blue_color);
					aq.id(R.id.sch_allday_img).background(
							R.drawable.allday_blue);
					aq.id(R.id.sch_time_from).getTextView()
							.setVisibility(View.GONE);
					aq.id(R.id.sch_time_to).getTextView()
							.setVisibility(View.GONE);

					aq.id(R.id.time_picker).getView().setVisibility(View.GONE);
					aq.id(R.id.time_picker_event_end).getView()
							.setVisibility(View.GONE);
				} else {

					aq.id(R.id.time_picker).getView()
							.setVisibility(View.VISIBLE);
					aq.id(R.id.time_picker_event_end).getView()
							.setVisibility(View.VISIBLE);
					aq.id(R.id.sch_allday_txt).textColorId(R.color.hint_color);
					aq.id(R.id.sch_allday_img).background(R.drawable.allday);
					aq.id(R.id.sch_time_from).getTextView()
							.setVisibility(View.VISIBLE);

					aq.id(R.id.sch_time_to).getTextView()
							.setVisibility(View.VISIBLE);
				}
			}
		});

		// *******************dATE tIME

		// Date picker implementation
		final DatePicker dPicker = (DatePicker) aq.id(R.id.date_picker)
				.getView();
		int density = getResources().getDisplayMetrics().densityDpi;
		showRightDateAndTime();
		dPicker.init(currentYear, currentMonDigit, currentDayDigit,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						currentYear = year;
						currentMonDigit = monthOfYear;
						currentDayDigit = dayOfMonth;

						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						currentDay = cal.getDisplayName(Calendar.DAY_OF_WEEK,
								Calendar.SHORT, Locale.US);
						currentMon = cal.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US);
						showRightDateAndTime();
					}

				});

		// Time picker implementation
		TimePicker tPicker = (TimePicker) aq.id(R.id.time_picker).getView();
		tPicker.setIs24HourView(true);
		tPicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				currentHours = hourOfDay;
				currentMin = minute;
				showRightDateAndTime();
			}
		});

		// Date picker implementation
		DatePicker dPickerEvent = (DatePicker) aq
				.id(R.id.date_picker_event_end).getView();
		showRightDateAndTime();
		dPickerEvent.init(currentYear, currentMonDigit, currentDayDigit,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						currentYear = year;
						currentMonDigit = monthOfYear;
						currentDayDigit = dayOfMonth;

						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						currentDay = cal.getDisplayName(Calendar.DAY_OF_WEEK,
								Calendar.SHORT, Locale.US);
						currentMon = cal.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US);
						showRightDateAndTime();
					}

				});

		// Time picker implementation
		TimePicker tPickerEvent = (TimePicker) aq
				.id(R.id.time_picker_event_end).getView();
		tPickerEvent.setIs24HourView(true);
		tPickerEvent.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				currentHours = hourOfDay;
				currentMin = minute;
				showRightDateAndTime();
			}
		});

		if (density == DisplayMetrics.DENSITY_HIGH) {

			aq.id(R.id.date_picker).margin(-50, -20, -60, -40);
			aq.id(R.id.time_picker).margin(0, -36, -40, -40);
			aq.id(R.id.date_picker_event_end).margin(-50, -20, -60, -40);
			aq.id(R.id.time_picker_event_end).margin(0, -36, -40, -40);
			dPicker.setScaleX(0.7f);
			dPicker.setScaleY(0.7f);
			tPicker.setScaleX(0.7f);
			tPicker.setScaleY(0.7f);

			dPickerEvent.setScaleX(0.7f);
			dPickerEvent.setScaleY(0.7f);
			tPickerEvent.setScaleX(0.7f);
			tPickerEvent.setScaleY(0.7f);
		}

		// ************************** Date TIme END

		// *************************End time date

		// ***************** Location

		aq.id(R.id.sch_location)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());
		locationTextView = (AutoCompleteTextView) aq.id(R.id.sch_location)
				.getView();
		locationTextView.setAdapter(new PlacesAutoCompleteAdapter(
				getActivity(), android.R.layout.simple_spinner_dropdown_item));
		locationTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				/*
				 * locationTextView.setTextColor(getResources().getColor(
				 * R.color.deep_sky_blue));
				 * aq.id(R.id.sch_location_img2).background(
				 * R.drawable.location_blue);
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		// ******************Location END

		// *******************Repeat

		aq.id(R.id.sch_repeat_txt)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.sch_repeat_grid)
				.getGridView()
				.setAdapter(
						new ArrayAdapter<String>(getActivity(),
								R.layout.grid_layout_textview, repeatArray) {

							@Override
							public View getView(int position, View convertView,
									ViewGroup parent) {

								TextView textView = (TextView) super.getView(
										position, convertView, parent);
								if (position == 2) {
									previousSelected = textView;
									((TextView) textView)
											.setBackgroundResource(R.drawable.round_buttons_blue);
									((TextView) textView)
											.setTextColor(Color.WHITE);

								} else
									((TextView) textView)
											.setTextColor(getResources()
													.getColor(R.color._4d4d4d));
								// convertView.setSelected(true);
								return textView;
							}

						});

		aq.id(R.id.sch_repeat_grid).itemClicked(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (((TextView) previousSelected).getText().toString()
						.equals("Weekly")) {

					((TextView) previousSelected)
							.setBackgroundResource(R.drawable.round_buttons_white);
					((TextView) previousSelected).setTextColor(getResources()
							.getColor(R.color._4d4d4d));
				} else if (previousSelected != null) {
					((TextView) previousSelected).setTextColor(getResources()
							.getColor(R.color._4d4d4d));
				}
				if (((TextView) view).getText().toString().equals("Weekly")) {
					((TextView) view)
							.setBackgroundResource(R.drawable.round_buttons_blue);
				}

				if (((TextView) view).getText().toString().equals("Never")) {
					aq.id(R.id.sch_repeat_forever_radio).checked(true);
					aq.id(R.id.sch_repeat_time_radio).textColor(
							Color.parseColor("#bababa"));
					aq.id(R.id.sch_repeat_forever_radio).textColor(
							getResources().getColor(R.color._4d4d4d));
				}
				((TextView) view).setTextColor(Color.WHITE);
				view.setSelected(true);
				if (repeatArray[position] == "Never") {
					aq.id(R.id.sch_repeat_txt).text(repeatArray[position])
							.textColorId(R.color.deep_sky_blue);
				} else {
					aq.id(R.id.sch_repeat_txt)
							.text("Repeat "
									+ repeatArray[position].toLowerCase())
							.textColorId(R.color.deep_sky_blue);
				}
				aq.id(R.id.sch_repeat_img).background(R.drawable.repeat_blue);
				previousSelected = view;

			}

		});
		LayoutInflater inflater4 = getActivity().getLayoutInflater();
		View dateTimePickerDialog = inflater4.inflate(
				R.layout.date_time_layout_dialog, null, false);
		AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
		builder4.setView(dateTimePickerDialog);
		date_time_alert = builder4.create();
		/*
		 * final TextView dayField = (TextView) dateTimePickerDialog
		 * .findViewById(R.id.day_field); final TextView monthField = (TextView)
		 * dateTimePickerDialog .findViewById(R.id.month_year_field);
		 */

		// Date picker implementation for forever dialogdate_picker
		final DatePicker dialogDatePicker = (DatePicker) dateTimePickerDialog
				.findViewById(R.id.date_picker_dialog);
		showRightDateAndTimeForDialog();
		dialogDatePicker.init(currentYear, currentMonDigit, currentDayDigit,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						currentYear = year;
						currentMonDigit = monthOfYear;
						currentDayDigit = dayOfMonth;

						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						currentDay = cal.getDisplayName(Calendar.DAY_OF_WEEK,
								Calendar.SHORT, Locale.US);
						currentMon = cal.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US);
						showRightDateAndTimeForDialog();
					}

				});

		aq.id(R.id.sch_repeat_time_radio)
				.textColor(Color.parseColor("#bababa"));
		final TextView set = (TextView) dateTimePickerDialog
				.findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogDatePicker.clearFocus();
				set.requestFocus();
				date_time_alert.dismiss();

				aq.id(R.id.sch_repeat_txt)
						.text("Repeat "
								+ /* setday + " " + */((TextView) previousSelected)
										.getText().toString().toLowerCase()
								+ " until " + setmon1);
				RadioButton rb = (RadioButton) aq
						.id(R.id.sch_repeat_time_radio).getView();
				rb.setText(setmon1);
				aq.id(R.id.sch_repeat_txt).textColorId(R.color.active);
			}
		});
		TextView cancel = (TextView) dateTimePickerDialog
				.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				date_time_alert.cancel();
			}
		});
		aq.id(R.id.sch_repeat_forever_radio).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (aq.id(R.id.sch_repeat_txt).getText().toString() == "Never") {
				} else {
					aq.id(R.id.sch_repeat_txt).text(
							"Repeat "
									+ ((TextView) previousSelected).getText()
											.toString().toLowerCase());
				}

				aq.id(R.id.sch_repeat_time_radio).textColor(
						Color.parseColor("#bababa"));
				aq.id(R.id.sch_repeat_forever_radio).textColor(
						getResources().getColor(R.color._4d4d4d));
			}
		});

		aq.id(R.id.sch_repeat_time_radio).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (aq.id(R.id.sch_repeat_txt).getText().toString()
						.equals("Never")) {
					Toast.makeText(getActivity(), "Please Select ...",
							Toast.LENGTH_SHORT).show();
					aq.id(R.id.sch_repeat_time_radio).checked(false);
					aq.id(R.id.sch_repeat_forever_radio).checked(true);
					aq.id(R.id.sch_repeat_time_radio).textColor(
							Color.parseColor("#bababa"));
					aq.id(R.id.sch_repeat_forever_radio).textColor(
							getResources().getColor(R.color._4d4d4d));
				} else {

					aq.id(R.id.sch_repeat_time_radio).textColor(
							getResources().getColor(R.color._4d4d4d));
					aq.id(R.id.sch_repeat_forever_radio).textColor(

					getResources().getColor(R.color._4d4d4d));
					date_time_alert.show();
				}
			}
		});

		// *********************End Repeat

		// ******************** Label

		// ******************* label dialog

		aq.id(R.id.sch_label_layout).clicked(new GeneralOnClickListner());

		LayoutInflater inflater5 = getActivity().getLayoutInflater();

		View dialoglayout6 = inflater5.inflate(R.layout.add_task_edit, null,
				false);
		aq_label_edit = new AQuery(dialoglayout6);
		AlertDialog.Builder builder6 = new AlertDialog.Builder(getActivity());
		builder6.setView(dialoglayout6);
		label_edit = builder6.create();

		View dialoglayout7 = inflater5.inflate(R.layout.add_task_edit_delete,
				null, false);
		aq_label_del = new AQuery(dialoglayout7);
		AlertDialog.Builder builder7 = new AlertDialog.Builder(getActivity());
		builder7.setView(dialoglayout7);
		location_del = builder7.create();

		GridView gridView;

		View vie = getActivity().getLayoutInflater().inflate(
				R.layout.add_label, null, false);

		aq_label = new AQuery(vie);
		final TextView label_text = (TextView) vie
				.findViewById(R.id.add_label_text);
		gridView = (GridView) vie.findViewById(R.id.add_label_grid);

		gridView.setAdapter(new LabelImageAdapter(getActivity()));

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				ImageView img = (ImageView) view;
				if (last != null) {
					// Getting the Country TextView

					last.setImageResource(R.color.transparent);
				}
				last = img;

				img.setImageResource(R.drawable.select_white);

				Label_postion = position;
			}
		});

		AlertDialog.Builder builderLabel = new AlertDialog.Builder(
				getActivity());
		builderLabel.setView(vie);
		add_new_label_alert = builderLabel.create();

		add_new_label_alert.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub

				label_text.setText("");
			}
		});
		TextView saveLabel = (TextView) vie.findViewById(R.id.save);
		saveLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				add_new_label_alert.dismiss();
				if (!(label_text.getText().toString().equals(""))) {
					if (Label_postion != -1) {
						GradientDrawable mDrawable = (GradientDrawable) getResources()
								.getDrawable(R.drawable.label_background);
						mDrawable.setColor(Color
								.parseColor(colors1[Label_postion]));
						Save(label_view.getId() + "" + itempos, label_text
								.getText().toString(), Label_postion);
						Label_postion = -1;
						((TextView) label_view).setBackground(mDrawable);
						((TextView) label_view).setText(label_text.getText()
								.toString());
						((TextView) label_view).setTextColor(Color.WHITE);

						aq.id(R.id.sch_label_txt).text(
								((TextView) label_view).getText().toString());
						aq.id(R.id.sch_label_txt).getTextView()
								.setBackground(label_view.getBackground());
						aq.id(R.id.sch_label_txt).getTextView()
								.setTextColor(Color.WHITE);
						aq.id(R.id.sch_label_img).background(
								R.drawable.label_blue);
					}
				}
			}
		});

		TextView cancelLabel = (TextView) vie.findViewById(R.id.cancel);
		cancelLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				add_new_label_alert.dismiss();
			}
		});

		// Init labels adapter
		final String[] colors = { "#AC7900", "#4D6600", "#5A0089" };
		aq.id(R.id.sch_label_grid)
				.getGridView()
				.setAdapter(
						new ArrayAdapter<String>(getActivity(),
								R.layout.grid_layout_label_text_view,
								labels_array) {

							@Override
							public View getView(int position, View convertView,
									ViewGroup parent) {
								TextView textView = (TextView) super.getView(
										position, convertView, parent);
								Load(textView.getId() + "" + position);
								/*
								 * Log.v("View id= ", textView.getId() +
								 * position + "| " + plabel + " | " +
								 * pposition);
								 */

								if (!textView.getText().toString()
										.equals("New")) {
									textView.setTextColor(Color.WHITE);
									GradientDrawable mDrawable = (GradientDrawable) getResources()
											.getDrawable(
													R.drawable.label_background);
									mDrawable.setColor(Color
											.parseColor(colors[position]));
									textView.setBackground(mDrawable);
								}
								if (plabel != null) {
									textView.setTextColor(Color.WHITE);
									textView.setText(plabel);
									GradientDrawable mDrawable = (GradientDrawable) getResources()
											.getDrawable(
													R.drawable.label_background);
									mDrawable.setColor(Color
											.parseColor(colors1[pposition]));
									textView.setBackground(mDrawable);
								}
								return textView;
							}

						});

		aq.id(R.id.sch_label_grid).getGridView()
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						itempos = position;
						label_view = view;
						if (!((TextView) view).getText().toString()
								.equalsIgnoreCase("new")) {
							aq.id(R.id.sch_label_txt).text(
									((TextView) view).getText().toString());
							aq.id(R.id.sch_label_txt).getTextView()
									.setBackground(view.getBackground());
							aq.id(R.id.sch_label_txt).getTextView()
									.setTextColor(Color.WHITE);
							aq.id(R.id.sch_label_img).background(
									R.drawable.label_blue);
						} else {
							add_new_label_alert.show();
						}

					}

				});

		aq.id(R.id.sch_label_grid).getGridView()
				.setOnItemLongClickListener(new LabelEditClickListener());

		aq_label_del.id(R.id.edit_cencel).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				location_del.dismiss();
			}
		});

		aq_label_del.id(R.id.edit_del).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Remove(viewl.getId() + "" + itempos);
				((TextView) viewl).setText("New");
				GradientDrawable mDrawable = (GradientDrawable) getResources()
						.getDrawable(R.drawable.label_simple);
				((TextView) viewl).setBackground(mDrawable);
				((TextView) viewl).setTextColor(R.color.mountain_mist);

				location_del.dismiss();
			}
		});

		aq_label_edit.id(R.id.add_task_delete).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				label_edit.dismiss();
				location_del.show();
			}
		});

		aq_label_edit.id(R.id.add_task_edit).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub=
				// aqd.id(R.id.sch_label_txt).text(((TextView)
				// viewl).getText().)
				aq_label.id(R.id.label_title).text("Edit");
				aq_label.id(R.id.save).text("Save");
				label_view = viewl;
				label_edit.dismiss();
				add_new_label_alert.show();
			}
		});

		// ******************** END Label

		aq.id(R.id.before_schedule)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		/**
		 * View pager for before and location
		 * 
		 */
		ViewPager pager = (ViewPager) aq
				.id(R.id.add_task_before_pager_schedule).getView();

		pager.setAdapter(new AddTaskBeforePagerFragment(getActivity()
				.getSupportFragmentManager()));
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) aq.id(
				R.id.add_task_before_tabs_schedule).getView();
		tabs.setDividerColorResource(android.R.color.transparent);
		tabs.setIndicatorColorResource(R.color.deep_sky_blue);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(getActivity(), 16));
		tabs.setIndicatorHeight(2);
		tabs.setTextColorResource(R.color.deep_sky_blue);
		tabs.setAllCaps(false);
		tabs.setTypeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE),
				Typeface.NORMAL);
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);

		// Gallery and Camera intent
		aq.id(R.id.gallery)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent galleryIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(galleryIntent, RESULT_GALLERY);
					}
				});
		aq.id(R.id.camera)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");

						String path = Environment.getExternalStorageDirectory()
								.toString();
						File makeDirectory = new File(path + File.separator
								+ "OneTodo");
						makeDirectory.mkdir();
						File photo = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "OneToDo" + File.separator, "OneToDo_"
								+ System.currentTimeMillis() + ".JPG");
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(photo));
						imageUri = Uri.fromFile(photo);
						startActivityForResult(intent, TAKE_PICTURE);
					}
				});

	}

	// ***************Main End**********************

	public static void inflateLayouts() {
		GridLayout gridLayout = (GridLayout) allView
				.findViewById(R.id.inner_container_scheduale);
		gridLayout.removeAllViews();
		for (int key : inflatingLayouts.keySet()) {
			View child = act.getLayoutInflater().inflate(
					inflatingLayouts.get(key), null);
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.WRAP_CONTENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rowSpec = GridLayout.spec(key);
			child.setId(inflatingLayouts.get(key));
			child.setLayoutParams(param);
			gridLayout.addView(child);
		}
	}

	private void hideAll() {
		for (int view : collapsingViews)
			if (aq.id(view).getView() != null
					&& aq.id(view).getView().getVisibility() == View.VISIBLE)
				aq.id(view)
						.getView()
						.startAnimation(
								new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(view).getView(), true));
	}

	private void showCurrentView(View v) {

		hideAll();

		switch (v.getId()) {
		/*
		 * case R.id.schedule_date: if
		 * (aq.id(R.id.date_time_include_schedule).getView() .getVisibility() ==
		 * View.GONE) aq.id(R.id.date_time_include_schedule) .getView()
		 * .startAnimation( new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 200,
		 * aq.id(R.id.date_time_include_schedule) .getView(), true));
		 */
		// break;
		case R.id.sch_time_to_layout:
			if (aq.id(R.id.sch_time_date_to_include).getView().getVisibility() == View.GONE)
				aq.id(R.id.sch_time_date_to_include)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(
												R.id.sch_time_date_to_include)
												.getView(), true));
			break;
		case R.id.sch_time_from_layout:
			if (aq.id(R.id.sch_time_date_from_include).getView()
					.getVisibility() == View.GONE)
				aq.id(R.id.sch_time_date_from_include)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200,
										aq.id(R.id.sch_time_date_from_include)
												.getView(), true));
			break;

		case R.id.sch_repeat_txt:
			if (aq.id(R.id.sch_repeat_grid_layout).getView().getVisibility() == View.GONE) {
				if (aq.id(R.id.sch_repeat_txt).getText().toString() == "") {
					aq.id(R.id.sch_repeat_txt).text(repeatArray[2])
							.textColorId(R.color.deep_sky_blue);
					aq.id(R.id.sch_repeat_img).background(
							R.drawable.repeat_blue);

				}
				aq.id(R.id.sch_repeat_grid_layout)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.sch_repeat_grid_layout)
												.getView(), true));

			}
			break;
		case R.id.before_schedule:
			if (aq.id(R.id.before_grid_view_linear_schedule).getView()
					.getVisibility() == View.GONE)
				aq.id(R.id.before_grid_view_linear_schedule)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(
										1.0f,
										1.0f,
										1.0f,
										0.0f,
										200,
										aq.id(R.id.before_grid_view_linear_schedule)
												.getView(), true));

			break;
		case R.id.sch_label_layout:
			if (aq.id(R.id.sch_label_grid).getView().getVisibility() == View.GONE)
				aq.id(R.id.sch_label_grid)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.sch_label_grid)
												.getView(), true));
		default:
			break;
		}

	}

	private class GeneralOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.setFocusableInTouchMode(true);
			v.requestFocus();
			showCurrentView(v);
			setAllOtherFocusableFalse(v);
			if (v.getId() == R.id.sch_location
					|| v.getId() == R.id.sch_repeat_txt
					|| v.getId() == R.id.before_schedule
					|| v.getId() == R.id.sch_label_layout)
				Utils.showKeyboard(getActivity());
			else
				Utils.hidKeyboard(getActivity());
		}

	}

	private void setAllOtherFocusableFalse(View v) {
		for (int id : allViews)
			if (v.getId() != id) {
				try {
					aq.id(id).getView().setFocusableInTouchMode(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	}

	public class AddTaskBeforePagerFragment extends FragmentStatePagerAdapter {

		public AddTaskBeforePagerFragment(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2; // just Add Task & Add Event
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Time";
			case 1:
				return "Location";
			default:
				return "";// not the case

			}
		}

		@Override
		public Fragment getItem(int position) {
			return AddScheduleBeforeFragment.newInstance(position);
		}
	}

	public void slideUpDown(final View view) {
		if (!isPanelShown(view)) {
			// Show the panel
			Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
					R.anim.bottom_up);

			view.startAnimation(bottomUp);
			view.setVisibility(View.VISIBLE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					R.color.dim_background));
			((FrameLayout) aq.id(R.id.add_schedule_frame).getView())
					.setForeground(tintColor);
		} else {
			// Hide the Panel
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.bottom_down);

			view.startAnimation(bottomDown);
			view.setVisibility(View.GONE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					android.R.color.transparent));
			((FrameLayout) aq.id(R.id.add_schedule_frame).getView())
					.setForeground(tintColor);
		}
	}

	private boolean isPanelShown(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case TAKE_PICTURE:

			if (resultCode == Activity.RESULT_OK)
				showImageURI(imageUri);
		case RESULT_GALLERY:
			if (null != data) {
				showImageURI(data.getData());
			}
			break;
		case PICK_CONTACT:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getActivity().getContentResolver().query(
						contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					AppMsg.makeText(getActivity(), name + " is selected",
							AppMsg.STYLE_CONFIRM).show();
				}
			}
			break;
		default:
			break;
		}
	}

	private void showImageURI(Uri selectedImage) {
		getActivity().getContentResolver().notifyChange(selectedImage, null);
		ContentResolver cr = getActivity().getContentResolver();
		Bitmap bitmap;
		try {
			bitmap = Utils.getBitmap(selectedImage, getActivity(), cr);

			aq.id(R.id.attach_schedule).visible();
			final LinearLayout item = (LinearLayout) aq
					.id(R.id.added_image_outer).visible().getView();

			final View child = getActivity().getLayoutInflater().inflate(
					R.layout.image_added_layout, null);
			ImageView image = (ImageView) child.findViewById(R.id.image_added);
			aq.id(image).image(Utils.getRoundedCornerBitmap(bitmap, 20));
			TextView text = (TextView) child
					.findViewById(R.id.image_added_text);
			String filename = selectedImage.getPath();
			text.setText(filename);
			child.findViewById(R.id.image_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							item.removeView(child);
							// child.setVisibility(View.GONE);
						}
					});
			item.addView(child);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
					.show();
			Log.e("Camera", e.toString());
		}
	}

	private void showRightDateAndTime() {
		String tempCurrentDayDigit = String.format("%02d", currentDayDigit);
		String tempCurrentHours = String.format("%02d", currentHours);
		String tempCurrentMins = String.format("%02d", currentMin);

		if (aq.id(R.id.sch_time_date_to_include).getView().getVisibility() == 0) {
			aq.id(R.id.sch_time_to_day).text(currentDay)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_to_day_month)
					.text(tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit))
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_to_month).text(currentMon)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_to_img)
					.getImageView()
					.setBackground(
							getResources()
									.getDrawable(R.drawable.calendar_blue));
			aq.id(R.id.sch_time_to_txt).textColor(
					getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_to)
					.text(tempCurrentHours + " : " + tempCurrentMins)
					.textColor(getResources().getColor(R.color.deep_sky_blue));

		}

		if (aq.id(R.id.sch_time_date_from_include).getView().getVisibility() == 0) {
			aq.id(R.id.sch_time_from_day).text(currentDay)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_from_day_month)
					.text(tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit))
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_from_month).text(currentMon)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_from_img)
					.getImageView()
					.setBackground(
							getResources()
									.getDrawable(R.drawable.calendar_blue));
			aq.id(R.id.sch_time_from_txt).textColor(
					getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.sch_time_from)
					.text(tempCurrentHours + " : " + tempCurrentMins)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
		}
	}

	private void showRightDateAndTimeForDialog() {

		String fff = String.valueOf(currentDayDigit).replace("th", "");
		setmon1 = fff + " " + currentMon + " " + currentYear;
		repeatdate = currentYear + "-" + (currentMonDigit + 1) + "-"
				+ currentDayDigit + " 00:00:00";
	}

	public void Save(String id, String name, int label_position) {
		// 0 - for private mode
		editor.putString(3 + "key_label" + id, name); // Storing integer
		editor.putInt(3 + "key_color_position" + id, label_position); // Storing
																		// float
		editor.commit();
	}

	public void Load(String id) {
		plabel = null;
		plabel = AddTask.label.getString(3 + "key_label" + id, null); // getting
																		// String
		Log.v("View id= ", id + "| " + plabel + " | " + pposition);

		pposition = AddTask.label.getInt(3 + "key_color_position" + id, 0); // getting
																			// String
	}

	public void Remove(String id) {
		editor.remove(3 + "key_label" + id); // will delete key name
		editor.remove(3 + "key_color_position" + id); // will delete key email
		editor.commit();
	}

	private class LabelEditClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			if (((TextView) arg1).getText().toString().equals("New")
					|| position < 3) {

			} else {
				aq_label.id(R.id.add_label_text).text(
						((TextView) arg1).getText().toString());
				aq_label_del.id(R.id.body).text(
						"Label " + ((TextView) arg1).getText().toString()
								+ " will be deleted");
				aq_label_edit.id(R.id.add_task_edit_title).text(
						"Label: " + ((TextView) arg1).getText().toString());
				viewl = arg1;
				itempos = position;
				label_edit.show();
			}
			return false;
		}
	}

	public class LabelImageAdapter extends BaseAdapter {
		private Context mContext;

		public LabelImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return 10;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(Utils
						.convertDpToPixel(40, mContext), Utils
						.convertDpToPixel(40, mContext)));
			} else {
				imageView = (ImageView) convertView;
			}

			GradientDrawable mDrawable = (GradientDrawable) getResources()
					.getDrawable(R.drawable.label_background_dialog);
			mDrawable.setColor(Color.parseColor(colors1[position]));
			imageView.setBackground(mDrawable);

			return imageView;
		}

	}

}
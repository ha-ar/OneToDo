package com.vector.onetodo;

import it.feio.android.checklistview.ChecklistManager;
import it.feio.android.checklistview.exceptions.ViewNotSupportedException;
import it.feio.android.checklistview.interfaces.CheckListChangedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.simonvt.datepicker.DatePicker;
import net.simonvt.datepicker.DatePicker.OnDateChangedListener;
import net.simonvt.timepicker.TimePicker;
import net.simonvt.timepicker.TimePicker.OnTimeChangedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.astuetz.PagerSlidingTabStrip;
import com.devspark.appmsg.AppMsg;
import com.vector.onetodo.db.gen.LabelName;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.ScaleAnimToHide;
import com.vector.onetodo.utils.ScaleAnimToShow;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

public class AddEventFragment extends Fragment {

	
	// iMageview menu_dots_task,event_attachment edittext task_comment  linearlayout comment_box
	public AQuery aq, AQlabel, AQlabel_edit, AQlabel_del, AQ_attach;

	// HttpClient client;
	HttpPost post;
	List<NameValuePair> pairs;
	HttpResponse response = null;
	static int FragmentCheck = 0;
	Uri filename;
	EditText taskTitle;

	static String checkedId2 = null;
	View label_view, viewl;;
	GradientDrawable label_color;
	int Label_postion = -1;
	int dayPosition;
	private int lastCheckedId = -1;

	final String[] colors1 = { "#790000", "#005826", "#0D004C", "#ED145B",
			"#E0D400", "#0000FF", "#4B0049", "#005B7F", "#603913", "#005952" };
	ImageView last;
	String plabel = null;
	int pposition = -1;
	int itempos = -1;
	int MaxId = -1;
	Editor editor, editorattach;

	static ContactsCompletionView completionAssignView, completionShareView;

	AlertDialog add_new_label_alert, date_time_alert,
			label_edit, location_del, attach_alert;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	private String currentDay, currentMon;

	private int[] collapsingViews = { R.id.date_time_include_to,
			R.id.date_time_include_from,
			R.id.before_grid_view_linear_event, R.id.repeat_linear_layout,
			R.id.label_event_grid_view };

	private int[] allViews = { R.id.event_title, R.id.time_date_to,
			R.id.time_date_from, R.id.before_event, R.id.spinner_labels_event,
			R.id.spinner_label_layout };

	public static HashMap<Integer, Integer> inflatingLayoutsEvents = new HashMap<Integer, Integer>();

	public static View parentView;

	static final String[] repeatArray = new String[] { "Once", "Daily",
			"Weekly", "Monthly", "Yearly" };

	private final String[] labels_array = new String[] { "Personal", "Home",
			"Work", "New", "New", "New", "New", "New", "New" };

	protected static final int RESULT_CODE = 123;

	private static final int TAKE_PICTURE = 1;

	public static final int RESULT_GALLERY = 0;

	public static final int PICK_CONTACT = 2;

	private Uri imageUri;

	public static View allView;

	public static Activity act;

	public static AddEventFragment newInstance(int position, int dayPosition) {
		AddEventFragment myFragment = new AddEventFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putInt("dayPosition", dayPosition);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_event_fragment, container,
				false);
		aq = new AQuery(getActivity(), view);
		act = getActivity();
		editor = AddTask.label.edit();
		editorattach = AddTask.attach.edit();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dayPosition = getArguments().getInt("dayPosition", 0);
		allView = getView();

		currentYear = Utils.getCurrentYear(dayPosition);
		currentMonDigit = Utils.getCurrentMonthDigit(dayPosition);
		currentDayDigit = Utils.getCurrentDayDigit(dayPosition);
		currentDay = Utils.getCurrentDay(dayPosition, Calendar.SHORT);
		currentMon = Utils.getCurrentMonth(dayPosition, Calendar.SHORT);
		currentHours = Utils.getCurrentHours();
		currentMin = Utils.getCurrentMins();

		inflatingLayoutsEvents.put(0, R.layout.add_event_title);
		inflatingLayoutsEvents.put(1, R.layout.add_event_time_date);
		inflatingLayoutsEvents.put(2, R.layout.add_event_before);
		inflatingLayoutsEvents.put(3, R.layout.add_event_label);
		inflatingLayoutsEvents.put(4, R.layout.add_event_notes);
		inflatingLayoutsEvents.put(5, R.layout.add_event_image);

		inflateLayouts();

		// *******************TypeFaces

		aq.id(R.id.cancel)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						getActivity().finish();
					}
				});
		aq.id(R.id.contacts_name).typeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE));

		aq.id(R.id.time_date_to)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.time_date_from)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.event_title)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.location_event)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());
		aq.id(R.id.before_event)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());
		aq.id(R.id.repeat_event)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.grid_text)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		// **************************************TypeFaces

		// *****************Title
		taskTitle = (EditText) aq.id(R.id.event_title).getView();

		TextView taskassign = (TextView) aq.id(R.id.event_assign).getView();

		taskassign.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				aq.id(R.id.event_assign).textColorId(R.color.active);
				aq.id(R.id.assign_event_button).background(
						R.drawable.assign_blue);
				aq.id(R.id.imageView1).background(R.drawable.next_arrow_black);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

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
						/*if (name.equalsIgnoreCase(words))
							showCurrentView(aq.id(R.id.contacts_layout_include)
									.getView());*/
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

		// *********************** Title End

		// ******************************ALL DAY sWITCH

		ToggleButton toggle = (ToggleButton) getActivity().findViewById(
				R.id.switch_event);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked == true) {

					aq.id(R.id.all_day_all).textColorId(R.color.blue_color);
					aq.id(R.id.all_day_image)
							.background(R.drawable.allday_blue);
					aq.id(R.id.time_from).getTextView()
							.setVisibility(View.GONE);
					aq.id(R.id.time_to).getTextView().setVisibility(View.GONE);

					aq.id(R.id.time_picker).getView().setVisibility(View.GONE);
					aq.id(R.id.time_picker_event_end).getView()
							.setVisibility(View.GONE);

				} else {

					aq.id(R.id.time_picker).getView()
							.setVisibility(View.VISIBLE);
					aq.id(R.id.time_picker_event_end).getView()
							.setVisibility(View.VISIBLE);
					aq.id(R.id.all_day_all).textColorId(R.color.hint_color);
					aq.id(R.id.all_day_image).background(R.drawable.allday);
					aq.id(R.id.time_from).getTextView()
							.setVisibility(View.VISIBLE);

					aq.id(R.id.time_to).getTextView()
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

		// ****************************** Label Dialog
		GridView gridView;

		final String[] labels_array1 = new String[] { "A", "A", "A", "A", "A",
				"A", "A", "A", "A", "A", };

		View vie = getActivity().getLayoutInflater().inflate(
				R.layout.add_label_event, null, false);

		AQlabel = new AQuery(vie);
		final TextView label_text = (TextView) vie
				.findViewById(R.id.add_label_text_event);
		gridView = (GridView) vie.findViewById(R.id.add_label_grid_event);

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

				// buttonColor = (ColorDrawable) view.getBackground();
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

						aq.id(R.id.spinner_labels_event).text(
								((TextView) label_view).getText().toString());
						aq.id(R.id.spinner_labels_event).getTextView()
								.setBackground(label_view.getBackground());
						aq.id(R.id.spinner_labels_event).getTextView()
								.setTextColor(Color.WHITE);
						aq.id(R.id.label_image).background(
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
		aq.id(R.id.label_event_grid_view)
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
								Log.v("View id= ", textView.getId() + position
										+ "| " + plabel + " | " + pposition);

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
		aq.id(R.id.label_event_grid_view).getGridView()
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						itempos = position;
						label_view = view;
						if (!((TextView) view).getText().toString()
								.equalsIgnoreCase("new")) {
							aq.id(R.id.spinner_labels_event).text(
									((TextView) view).getText().toString());
							aq.id(R.id.spinner_labels_event).getTextView()
									.setBackground(view.getBackground());
							aq.id(R.id.spinner_labels_event).getTextView()
									.setTextColor(Color.WHITE);
							aq.id(R.id.label_image).background(
									R.drawable.label_blue);
						} else {
							add_new_label_alert.show();
						}

					}

				});

		aq.id(R.id.label_event_grid_view).getGridView()
				.setOnItemLongClickListener(new LabelEditClickListener());

		LayoutInflater inflater5 = getActivity().getLayoutInflater();

		View dialoglayout6 = inflater5.inflate(R.layout.add_task_edit, null,
				false);
		AQlabel_edit = new AQuery(dialoglayout6);
		AlertDialog.Builder builder6 = new AlertDialog.Builder(getActivity());
		builder6.setView(dialoglayout6);
		label_edit = builder6.create();

		View dialoglayout7 = inflater5.inflate(R.layout.add_task_edit_delete,
				null, false);
		AQlabel_del = new AQuery(dialoglayout7);
		AlertDialog.Builder builder7 = new AlertDialog.Builder(getActivity());
		builder7.setView(dialoglayout7);
		location_del = builder7.create();

		AQlabel_del.id(R.id.edit_cencel).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				location_del.dismiss();
			}
		});

		AQlabel_del.id(R.id.edit_del).clicked(new OnClickListener() {

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

		AQlabel_edit.id(R.id.add_task_delete).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				label_edit.dismiss();
				location_del.show();
			}
		});

		AQlabel_edit.id(R.id.add_task_edit).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub=
				// aqd.id(R.id.add_label_text).text(((TextView)
				// viewl).getText().)
				AQlabel.id(R.id.label_title_event).text("Edit");
				AQlabel.id(R.id.save).text("Save");
				label_view = viewl;
				label_edit.dismiss();
				add_new_label_alert.show();
			}
		});
		aq.id(R.id.spinner_label_layout).clicked(new GeneralOnClickListner());

		// ********************************* Label END

		// Show image choose options
		aq.id(R.id.image_event)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		// Gallery and Camera intent

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View attachment = inflater
				.inflate(R.layout.add_attachment, null, false);
		AQ_attach = new AQuery(attachment);

		// Gallery and Camera intent
		AQ_attach
				.id(R.id.gallery1)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						attach_alert.dismiss();
						Intent galleryIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(galleryIntent, RESULT_GALLERY);
					}
				});
		AQ_attach
				.id(R.id.camera1)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						attach_alert.dismiss();
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
		AlertDialog.Builder attach_builder = new AlertDialog.Builder(
				getActivity());
		attach_builder.setView(attachment);
		attach_alert = attach_builder.create();

		/*aq.id(R.id.event_attachment).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// slideUpDown(aq.id(R.id.attachement_layout_include).getView());
				attach_alert.show();
			}
		});*/

		// *********************Priority*******
		/*
		 * lastCheckedId = ((RadioGroup) aq.id(R.id.priority_radio_buttons)
		 * .getView()).getCheckedRadioButtonId(); ((RadioGroup)
		 * aq.id(R.id.priority_radio_buttons).getView())
		 * .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(RadioGroup group, int
		 * checkedId) { ((RadioButton) group.findViewById(lastCheckedId))
		 * .setTextColor(getResources().getColor( R.color.deep_sky_blue));
		 * ((RadioButton) group.findViewById(checkedId))
		 * .setTextColor(getResources().getColor( android.R.color.white));
		 * String abc = ((RadioButton) group
		 * .findViewById(checkedId)).getText().toString(); if
		 * (abc.equals("None")) AddTask.priority = 0; else if (abc.equals("!"))
		 * AddTask.priority = 1; else if (abc.equals("! !")) AddTask.priority =
		 * 2; else if (abc.equals("! ! !")) AddTask.priority = 3; lastCheckedId
		 * = checkedId; } });
		 */

		/**
		 * View pager for before and location
		 * 
		 */
		ViewPager pager = (ViewPager) aq.id(R.id.add_event_before_pager)
				.getView();

		pager.setAdapter(new AddEventBeforePagerFragment(getActivity()
				.getSupportFragmentManager()));

		// Bind the tabs to the ViewPager
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) aq.id(
				R.id.add_task_before_tabs).getView();
		tabs.setDividerColorResource(android.R.color.transparent);
		tabs.setIndicatorColorResource(R.color._4d4d4d);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(getActivity(), 16));
		tabs.setIndicatorHeight(Utils.getPxFromDp(getActivity(), 1));
		tabs.setAllCaps(false);
		tabs.setTypeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE),
				Typeface.NORMAL);
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);

		LayoutInflater inflater4 = getActivity().getLayoutInflater();
		View dateTimePickerDialog = inflater4.inflate(
				R.layout.date_time_layout_dialog, null, false);
		AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
		builder4.setView(dateTimePickerDialog);
		date_time_alert = builder4.create();
		final TextView dayField = (TextView) dateTimePickerDialog
				.findViewById(R.id.day_field);
		final TextView monthField = (TextView) dateTimePickerDialog
				.findViewById(R.id.month_year_field);

		// Date picker implementation for forever dialog
		final DatePicker dialogDatePicker = (DatePicker) dateTimePickerDialog
				.findViewById(R.id.date_picker_dialog);
		showRightDateAndTimeForDialog(dayField, monthField);
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
						showRightDateAndTimeForDialog(dayField, monthField);
					}

				});
		final TextView set = (TextView) dateTimePickerDialog
				.findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				set.requestFocus();
				dialogDatePicker.clearFocus();
				date_time_alert.dismiss();
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

		// ************ Location

		/*
		 * locationTextView = (AutoCompleteTextView) aq.id(R.id.location_event)
		 * .getView(); locationTextView.setAdapter(new
		 * PlacesAutoCompleteAdapter( getActivity(),
		 * android.R.layout.simple_spinner_dropdown_item));
		 * locationTextView.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable s) {
		 * locationTextView.setTextColor(getResources().getColor(
		 * R.color.deep_sky_blue)); aq.id(R.id.location_event_img).background(
		 * R.drawable.location_blue);
		 * 
		 * } });
		 */

		// *************************************** Check List

		lastCheckedId = ((RadioGroup) aq.id(R.id.priority_radio_buttons_event)
				.getView()).getCheckedRadioButtonId();
		((RadioGroup) aq.id(R.id.priority_radio_buttons_event).getView())
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						((RadioButton) group.findViewById(lastCheckedId))
								.setTextColor(getResources().getColor(
										R.color.deep_sky_blue));
						((RadioButton) group.findViewById(checkedId))
								.setTextColor(getResources().getColor(
										android.R.color.white));
						lastCheckedId = checkedId;
					}
				});

		View switchView = aq.id(R.id.add_sub_task_event).getView();
		toggleCheckList(switchView);

	}

	private void showRightDateAndTime() {
		String tempCurrentDayDigit = String.format("%02d", currentDayDigit);
		String tempCurrentHours = String.format("%02d", currentHours);
		String tempCurrentMins = String.format("%02d", currentMin);

		if (aq.id(R.id.date_time_include_to).getView().getVisibility() == 0) {
			aq.id(R.id.day_field_to).text(currentDay)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.date_field_to)
					.text(tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit))
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.month_year_field_to).text(currentMon)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.date_time_to)
					.getImageView()
					.setBackground(
							getResources()
									.getDrawable(R.drawable.calendar_blue));
			aq.id(R.id.to).textColor(
					getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.time_to)
					.text(tempCurrentHours + " : " + tempCurrentMins)
					.textColor(getResources().getColor(R.color.deep_sky_blue));

		}

		if (aq.id(R.id.date_time_include_from).getView().getVisibility() == 0) {
			aq.id(R.id.day_field_from).text(currentDay)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.date_field_from)
					.text(tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit))
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.month_year_field_from).text(currentMon)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.date_time_from)
					.getImageView()
					.setBackground(
							getResources()
									.getDrawable(R.drawable.calendar_blue));
			aq.id(R.id.from).textColor(
					getResources().getColor(R.color.deep_sky_blue));
			aq.id(R.id.time_from)
					.text(tempCurrentHours + " : " + tempCurrentMins)
					.textColor(getResources().getColor(R.color.deep_sky_blue));
		}
	}

	private void showRightDateAndTimeForDialog(View day, View month) {
		String tempCurrentDayDigit = String.format("%02d", currentDayDigit);
		String tempYear = String.valueOf(currentYear).substring(2, 4);
		aq.id(day).text(currentDay);
		aq.id(month).text(
				tempCurrentDayDigit
						+ Utils.getDayOfMonthSuffix(currentDayDigit) + " "
						+ currentMon + "," + tempYear);
	}

	public static void inflateLayouts() {
		GridLayout gridLayout = (GridLayout) allView
				.findViewById(R.id.inner_event_container);
		gridLayout.removeAllViews();
		for (int key : inflatingLayoutsEvents.keySet()) {
			View child = act.getLayoutInflater().inflate(
					inflatingLayoutsEvents.get(key), null);
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.WRAP_CONTENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rowSpec = GridLayout.spec(key);
			child.setId(inflatingLayoutsEvents.get(key));
			child.setLayoutParams(param);
			gridLayout.addView(child);
		}
	}

	public static void swapInflatedLayouts(int from, int to) {
		if (from < to) {
			for (int i = from; i < to; i++) {
				Integer temp = inflatingLayoutsEvents.get(i);
				inflatingLayoutsEvents
						.put(i, inflatingLayoutsEvents.get(i + 1));
				inflatingLayoutsEvents.put(i + 1, temp);
			}
		} else {
			for (int i = from; i > to; i--) {
				Integer temp = inflatingLayoutsEvents.get(i);
				inflatingLayoutsEvents
						.put(i, inflatingLayoutsEvents.get(i - 1));
				inflatingLayoutsEvents.put(i - 1, temp);
			}

		}

	}

	/**
	 * Checlists added counter
	 */
	static int checklistCount = 0;

	static List<View> checklistViews = new ArrayList<View>();
	EditText editTextCurrent;

	private class GeneralOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.setFocusableInTouchMode(true);
			v.requestFocus();
			showCurrentView(v);
			setAllOtherFocusableFalse(v);
			if (v.getId() == R.id.event_title || v.getId() == R.id.notes)
				Utils.showKeyboard(getActivity());
			else
				Utils.hidKeyboard(getActivity());
		}

	}

	private void setAllOtherFocusableFalse(View v) {
		for (int id : allViews)
			if (v.getId() != id) {
				aq.id(id).getView().setFocusableInTouchMode(false);
			}

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
		Cursor returnCursor = cr.query(selectedImage, null, null, null, null);
		// returnCursor.moveToFirst();

		MimeTypeMap mime = MimeTypeMap.getSingleton();

		String type = MimeTypeMap.getFileExtensionFromUrl(selectedImage
				.toString());

		Bitmap bitmap;
		if (FragmentCheck == 0) {
			try {
				bitmap = Utils.getBitmap(selectedImage, getActivity(), cr);
				aq.id(R.id.attach_event).visible();
				final LinearLayout item = (LinearLayout) aq
						.id(R.id.added_image_outer_event).visible().getView();

				final View child = getActivity().getLayoutInflater().inflate(
						R.layout.image_added_layout, null);
				ImageView image = (ImageView) child
						.findViewById(R.id.image_added);
				aq.id(image).image(Utils.getRoundedCornerBitmap(bitmap, 20));
				TextView text = (TextView) child
						.findViewById(R.id.image_added_text);
				TextView by = (TextView) child
						.findViewById(R.id.image_added_by);/*
				TextView size = (TextView) child
						.findViewById(R.id.image_added_size);
				size.setText("" + new File(selectedImage.getPath()).length());*/
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
				by.setText("By Usman Ameer on " + sdf.format(cal.getTime()));
				filename = selectedImage;
				// AddTask.path.add(filename);
				File myFile = new File(selectedImage.toString());

				myFile.getAbsolutePath();
				/*
				 * Toast.makeText(getActivity(), filename + "  ---   ",
				 * Toast.LENGTH_LONG).show();
				 */
				/*
				 * ImageUploadTask asyn=new ImageUploadTask(); asyn.execute();
				 */
				imageupload();
				// text.setText(returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
				text.setText(selectedImage.getLastPathSegment() + "." + type);
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
				Toast.makeText(getActivity(), "Failed to load",
						Toast.LENGTH_SHORT).show();
				Log.e("Camera", e.toString());
			}
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
		case R.id.time_date_to:
			if (aq.id(R.id.date_time_include_to).getView().getVisibility() == View.GONE)
				aq.id(R.id.date_time_include_to)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.date_time_include_to)
												.getView(), true));
			break;
		case R.id.time_date_from:
			if (aq.id(R.id.date_time_include_from).getView().getVisibility() == View.GONE)
				aq.id(R.id.date_time_include_from)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.date_time_include_from)
												.getView(), true));
			break;
		/*case R.id.contacts_layout_include:
			if (aq.id(R.id.contacts_layout_include).getView().getVisibility() == View.GONE) {
				aq.id(R.id.contacts_layout_include)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200,
										aq.id(R.id.contacts_layout_include)
												.getView(), true));
			}
			break;*/
		/*
		 * case R.id.image: if
		 * (aq.id(R.id.gallery_include).getView().getVisibility() == View.GONE)
		 * aq.id(R.id.gallery_include) .getView() .startAnimation( new
		 * ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 200,
		 * aq.id(R.id.gallery_include) .getView(), true)); break;
		 */
		case R.id.before_event:
			if (aq.id(R.id.before_grid_view_linear_event).getView()
					.getVisibility() == View.GONE) {
				if (aq.id(R.id.before_event).getText().toString() == "") {
					aq.id(R.id.before_event)
							.text("Reminde before "
									+ AddEventBeforeFragment.beforeArray[1])
							.textColorId(R.color.deep_sky_blue);
					aq.id(R.id.before_event_image).background(
							R.drawable.reminder_blue);

				}
				aq.id(R.id.before_grid_view_linear_event)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(
										1.0f,
										1.0f,
										1.0f,
										0.0f,
										200,
										aq.id(R.id.before_grid_view_linear_event)
												.getView(), true));
			}
			break;
		case R.id.repeat_event:
			if (aq.id(R.id.repeat_linear_layout).getView().getVisibility() == View.GONE) {
				if (aq.id(R.id.repeat_event).getText().toString() == "") {
					aq.id(R.id.repeat_event).text(repeatArray[0])
							.textColorId(R.color.deep_sky_blue);
					aq.id(R.id.repeat_image).background(R.drawable.repeat_blue);

				}
				aq.id(R.id.repeat_linear_layout)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.repeat_linear_layout)
												.getView(), true));
			}
			break;
		case R.id.spinner_label_layout:
			if (aq.id(R.id.label_event_grid_view).getView().getVisibility() == View.GONE)
				aq.id(R.id.label_event_grid_view)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.label_event_grid_view)
												.getView(), true));
		default:
			break;
		}

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private final String[] mobileValues;

		public ImageAdapter(Context context, String[] mobileValues) {
			this.context = context;
			this.mobileValues = mobileValues;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View gridView;

			if (convertView == null) {
				gridView = new View(context);
				gridView = inflater
						.inflate(R.layout.add_label_text_event, null);

				TextView textView = (TextView) gridView
						.findViewById(R.id.grid_item_label_event);
				textView.setText(mobileValues[position]);
			} else {
				gridView = (View) convertView;
			}
			return gridView;
		}

		@Override
		public int getCount() {
			return mobileValues.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	public class AddEventBeforePagerFragment extends FragmentStatePagerAdapter {

		public AddEventBeforePagerFragment(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "By time";
			case 1:
				return "At location";
			default:
				return "";// not the case

			}
		}

		@Override
		public Fragment getItem(int position) {
			return AddEventBeforeFragment.newInstance(position);
		}
	}

	public void label_add(String name) {
		// AddTask.labelnamedao.notifyAll();
		List<LabelName> labelname_list1 = AddTask.labelnamedao.loadAll();
		Toast.makeText(getActivity(), "" + labelname_list1.size(),
				Toast.LENGTH_SHORT).show();
		if (labelname_list1.size() < 6) {
			int g = 0;
			for (int i = 0; i < labelname_list1.size(); i++)
				if (labelname_list1.get(i).getName().toString()
						.equalsIgnoreCase(name)) {
					g = 1;
				}
			if (g == 0) {
				LabelName labelname = new LabelName(AddTask.f2, name);
				AddTask.labelnamedao.insert(labelname);

			}
		}
	}

	// private ChecklistManager mChecklistManager;

	private void toggleCheckList(View switchView) {
		View newView;

		/*
		 * Here is where the job is done. By simply calling an instance of the
		 * ChecklistManager we can call its methods.
		 */
		try {
			// Getting instance
			ChecklistManager mChecklistManager = ChecklistManager
					.getInstance(getActivity());

			/*
			 * These method are useful when converting from EditText to
			 * ChecklistView (but can be set anytime, they'll be used at
			 * appropriate moment)
			 */

			// Setting new entries hint text (if not set no hint
			// will be used)
			mChecklistManager.setNewEntryHint("Add a subtask...");
			// Let checked items are moved on bottom

			mChecklistManager.setMoveCheckedOnBottom(1);

			mChecklistManager
					.setCheckListChangedListener(new CheckListChangedListener() {

						@Override
						public void onCheckListChanged() {

						}
					});

			// Decide if keep or remove checked items when converting
			// back to simple text from checklist
			mChecklistManager.setKeepChecked(true);

			// I want to make checks symbols visible when converting
			// back to simple text from checklist
			mChecklistManager.setShowChecks(true);

			// Converting actual EditText into a View that can
			// replace the source or viceversa
			newView = mChecklistManager.convert(switchView);

			// Replacing view in the layout
			mChecklistManager.replaceViews(switchView, newView);

			// Updating the instance of the pointed view for
			// eventual reverse conversion
			switchView = newView;

		} catch (ViewNotSupportedException e) {
			// This exception is fired if the source view class is
			// not supported
			e.printStackTrace();
		}
	}

	private class LabelEditClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			if (((TextView) arg1).getText().toString().equals("New")
					|| position < 3) {

			} else {
				AQlabel.id(R.id.add_label_text_event).text(
						((TextView) arg1).getText().toString());
				AQlabel_del.id(R.id.body).text(
						"Label " + ((TextView) arg1).getText().toString()
								+ " will be deleted");
				AQlabel_edit.id(R.id.add_task_edit_title).text(
						"Label: " + ((TextView) arg1).getText().toString());
				viewl = arg1;
				itempos = position;
				label_edit.show();
			}
			return false;
		}
	}

	public void Save(String id, String name, int label_position) {
		// 0 - for private mode
		editor.putString(2 + "key_label" + id, name); // Storing integer
		editor.putInt(2 + "key_color_position" + id, label_position); // Storing
																		// float
		editor.commit();
	}

	public void Load(String id) {
		plabel = null;
		plabel = AddTask.label.getString(2 + "key_label" + id, null); // getting
																		// String
		Log.v("View id= ", id + "| " + plabel + " | " + pposition);

		pposition = AddTask.label.getInt(2 + "key_color_position" + id, 0); // getting
																			// String
	}

	public void Remove(String id) {
		editor.remove(2 + "key_label" + id); // will delete key name
		editor.remove(2 + "key_color_position" + id); // will delete key email
		editor.commit();
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
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(Utils
						.convertDpToPixel(40, mContext), Utils
						.convertDpToPixel(40, mContext)));
				// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				// imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			GradientDrawable mDrawable = (GradientDrawable) getResources()
					.getDrawable(R.drawable.label_background_dialog);
			mDrawable.setColor(Color.parseColor(colors1[position]));
			imageView.setBackground(mDrawable);

			// imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

	}

	public void imageupload() {

		HttpEntity entity = null;

		Bitmap bm = null;
		try {
			bm = MediaStore.Images.Media.getBitmap(getActivity()
					.getContentResolver(), filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArray = baos.toByteArray();
		String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
		pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("image", encoded));

		// UrlEncodedFormEntity
		// entity=null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			// entity.setContentType("application/json");
		} catch (UnsupportedEncodingException e) {
			// TODO
			// Auto-generated
			// catch block
			e.printStackTrace();
		}

		Map<String, HttpEntity> param = new HashMap<String, HttpEntity>();
		param.put(AQuery.POST_ENTITY, entity);

		aq.ajax("http://api.heuristix.net/one_todo/v1/upload.php", param,
				JSONObject.class, new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						// dismis();
						String path = null;
						try {

							JSONObject obj1 = new JSONObject(json.toString());
							path = obj1.getString("path");
							// id = obj1.getInt("result");

						} catch (Exception e) {
						}

						Loadattachmax();
						if (MaxId == 0) {
							MaxId = 1;
						} else {
							MaxId = MaxId + 1;
						}
						Saveattach(MaxId, path, "type");
						Log.v("Response", json.toString());

					}
				});
	}

	public void Saveattach(int id, String path, String type) {
		// 0 - for private mode
		editorattach.putInt("2Max", id);
		editorattach.putString(2 + "path" + id, path);
		editorattach.putString(2 + "type" + id, type); // Storing float
		editorattach.commit();
	}

	public void Loadattachmax() {
		MaxId = AddTask.attach.getInt("2Max", 0);
	}

	public void Loadattach(int id) {
		AddTask.attach.getString(2 + "path" + id, null);
		AddTask.attach.getString(2 + "type" + id, null); // getting String
	}

	public void Removeattach(int id) {
		editorattach.remove(2 + "path" + id); // will delete key name
		editorattach.remove(2 + "type" + id); // will delete key email
		editorattach.commit();
	}

}
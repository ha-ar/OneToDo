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
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.PopupWindow.OnDismissListener;
import android.content.Intent;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.astuetz.PagerSlidingTabStrip;
import com.devspark.appmsg.AppMsg;
import com.vector.onetodo.R;
import com.vector.onetodo.R.anim;
import com.vector.onetodo.R.color;
import com.vector.onetodo.R.drawable;
import com.vector.onetodo.R.id;
import com.vector.onetodo.R.layout;
import com.vector.onetodo.db.gen.LabelName;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.ScaleAnimToHide;
import com.vector.onetodo.utils.ScaleAnimToShow;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

public class AddTaskFragment extends Fragment {

	// iMageview menu_dots_task,task_attachment edittext task_comment
	// linearlayout comment_box

	// HttpClient client;
	HttpPost post;
	List<NameValuePair> pairs;
	HttpResponse response = null;
	//add asyn;
	Uri filename;
	Editor editor, editorattach;
	String plabel = null;
	int pposition = -1;
	int itempos = -1;
	int MaxId = -1;
	private static int Tag = 0;
	private PopupWindow popupWindowAttach;

	// public PopupWindow popupImage;
	public static AQuery aq, popupAQ, aq_edit, aqd, aq_del, att, aqa, aq_menu;

	static List<java.lang.Object> names;
	int Label_postion = -1;
	ImageView last;

	static LinearLayout ll_iner;
	String[] colors1 = { "#790000", "#005826", "#0D004C", "#ED145B", "#E0D400",
			"#0000FF", "#4B0049", "#005B7F", "#603913", "#005952" };

	static int FragmentCheck = 0;
	static String repeatdate = "";
	static String checkedId2 = null, title = null;
	View label_view = null, viewl;
	private AutoCompleteTextView locationTextView;

	static ContactsCompletionView completionAssignView, completionShareView;

	static AlertDialog attach;
	Dialog add_new_label_alert, assig_alert, share_alert, date_time_alert,
			label_edit, location_del;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	int dayPosition;
	private String currentDay, currentMon, setmon1;

	private int[] collapsingViews = { R.id.title_task_layout1,
			R.id.date_time_include, R.id.before_grid_view_linear,
			R.id.repeat_linear_layout, R.id.label_grid_view3 };

	private int[] allViews = { R.id.task_title1, R.id.time_date,
			R.id.location_task, R.id.image, R.id.before1, R.id.repeat_task_lay,
			R.id.spinner_labels_task };

	public static EditText taskTitle;
	public static HashMap<Integer, Integer> inflatingLayouts = new HashMap<Integer, Integer>();

	static final String[] repeatArray = new String[] { "Never", "Daily",
			"Weekly", "Monthly", "Yearly" };

	static ImageView img;
	private final String[] labels_array = new String[] { "Personal", "Home",
			"Work", "New", "New", "New", "New", "New", "New" };

	private Person[] people;
	private ArrayAdapter<Person> adapter, shareAdapter;
	EditText label_field = null;

	protected static final int RESULT_CODE = 123;

	private static final int TAKE_PICTURE = 1;

	public static final int RESULT_GALLERY = 0;

	public static final int PICK_CONTACT = 2;

	private Uri imageUri;

	public static View allView;

	public static Activity act;

	private static View previousSelected;

	private int lastCheckedId = -1;

	public static AddTaskFragment newInstance(int position, int dayPosition) {
		AddTaskFragment myFragment = new AddTaskFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putInt("dayPosition", dayPosition);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.add_task_fragment, container,
				false);
		editor = AddTask.label.edit();
		editorattach = AddTask.attach.edit();
		aq = new AQuery(getActivity(), view);
		act = getActivity();
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

		inflatingLayouts.put(0, R.layout.add_task_title);
		inflatingLayouts.put(1, R.layout.add_task_assign1);
		inflatingLayouts.put(2, R.layout.add_task_details);
		inflatingLayouts.put(3, R.layout.add_task_time_date);
		inflatingLayouts.put(4, R.layout.add_task_location1);
		inflatingLayouts.put(5, R.layout.add_task_before);
		inflatingLayouts.put(6, R.layout.add_task_repeat);
		inflatingLayouts.put(7, R.layout.add_task_label);
		inflatingLayouts.put(8, R.layout.add_task_subtask);
		inflatingLayouts.put(9, R.layout.add_task_notes);
		inflatingLayouts.put(10, R.layout.add_task_attach);

		inflateLayouts();

		aqa = aq;

		aq.id(R.id.task_assign).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				title = aq.id(R.id.task_title1).getText().toString();
				Fragment fr = new AddTaskAssign();
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.setCustomAnimations(R.anim.slide_in1,
						R.anim.slide_out1);
				transaction.replace(R.id.main_container, fr);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

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

		aq.id(R.id.time_date)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.task_title1)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.location_task)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		aq.id(R.id.spinner_label_layout).clicked(new GeneralOnClickListner());

		aq.id(R.id.before1).clicked(new GeneralOnClickListner());

		aq.id(R.id.repeat_task_lay).clicked(new GeneralOnClickListner());

		aq.id(R.id.grid_text)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());
		final View view12 = getActivity().getLayoutInflater().inflate(
				R.layout.landing_menu, null, false);
		aq_menu = new AQuery(getActivity(), view12);
		popupWindowAttach = new PopupWindow(view12, Utils.getDpValue(200,
				getActivity()), WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindowAttach.setBackgroundDrawable(getResources().getDrawable(
				android.R.drawable.dialog_holo_light_frame));
		popupWindowAttach.setOutsideTouchable(true);
		popupWindowAttach.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

			}
		});

		// popupWindowTask.setAnimationStyle(R.style.Animation);

		/*
		 * CheckBox cb=(CheckBox)
		 * getActivity().findViewById(R.id.completed_task); aq.id(id)
		 */

		// *****************Title

		LayoutInflater inflater5 = getActivity().getLayoutInflater();

		View dialoglayout6 = inflater5.inflate(R.layout.add_task_edit, null,
				false);
		aq_edit = new AQuery(dialoglayout6);
		AlertDialog.Builder builder6 = new AlertDialog.Builder(getActivity());
		builder6.setView(dialoglayout6);
		label_edit = builder6.create();

		View dialoglayout7 = inflater5.inflate(R.layout.add_task_edit_delete,
				null, false);
		aq_del = new AQuery(dialoglayout7);
		AlertDialog.Builder builder7 = new AlertDialog.Builder(getActivity());
		builder7.setView(dialoglayout7);
		location_del = builder7.create();

		taskTitle = (EditText) aq.id(R.id.task_title1).getView();
		/*
		 * final EditText taskassign = (EditText) aq.id(R.id.task_assign)
		 * .getView(); taskassign.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence arg0, int arg1, int
		 * arg2, int arg3) { // TODO Auto-generated method stub //
		 * taskassign.setTextColor(R.color.active);
		 * aq.id(R.id.task_assign).textColorId(R.color.active);
		 * aq.id(R.id.assign_task_button).background( R.drawable.assign_blue);
		 * aq.id(R.id.imageView12).background(R.drawable.next_arrow_black);
		 * 
		 * }
		 * 
		 * @Override public void beforeTextChanged(CharSequence arg0, int arg1,
		 * int arg2, int arg3) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */
		taskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (taskTitle.getText().length() > 0)
					AddTask.btn.setAlpha(1);

				aq.id(R.id.completed_task).textColorId(R.color.active);
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

		// Date picker implementation
		DatePicker dPicker = (DatePicker) aq.id(R.id.date_picker).getView();
		int density = getResources().getDisplayMetrics().densityDpi;
		showRightDateAndTime();
		dPicker.init(currentYear, currentMonDigit, currentDayDigit,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						dayPosition = 3;
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
				aq.id(R.id.time_field).visible();
				showRightDateAndTime();
			}
		});

		if (density == DisplayMetrics.DENSITY_HIGH) {
			aq.id(R.id.date_picker).margin(-50, -20, -60, -40);
			aq.id(R.id.time_picker).margin(0, -36, -40, -40);
			dPicker.setScaleX(0.7f);
			dPicker.setScaleY(0.7f);
			tPicker.setScaleX(0.7f);
			tPicker.setScaleY(0.7f);
		}

		// ******************* label dialog
		GridView gridView;

		View vie = getActivity().getLayoutInflater().inflate(
				R.layout.add_label, null, false);

		aqd = new AQuery(vie);
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

				// buttonColor = (ColorDrawable) view.getBackground();
			}
		});

		AlertDialog.Builder builderLabel = new AlertDialog.Builder(
				getActivity());

		builderLabel.setView(vie);

		add_new_label_alert = builderLabel.create();
		add_new_label_alert
				.setOnDismissListener(new DialogInterface.OnDismissListener() {

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

						aq.id(R.id.spinner_labels_task).text(
								((TextView) label_view).getText().toString());
						aq.id(R.id.spinner_labels_task).getTextView()
								.setBackground(label_view.getBackground());
						aq.id(R.id.spinner_labels_task).getTextView()
								.setTextColor(Color.WHITE);
						/*
						 * aq.id(R.id.label_image).background(
						 * R.drawable.label_blue);
						 */
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
		aq.id(R.id.label_grid_view)
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

		aq.id(R.id.label_grid_view).getGridView()
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						itempos = position;
						label_view = view;
						if (!((TextView) view).getText().toString()
								.equalsIgnoreCase("new")) {
							aq.id(R.id.spinner_labels_task).text(
									((TextView) view).getText().toString());
							aq.id(R.id.spinner_labels_task).getTextView()
									.setBackground(view.getBackground());
							aq.id(R.id.spinner_labels_task).getTextView()
									.setTextColor(Color.WHITE);
							/*
							 * aq.id(R.id.label_image).background(
							 * R.drawable.label_blue);
							 */
						} else {
							add_new_label_alert
									.getWindow()
									.setBackgroundDrawable(
											new ColorDrawable(
													android.graphics.Color.TRANSPARENT));
							add_new_label_alert.show();

						}

					}

				});

		aq.id(R.id.label_grid_view).getGridView()
				.setOnItemLongClickListener(new LabelEditClickListener());

		aq_del.id(R.id.edit_cencel).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				location_del.dismiss();
			}
		});

		aq_del.id(R.id.edit_del).clicked(new OnClickListener() {

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

		aq_edit.id(R.id.add_task_delete).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				label_edit.dismiss();
				location_del.show();
			}
		});

		aq_edit.id(R.id.add_task_edit).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub=
				// aqd.id(R.id.add_label_text).text(((TextView)
				// viewl).getText().)
				aqd.id(R.id.label_title).text("Edit");
				aqd.id(R.id.save).text("Save");
				label_view = viewl;
				label_edit.dismiss();

				add_new_label_alert.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				add_new_label_alert.show();

			}
		});

		aq.id(R.id.image)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

		/**
		 * View pager for before and location
		 * 
		 */
		ViewPager pager = (ViewPager) aq.id(R.id.add_task_before_pager)
				.getView();

		pager.setAdapter(new AddTaskBeforePagerFragment(getActivity()
				.getSupportFragmentManager()));

		// Bind the tabs to the ViewPager
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) aq.id(
				R.id.add_task_before_tabs_task).getView();
		tabs.setDividerColorResource(android.R.color.transparent);
		tabs.setIndicatorColorResource(R.color._4d4d4d);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(getActivity(), 16));
		tabs.setIndicatorHeight(Utils.getPxFromDp(getActivity(), 1));
		tabs.setShouldExpand(false);
		tabs.setSmoothScrollingEnabled(true);
		tabs.setAllCaps(false);
		tabs.setTypeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE),
				Typeface.NORMAL);
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);

		aq.id(R.id.repeat_grid_view)
				.getGridView()
				.setAdapter(
						new ArrayAdapter<String>(getActivity(),
								R.layout.grid_layout_textview, repeatArray) {

							@Override
							public View getView(int position, View convertView,
									ViewGroup parent) {

								TextView textView = (TextView) super.getView(
										position, convertView, parent);
								if (/*
									 * textView.getText().toString()
									 * .equals("Weekly")
									 */position == 2) {
									previousSelected = textView;
									((TextView) textView)
											.setBackgroundResource(R.drawable.round_buttons_blue);
									((TextView) textView)
											.setTextColor(Color.WHITE);

									/*
									 * textView.setTextColor(Color.WHITE);
									 * GradientDrawable mDrawable =
									 * (GradientDrawable) getResources()
									 * .getDrawable(
									 * R.drawable.label_background);
									 * mDrawable.setColor(Color
									 * .parseColor(colors[position]));
									 * textView.setBackground(mDrawable);
									 */
								} else
									((TextView) textView)
											.setTextColor(getResources()
													.getColor(R.color._4d4d4d));
								// convertView.setSelected(true);
								return textView;
							}

						});

		aq.id(R.id.repeat_grid_view).itemClicked(new OnItemClickListener() {

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
					aq.id(R.id.forever_radio).checked(true);
					aq.id(R.id.time_radio).textColor(
							Color.parseColor("#bababa"));
					aq.id(R.id.forever_radio).textColor(
							(getResources().getColor(R.color._777777)));
				}
				((TextView) view).setTextColor(Color.WHITE);
				view.setSelected(true);
				if (repeatArray[position] == "Never") {
					aq.id(R.id.repeat).text(repeatArray[position])
					/* .textColorId(R.color.deep_sky_blue) */;
				} else {
					aq.id(R.id.repeat).text(repeatArray[position]);
				}
				previousSelected = view;

			}

		});
		LayoutInflater inflater4 = getActivity().getLayoutInflater();
		View dateTimePickerDialog = inflater4.inflate(
				R.layout.date_time_layout_dialog, null, false);
		AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
		builder4.setView(dateTimePickerDialog);

		date_time_alert = builder4.create();

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

		aq.id(R.id.time_radio).textColor(Color.parseColor("#bababa"));
		final TextView set = (TextView) dateTimePickerDialog
				.findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogDatePicker.clearFocus();
				set.requestFocus();
				date_time_alert.dismiss();

				aq.id(R.id.repeat).text(
						((TextView) previousSelected).getText().toString()
								+ " until " + setmon1);
				RadioButton rb = (RadioButton) aq.id(R.id.time_radio).getView();
				rb.setText(setmon1);
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
		aq.id(R.id.forever_radio).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (aq.id(R.id.repeat).getText().toString() == "Never") {
				} else {
					aq.id(R.id.repeat).text(
							((TextView) previousSelected).getText().toString());
				}

				aq.id(R.id.time_radio).textColor(Color.parseColor("#bababa"));
				aq.id(R.id.forever_radio).textColor(
						getResources().getColor(R.color._777777));
			}
		});

		aq.id(R.id.time_radio).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (aq.id(R.id.repeat).getText().toString().equals("Never")) {
					Toast.makeText(getActivity(), "Please Select ...",
							Toast.LENGTH_SHORT).show();
					aq.id(R.id.time_radio).checked(false);
					aq.id(R.id.forever_radio).checked(true);
					aq.id(R.id.time_radio).textColor(
							Color.parseColor("#bababa"));
					aq.id(R.id.forever_radio).textColor(
							getResources().getColor(R.color._777777));
				} else {

					aq.id(R.id.time_radio).textColor(
							getResources().getColor(R.color._777777));
					aq.id(R.id.forever_radio).textColor(
							Color.parseColor("#bababa"));
					date_time_alert.show();
				}
			}
		});

		/*
		 * // *************** Location locationTextView = (AutoCompleteTextView)
		 * aq.id(R.id.location_task) .getView(); locationTextView.setAdapter(new
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
		 * R.color.deep_sky_blue));
		 * aq.id(R.id.location_image).background(R.drawable.location_blue);
		 * 
		 * } });
		 */

		// **************Should be removed -- old
		// design************************//

		people = new Person[] {
				new Person("Usman Ameer", "de.uameer@example.com"),
				new Person("Khurram Nawaaz", "khurram@example.com"),
				new Person("Hasan Ali", "has@example.com"),
				new Person("Umer", "umer@example.com"),
				new Person("Faizan Chaudhary", "amanda@example.com"),
				new Person("Ali Mumtaz", "ali@example.com") };

		LayoutInflater inflater2 = getActivity().getLayoutInflater();
		View assigDialogLayout = inflater2.inflate(
				R.layout.add_assign_share_dialog, null, false);
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setView(assigDialogLayout);
		assig_alert = builder1.create();

		adapter = new ArrayAdapter<Person>(getActivity(),
				android.R.layout.simple_list_item_1, people);

		completionAssignView = (ContactsCompletionView) assigDialogLayout
				.findViewById(R.id.searchView);
		completionAssignView.setScroller(new Scroller(getActivity()));
		completionAssignView.setMaxLines(4);
		completionAssignView.setVerticalScrollBarEnabled(true);
		completionAssignView.setMovementMethod(new ScrollingMovementMethod());
		completionAssignView.setAdapter(adapter);

		TextView assignButton = (TextView) assigDialogLayout
				.findViewById(R.id.assign);
		assignButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				assig_alert.dismiss();
				int childCount = ((LinearLayout) aq.id(
						R.id.added_contacts_share_outer).getView())
						.getChildCount();
				if (childCount >= 2) {
					aq.id(R.id.share_task).gone();
					aq.id(R.id.share_task_replica).visible()
							.clicked(new ShareOnClickListner());
				}
				showSlectedContacts(R.id.added_contacts_outer, "Assigned to:",
						completionAssignView);
			}
		});
		TextView cancelB = (TextView) assigDialogLayout
				.findViewById(R.id.cancel);
		cancelB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				assig_alert.cancel();
			}
		});

		aq.id(R.id.assign_task)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new AssignOnClickListner());

		LayoutInflater inflater3 = getActivity().getLayoutInflater();
		View shareDialogLayout = inflater3.inflate(
				R.layout.add_assign_share_dialog, null, false);
		AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
		builder3.setView(shareDialogLayout);
		share_alert = builder3.create();

		shareAdapter = new ArrayAdapter<Person>(getActivity(),
				android.R.layout.simple_list_item_1, people);

		completionShareView = (ContactsCompletionView) shareDialogLayout
				.findViewById(R.id.searchView);
		completionShareView.setScroller(new Scroller(getActivity()));
		completionShareView.setMaxLines(4);
		completionShareView.setVerticalScrollBarEnabled(true);
		completionShareView.setMovementMethod(new ScrollingMovementMethod());
		completionShareView.setAdapter(shareAdapter);

		TextView shareButton = (TextView) shareDialogLayout
				.findViewById(R.id.assign);
		shareButton.setText("Share");
		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				share_alert.dismiss();
				int childCount = ((LinearLayout) aq.id(
						R.id.added_contacts_outer).getView()).getChildCount();
				if (childCount >= 2) {
					aq.id(R.id.share_task).gone();
					aq.id(R.id.share_task_replica).visible()
							.clicked(new ShareOnClickListner());
				}
				showSlectedContacts(R.id.added_contacts_share_outer,
						"Shared to:", completionShareView);

			}
		});
		TextView cancelShare = (TextView) shareDialogLayout
				.findViewById(R.id.cancel);
		cancelShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				share_alert.cancel();
			}
		});

		aq.id(R.id.share_task)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new ShareOnClickListner());

		aq.id(R.id.assign_task_button).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Fragment fr = new AddTaskAssign();
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.main_container, fr);
				transaction.setCustomAnimations(R.anim.slide_in1,
						R.anim.slide_out1);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

		// ***************************** Attachment
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View attachment = inflater
				.inflate(R.layout.add_attachment, null, false);
		att = new AQuery(attachment);

		LinearLayout ll = (LinearLayout) aq.id(R.id.added_image_outer)
				.getView();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout lll = (LinearLayout) arg0;
				Toast.makeText(getActivity(), lll.getChildCount() + "",
						Toast.LENGTH_LONG).show();
			}
		});

		// Gallery and Camera intent
		att.id(R.id.gallery1)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {

						attach.dismiss();
						Intent galleryIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(galleryIntent, RESULT_GALLERY);
					}
				});
		att.id(R.id.camera1)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {
						attach.dismiss();
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
		attach = attach_builder.create();
		aq.id(R.id.task_attachment).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// slideUpDown(aq.id(R.id.attachement_layout_include).getView());
				attach.show();
			}
		});

		// ********************** Attachment End

		// ***************************** Priority
		lastCheckedId = ((RadioGroup) aq.id(R.id.priority_radio_buttons)
				.getView()).getCheckedRadioButtonId();
		((RadioGroup) aq.id(R.id.priority_radio_buttons).getView())
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						((RadioButton) group.findViewById(lastCheckedId))
								.setTextColor(getResources().getColor(
										R.color.deep_sky_blue));
						((RadioButton) group.findViewById(checkedId))
								.setTextColor(getResources().getColor(
										android.R.color.white));
						String abc = ((RadioButton) group
								.findViewById(checkedId)).getText().toString();
						if (abc.equals("None"))
							AddTask.priority = 0;
						else if (abc.equals("!"))
							AddTask.priority = 1;
						else if (abc.equals("! !"))
							AddTask.priority = 2;
						else if (abc.equals("! ! !"))
							AddTask.priority = 3;
						lastCheckedId = checkedId;
					}
				});

		AddTask.aq_menu.id(R.id.menu_item2).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated m111111ethod stub
				if (taskTitle.length() > 0) {
					if (taskTitle.getText().length() > 0) {
						AddTask.aq_menu.id(R.id.menu_item1).textColor(
								R.color._4d4d4d);
						AddTask.aq_menu.id(R.id.menu_item2).textColor(
								R.color._4d4d4d);
						if (FragmentCheck == 0) {
							FragmentCheck = 1;
							title = aq.id(R.id.task_title1).getText()
									.toString();
							AddTask.popupWindowAdd.dismiss();
							Fragment fr = new AddTaskComment();
							FragmentManager manager = getFragmentManager();
							FragmentTransaction transaction = manager
									.beginTransaction();
							transaction.replace(R.id.main_container, fr);
							transaction.setCustomAnimations(R.anim.slide_in1,
									R.anim.slide_out1);
							transaction.addToBackStack(null);
							transaction.commit();
						}
					}
				}
			}
		});
		View switchView = aq.id(R.id.add_sub_task).getView();
		toggleCheckList(switchView);

		aq.id(R.id.image_menu).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "" + arg0.getId(),
						Toast.LENGTH_LONG).show();
			}
		});
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
			mChecklistManager.setNewEntryHint("Add a subtask");
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

	private void showSlectedContacts(int layout, String heading,
			ContactsCompletionView completionView) {
		final LinearLayout item = (LinearLayout) aq.id(layout).visible()
				.getView();
		item.removeAllViews();
		TextView textView = new TextView(getActivity());
		textView.setPadding(10, 10, 10, 10);
		aq.id(textView).text(heading).textSize(16f);
		item.addView(textView);
		names = completionView.getObjects();
		for (java.lang.Object name : names) {

			final View child = getActivity().getLayoutInflater().inflate(
					R.layout.image_added_layout, null);

			TextView text = (TextView) child
					.findViewById(R.id.image_added_text);
			text.setText(name.toString());
			child.findViewById(R.id.image_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							item.removeView(child);
						}
					});
			item.addView(child);
		}
	}

	private void showRightDateAndTime() {
		String tempCurrentDayDigit = String.format("%02d", currentDayDigit);
		String tempCurrentHours = String.format("%02d", currentHours);
		String tempCurrentMins = String.format("%02d", currentMin);
		if (dayPosition == 0) {
			aq.id(R.id.day_field).text("");
			aq.id(R.id.da);
			aq.id(R.id.day_field).text("Today");
		} else if (dayPosition == 1) {
			aq.id(R.id.day_field).text("");
			aq.id(R.id.day_field).text("Tomorrow");
		} else {
			aq.id(R.id.day_field).text(currentDay);
			aq.id(R.id.month_year_field).text(
					tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit) + " "
							+ currentMon);
		}
		aq.id(R.id.time_field).text(tempCurrentHours + ":" + tempCurrentMins);

	}

	private void showRightDateAndTimeForDialog() {
		String fff = String.valueOf(currentDayDigit).replace("th", "");
		setmon1 = fff + " " + currentMon + " " + currentYear;
		repeatdate = currentYear + "-" + (currentMonDigit + 1) + "-"
				+ currentDayDigit + " 00:00:00";
	}

	public static void inflateLayouts() {
		GridLayout gridLayout = (GridLayout) allView
				.findViewById(R.id.inner_container);
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

	public static void swapInflatedLayouts(int from, int to) {
		if (from < to) {
			for (int i = from; i < to; i++) {
				Integer temp = inflatingLayouts.get(i);
				inflatingLayouts.put(i, inflatingLayouts.get(i + 1));
				inflatingLayouts.put(i + 1, temp);
			}
		} else {
			for (int i = from; i > to; i--) {
				Integer temp = inflatingLayouts.get(i);
				inflatingLayouts.put(i, inflatingLayouts.get(i - 1));
				inflatingLayouts.put(i - 1, temp);
			}

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
			((FrameLayout) aq.id(R.id.add_task_frame).getView())
					.setForeground(tintColor);
		} else {
			// Hide the Panel
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.bottom_down);

			view.startAnimation(bottomDown);
			view.setVisibility(View.GONE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					android.R.color.transparent));
			((FrameLayout) aq.id(R.id.add_task_frame).getView())
					.setForeground(tintColor);
		}
	}

	private boolean isPanelShown(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

	private class AssignOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			assig_alert.show();
		}
	}

	private class ShareOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			share_alert.show();
		}
	}

	private class GeneralOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.setFocusableInTouchMode(true);
			v.requestFocus();
			showCurrentView(v);
			setAllOtherFocusableFalse(v);
			if (v.getId() == R.id.location_before
					|| v.getId() == R.id.task_title1 
					|| v.getId() == R.id.notes
					|| v.getId() == R.id.location_task)
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

				}
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

		MimeTypeMap mime = MimeTypeMap.getSingleton();

		String type = MimeTypeMap.getFileExtensionFromUrl(selectedImage
				.toString());

		Bitmap bitmap;
		if (FragmentCheck == 0) {
			try {
				bitmap = Utils.getBitmap(selectedImage, getActivity(), cr);
				final LinearLayout item = (LinearLayout) aq
						.id(R.id.added_image_outer).visible().getView();

				final View child = getActivity().getLayoutInflater().inflate(
						R.layout.image_added_layout, null);

				ImageView image = (ImageView) child
						.findViewById(R.id.image_added);
				ImageView imagemenu = (ImageView) child
						.findViewById(R.id.image_menu);
				Tag = Tag + 1;
				imagemenu.setTag(Tag);
				child.setId(Tag);

				imagemenu.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(),
								arg0.getId() + "     " + arg0.getTag(),
								Toast.LENGTH_LONG).show();
						ll_iner = (LinearLayout) item.findViewById(Integer
								.parseInt(arg0.getTag().toString()));

						if (popupWindowAttach.isShowing()) {
							popupWindowAttach.dismiss();

						} else {
							// layout_MainMenu.getForeground().setAlpha(150);
							popupWindowAttach.showAsDropDown(arg0, 5, 0);
						}
					}
				});

				aq_menu.id(R.id.menu_item1).text("Save file")
						.clicked(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								popupWindowAttach.dismiss();
							}
						});
				aq_menu.id(R.id.menu_item2).text("Delete")
						.clicked(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (ll_iner != null)
									item.removeView(ll_iner);
								popupWindowAttach.dismiss();
							}
						});

				aq.id(image).image(Utils.getRoundedCornerBitmap(bitmap, 7));
				TextView text = (TextView) child
						.findViewById(R.id.image_added_text);
				TextView by = (TextView) child
						.findViewById(R.id.image_added_by);
				TextView size = (TextView) child
						.findViewById(R.id.image_added_size);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
				by.setText("By Usman Ameer on " + sdf.format(cal.getTime()));
				filename = selectedImage;
				// AddTask.path.add(filename);
				File myFile = new File(selectedImage.toString());

				myFile.getAbsolutePath();
				imageupload();
				if (selectedImage.getLastPathSegment().contains(".")) {
					text.setText(selectedImage.getLastPathSegment());

				} else {
					text.setText(selectedImage.getLastPathSegment() + "."
							+ type);

				}

				size.setText("(" + (new File(selectedImage.getPath()).length())
						/ 1024 + " KB)");
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

		aq.id(R.id.calender_layout).background(R.drawable.input_fields_gray);
		aq.id(R.id.before_layout).background(R.drawable.input_fields_gray);
		aq.id(R.id.repeat_layout).background(R.drawable.input_fields_gray);
		aq.id(R.id.spinner_label_layout).background(
				R.drawable.input_fields_gray);
	}

	private void showCurrentView(View v) {

		hideAll();

		switch (v.getId()) {
		case R.id.time_date:
			if (aq.id(R.id.date_time_include).getView().getVisibility() == View.GONE) {
				aq.id(R.id.date_time_include)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.date_time_include)
												.getView(), true));
				aq.id(R.id.calender_layout).background(
						R.drawable.input_fields_blue);
			}
			break;
		case R.id.before1:

			if (aq.id(R.id.before_grid_view_linear).getView().getVisibility() == View.GONE) {
				if (aq.id(R.id.before).getText().toString() == "") {
					aq.id(R.id.before)
							.text(AddTaskBeforeFragment.beforeArray[1]
									+ " Before").visibility(View.VISIBLE);

				}
				aq.id(R.id.before_grid_view_linear)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200,
										aq.id(R.id.before_grid_view_linear)
												.getView(), true));

				aq.id(R.id.before_layout).background(
						R.drawable.input_fields_blue);
			}

			break;
		case R.id.repeat_task_lay:
			if (aq.id(R.id.repeat_linear_layout).getView().getVisibility() == View.GONE) {
				if (aq.id(R.id.repeat).getText().toString() == "") {
					aq.id(R.id.repeat).text(repeatArray[2])
							.visibility(View.VISIBLE);

				}
				aq.id(R.id.repeat_linear_layout)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.repeat_linear_layout)
												.getView(), true));

				aq.id(R.id.repeat_layout).background(
						R.drawable.input_fields_blue);

			}
			break;
		case R.id.spinner_label_layout:
			if (aq.id(R.id.label_grid_view3).getView().getVisibility() == View.GONE) {
				aq.id(R.id.label_grid_view3)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.label_grid_view3)
												.getView(), true));

				aq.id(R.id.spinner_label_layout).background(
						R.drawable.input_fields_blue);
			}

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
				gridView = inflater.inflate(R.layout.add_label_text, null);

				TextView textView = (TextView) gridView
						.findViewById(R.id.grid_item_label);
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
				return "By time";
			case 1:
				return "At location";
			default:
				return "";// not the case

			}
		}

		@Override
		public Fragment getItem(int position) {
			return AddTaskBeforeFragment.newInstance(position);
		}
	}

	public void label_add(String name) {
		// AddTask.labelnamedao.notifyAll();
		List<LabelName> labelname_list1 = AddTask.labelnamedao.loadAll();
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

	private class LabelEditClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			if (((TextView) arg1).getText().toString().equals("New")
					|| position < 3) {

			} else {
				aqd.id(R.id.add_label_text).text(
						((TextView) arg1).getText().toString());
				aq_del.id(R.id.body).text(
						"Label " + ((TextView) arg1).getText().toString()
								+ " will be deleted");
				aq_edit.id(R.id.add_task_edit_title).text(
						"Label: " + ((TextView) arg1).getText().toString());
				viewl = arg1;
				itempos = position;
				label_edit.show();
			}
			return false;
		}
	}

	/*
	 * public class ImageUploadTask extends AsyncTask<String, Integer, Void> {
	 * 
	 * @Override protected Void doInBackground(String... params) { // TODO
	 * Auto-generated method stub try { post.setEntity(new
	 * UrlEncodedFormEntity(pairs)); } catch (UnsupportedEncodingException e1) {
	 * // TODO Auto-generated catch block e1.printStackTrace(); }
	 * 
	 * try { response = client.execute(post); } catch (ClientProtocolException
	 * e1) { // TODO Auto-generated catch block e1.printStackTrace();
	 * asyn.cancel(true); } catch (IOException e1) { // TODO Auto-generated
	 * catch block e1.printStackTrace(); asyn.cancel(true); } return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result); String temp =
	 * null;
	 * 
	 * try { temp = EntityUtils.toString(response.getEntity()); } catch
	 * (org.apache.http.ParseException | IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); asyn.cancel(true); } Log.v("Response ",
	 * temp); }
	 * 
	 * @Override protected void onPreExecute() { // TODO Auto-generated method
	 * stub super.onPreExecute(); client = new DefaultHttpClient(); post = new
	 * HttpPost( "http://api.heuristix.net/one_todo/v1/upload.php"); pairs = new
	 * ArrayList<NameValuePair>(); if (filename.contains("external")) {
	 * 
	 * } else { String filepath = filename; } Log.v("PATH ", path.get(0)); File
	 * imagefile = new File(filepath); FileInputStream fis = null; try { fis =
	 * new FileInputStream(imagefile); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } Bitmap bm = BitmapFactory.decodeStream(fis);
	 * Bitmap bm = null; try { bm =
	 * MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
	 * filename); } catch (FileNotFoundException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } ByteArrayOutputStream
	 * baos = new ByteArrayOutputStream();
	 * bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); byte[] byteArray =
	 * baos.toByteArray(); String encoded = Base64.encodeToString(byteArray,
	 * Base64.DEFAULT); pairs.add(new BasicNameValuePair("image", encoded)); } }
	 */

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
						String path = null;
						try {

							JSONObject obj1 = new JSONObject(json.toString());
							path = obj1.getString("path");
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

	public void Save(String id, String name, int label_position) {
		// 0 - for private mode
		editor.putString(1 + "key_label" + id, name); // Storing integer
		editor.putInt(1 + "key_color_position" + id, label_position); // Storing
																		// float
		editor.commit();
	}

	public void Load(String id) {
		plabel = null;
		plabel = AddTask.label.getString(1 + "key_label" + id, null); // getting
																		// String
		Log.v("View id= ", id + "| " + plabel + " | " + pposition);

		pposition = AddTask.label.getInt(1 + "key_color_position" + id, 0); // getting
																			// String
	}

	public void Remove(String id) {
		editor.remove(1 + "key_label" + id); // will delete key name
		editor.remove(1 + "key_color_position" + id); // will delete key email
		editor.commit();
	}

	public void Saveattach(int id, String path, String type) {
		// 0 - for private mode
		editorattach.putInt("Max", id);
		editorattach.putString(1 + "path" + id, path);
		editorattach.putString(1 + "type" + id, type); // Storing float
		editorattach.commit();
	}

	public void Loadattachmax() {
		MaxId = AddTask.attach.getInt("Max", 0);
	}

	public void Loadattach(int id) {
		AddTask.attach.getString(1 + "path" + id, null);
		AddTask.attach.getString(1 + "type" + id, null); // getting String
	}

	public void Removeattach(int id) {
		editorattach.remove(1 + "path" + id); // will delete key name
		editorattach.remove(1 + "type" + id); // will delete key email
		editorattach.commit();
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
package com.vector.onetodo;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.androidquery.AQuery;
import com.devspark.appmsg.AppMsg;
import com.vector.onetodo.AddEventFragment.LabelImageAdapter;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.ScaleAnimToHide;
import com.vector.onetodo.utils.ScaleAnimToShow;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

public class Project2 extends Fragment {

	
	//event_attachment 
	public static AQuery aq, popupAQ, aqloc, AQlabel, AQlabel_edit,
			AQlabel_del;

	View label_view, viewl;
	GradientDrawable label_color;
	int Label_postion = -1;
	private int lastCheckedId = -1;
	ImageView last;
	String plabel = null;
	int pposition = -1;
	int itempos = -1;
	int MaxId = -1;
	EditText taskTitle;
	
	final String[] colors1 = { "#790000", "#005826", "#0D004C", "#ED145B",
			"#E0D400", "#0000FF", "#4B0049", "#005B7F", "#603913", "#005952" };
	private final String[] labels_array = new String[] { "Personal", "Home",
			"Work", "New", "New", "New", "New", "New", "New" };
	static List<java.lang.Object> names;

	public static HashMap<Integer, Integer> inflatingLayouts = new HashMap<Integer, Integer>();

	public static HashMap<Integer, Integer> inflatingLayoutsSelector = new HashMap<Integer, Integer>();

	private String currentDay, currentMon;
	static String checkedId2 = null;
	private Uri imageUri;
	public static final int RESULT_GALLERY = 0;

	public static final int PICK_CONTACT = 2;

	private static final int TAKE_PICTURE = 1;

	static LinearLayout lll;

	static ContactsCompletionView completionAssignView, completionShareView;

	AlertDialog label_edit, location_del, add_new_label_alert, assig_alert,
			share_alert, date_time_alert;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	int Month, Year;

	private int[] collapsingViews = { R.id.date_time_include,
			R.id.label_event_grid_view };

	private int[] allViews = { R.id.time_date, R.id.spinner_label_layout };

	static final String[] repeatArray = new String[] { "Once", "Daily",
			"Weekly", "Monthly", "Yearly" };

	int dayPosition;
	Editor editor;
	EditText label_field = null;

	protected static final int RESULT_CODE = 123;

	public static View allView;

	public static Activity act;

	public static Project2 newInstance(int position, int dayPosition) {
		Project2 myFragment = new Project2();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putInt("dayPosition", dayPosition);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.project_fragment, container,
				false);

		aq = new AQuery(getActivity(), view);
		act = getActivity();

		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		allView = getView();
		editor = AddTask.label.edit();
		dayPosition = getArguments().getInt("dayPosition", 0);
		currentDay = Utils.getCurrentDay(dayPosition, Calendar.SHORT);
		currentYear = Utils.getCurrentYear(dayPosition);
		currentMonDigit = Utils.getCurrentMonthDigit(dayPosition);
		currentDayDigit = Utils.getCurrentDayDigit(dayPosition);
		currentDay = Utils.getCurrentDay(dayPosition, Calendar.SHORT);
		currentMon = Utils.getCurrentMonth(dayPosition, Calendar.SHORT);
		currentHours = Utils.getCurrentHours();
		currentMin = Utils.getCurrentMins();

		// inflatingLayouts.put(0, R.layout.project_title);
		inflatingLayouts.put(0, R.layout.project_title);
		inflatingLayouts.put(1, R.layout.project_duration);
		inflatingLayouts.put(2, R.layout.project_task);
		inflatingLayouts.put(3, R.layout.project_note);

		inflateLayouts();
		main();
		
		taskTitle = (EditText) aq.id(R.id.project_title).getView();
		
		taskTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if(taskTitle.getText().length()>0)
					AddTask.btn.setAlpha(1);

				aq.id(R.id.completed_project).textColorId(R.color.active);
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	public void main() {

		aq.id(R.id.time_date)
				.typeface(
						TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE))
				.clicked(new GeneralOnClickListner());

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

		// **********************END DATE TIME

		// ****************************** Label Dialog

		aq.id(R.id.time_date).clicked(new GeneralOnClickListner());
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
	}

	public static void inflateLayouts() {
		GridLayout gridLayout = (GridLayout) allView
				.findViewById(R.id.inner_event_container);
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
		case R.id.time_date:
			if (aq.id(R.id.date_time_include).getView().getVisibility() == View.GONE)
				aq.id(R.id.date_time_include)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.date_time_include)
												.getView(), true));
			break;
		case R.id.spinner_label_layout:
			if (aq.id(R.id.label_event_grid_view).getView().getVisibility() == View.GONE)
				aq.id(R.id.label_event_grid_view)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(R.id.label_event_grid_view)
												.getView(), true));
			break;

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
			if (v.getId() == R.id.time_date
					|| v.getId() == R.id.spinner_label_layout)
				Utils.showKeyboard(getActivity());
			else
				Utils.hidKeyboard(getActivity());
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
			/*
			 * ((FrameLayout) aq.id(R.id.add_project_frame).getView())
			 * .setForeground(tintColor);
			 */
		} else {
			// Hide the Panel
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.bottom_down);

			view.startAnimation(bottomDown);
			view.setVisibility(View.GONE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					android.R.color.transparent));
			/*
			 * ((FrameLayout) aq.id(R.id.add_project_frame).getView())
			 * .setForeground(tintColor);
			 */
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
		String tempYear = String.valueOf(currentYear).substring(2, 4);
		aq.id(R.id.da).text("Due").textColorId(R.color.deep_sky_blue);
		if (dayPosition == 0) {
			aq.id(R.id.day_field).text("").textColorId(R.color.deep_sky_blue);
			aq.id(R.id.da).textColorId(R.color.deep_sky_blue);
			aq.id(R.id.day_field).text("Today")
					.textColorId(R.color.deep_sky_blue);
		} else if (dayPosition == 1) {
			aq.id(R.id.day_field).text("").textColorId(R.color.deep_sky_blue);
			aq.id(R.id.da).textColorId(R.color.deep_sky_blue);
			aq.id(R.id.day_field).text("Tomorrow")
					.textColorId(R.color.deep_sky_blue);
		} else {
			aq.id(R.id.day_field).text(currentDay)
					.textColorId(R.color.deep_sky_blue);
			aq.id(R.id.da).textColorId(R.color.deep_sky_blue);
			aq.id(R.id.month_year_field)
					.text(tempCurrentDayDigit
							+ Utils.getDayOfMonthSuffix(currentDayDigit) + " "
							+ currentMon).textColorId(R.color.deep_sky_blue);
		}
		aq.id(R.id.time_field).text(tempCurrentHours + ":" + tempCurrentMins)
				.textColorId(R.color.deep_sky_blue);
		aq.id(R.id.calendare_image).background(R.drawable.calendar_blue);
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

	public void Save(String id, String name, int label_position) {
		// 0 - for private mode
		editor.putString(5 + "key_label" + id, name); // Storing integer
		editor.putInt(5 + "key_color_position" + id, label_position); // Storing
																		// float
		editor.commit();
	}

	public void Load(String id) {
		plabel = null;
		plabel = AddTask.label.getString(5 + "key_label" + id, null); // getting
																		// String
		Log.v("View id= ", id + "| " + plabel + " | " + pposition);

		pposition = AddTask.label.getInt(5 + "key_color_position" + id, 0); // getting
																			// String
	}

	public void Remove(String id) {
		editor.remove(5 + "key_label" + id); // will delete key name
		editor.remove(5 + "key_color_position" + id); // will delete key email
		editor.commit();
	}
}

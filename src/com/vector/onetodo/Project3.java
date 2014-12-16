/*package com.vector.onetodo;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.devspark.appmsg.AppMsg;
import com.vector.one_todo.R;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.ScaleAnimToHide;
import com.vector.onetodo.utils.ScaleAnimToShow;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

public class Project3 extends Fragment {

	public static AQuery aq, popupAQ, aqloc;

	static List<java.lang.Object> names;

	private int lastCheckedId = -1;
	static String checkedId2 = null;

	static LinearLayout lll;
	private Uri imageUri;

	private int[] allViews = { R.id.date_time_project };

	private int[] collapsingViews = { R.id.date_time_include_project };

	private static final int TAKE_PICTURE = 1;

	static ContactsCompletionView completionAssignView, completionShareView;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	public static HashMap<Integer, Integer> inflatingLayouts = new HashMap<Integer, Integer>();

	EditText label_field = null;

	protected static final int RESULT_CODE = 123;

	public static final int RESULT_GALLERY = 0;
	
	private String currentDay, currentMon;

	public static final int PICK_CONTACT = 2;

	public static View allView;

	public static Activity act;

	public static Project3 newInstance(int position, int dayPosition) {
		Project3 myFragment = new Project3();
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
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view1 = inflater.inflate(R.layout.appoinment_selector_header,
				null, false);
		GridView gridView = (GridView) view1.findViewById(R.id.gridView1);


		final int dayPosition = 0;
		currentDay = Utils.getCurrentDay(dayPosition, Calendar.SHORT);
		currentMon = Utils.getCurrentMonth(dayPosition, Calendar.SHORT);
		// Instance of ImageAdapter Class
		gridView.setAdapter(new ImageAdapter(getActivity()));

		allView = getView();
		inflatingLayouts.put(0, R.layout.project_title1);
		inflatingLayouts.put(1, R.layout.project_date);
		inflatingLayouts.put(2, R.layout.project_task1);
		inflatingLayouts.put(3, R.layout.project_note1);
		inflatingLayouts.put(4, R.layout.project_image);

		inflateLayouts();

		

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

		
		
		// Date picker implementation
				DatePicker dPicker = (DatePicker) aq.id(R.id.date_picker_project).getView();
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
				TimePicker tPicker = (TimePicker) aq.id(R.id.time_picker_project).getView();
				tPicker.setIs24HourView(true);
				tPicker.setOnTimeChangedListener(new OnTimeChangedListener() {

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
					dPicker.setScaleX(0.7f);
					dPicker.setScaleY(0.7f);
					tPicker.setScaleX(0.7f);
					tPicker.setScaleY(0.7f);
				}
		
		
		lastCheckedId = ((RadioGroup) aq.id(R.id.priority_radio_button).getView()).getCheckedRadioButtonId();
		((RadioGroup) aq.id(R.id.priority_radio_button).getView())
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

	}

	public static void inflateLayouts() {
		GridLayout gridLayout = (GridLayout) allView
				.findViewById(R.id.inner_container_project);
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

	public class ImageAdapter extends BaseAdapter {

		private Context mContext;

		// Keep all Images in array
		public Integer[] mThumbIds = { R.drawable.img1, R.drawable.img2,
				R.drawable.img3, R.drawable.img4, R.drawable.img5,
				R.drawable.img6, R.drawable.img7, R.drawable.img8 };

		// Constructor
		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mThumbIds.length;
		}

		@Override
		public java.lang.Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mThumbIds[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view1 = inflater.inflate(R.layout.appoinment_selector_item,
					arg2, false);
			ImageView imageView = (ImageView) arg1
					.findViewById(R.id.grid_item_label);
			imageView.setImageResource(mThumbIds[arg0]);
			return arg1;
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
			((FrameLayout) aq.id(R.id.add_project_frame).getView())
					.setForeground(tintColor);
		} else {
			// Hide the Panel
			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.bottom_down);

			view.startAnimation(bottomDown);
			view.setVisibility(View.GONE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					android.R.color.transparent));
			((FrameLayout) aq.id(R.id.add_project_frame).getView())
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

	private void showCurrentView(View v) {

		hideAll();

		switch (v.getId()) {

		case R.id.date_time_project:
			if (aq.id(R.id.date_time_include_project).getView().getVisibility() == View.GONE)
				aq.id(R.id.date_time_include_project)
						.getView()
						.startAnimation(
								new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f,
										200, aq.id(
												R.id.date_time_include_project)
												.getView(), true));
			break;
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

	private class GeneralOnClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.setFocusableInTouchMode(true);
			v.requestFocus();
			showCurrentView(v);
			setAllOtherFocusableFalse(v);
			if (v.getId()==R.id.date_time_project)
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
	
	private void showRightDateAndTime() {
		String tempCurrentDayDigit = String.format("%02d", currentDayDigit);
		String tempCurrentHours = String.format("%02d", currentHours);
		String tempCurrentMins = String.format("%02d", currentMin);
		String tempYear = String.valueOf(currentYear).substring(2, 4);
		
		aq.id(R.id.date_time_project).text(currentDay+" "+tempCurrentHours + ":" + tempCurrentMins)
				.textColorId(R.color.deep_sky_blue);
		aq.id(R.id.date_time_project_img).background(R.drawable.calendar_blue);
	}
}*/
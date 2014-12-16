package com.vector.onetodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.simonvt.datepicker.DatePicker;
import net.simonvt.datepicker.DatePicker.OnDateChangedListener;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;
import net.simonvt.menudrawer.Position;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
import com.vector.model.TaskData;
import com.vector.model.TaskData.Todos;
import com.vector.onetodo.TaskListFragment.Holder;
import com.vector.onetodo.db.gen.DaoMaster;
import com.vector.onetodo.db.gen.DaoMaster.DevOpenHelper;
import com.vector.onetodo.db.gen.DaoSession;
import com.vector.onetodo.db.gen.Label;
import com.vector.onetodo.db.gen.LabelDao;
import com.vector.onetodo.db.gen.LabelNameDao;
import com.vector.onetodo.db.gen.ToDo;
import com.vector.onetodo.db.gen.ToDoDao;
import com.vector.onetodo.utils.AlphaForegroundColorSpan;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;
import com.zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import com.zh.wang.android.apis.yweathergetter4a.YahooWeather;
import com.zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

import de.greenrobot.dao.query.QueryBuilder;

public class MainActivity extends BaseActivity implements
		YahooWeatherInfoListener, ScrollTabHolder,
		ViewPager.OnPageChangeListener, OnItemClickListener {

	// ************* Save data for setting
	public static SharedPreferences setting;
	Editor setting_editor;
	public static Calendar CurrentDate;

	static int check = -1, check1 = 0;;
	public static int menuchange = 0;
	private PopupWindow popupWindowTask;
	public static FrameLayout layout_MainMenu;
	InputMethodManager inputMethodManager;
	AlertDialog date_time_alert;
	LinearLayout itemadded;
	public static DevOpenHelper ex_database_helper_obj;
	public static SQLiteDatabase ex_db;
	public static DaoSession daoSession;
	public static DaoMaster daoMaster;
	public static List<ToDo> todos;
	private MenuDrawer mDrawer, mDrawerr;
	public static int pager_number = 0;
	private AlarmManagerBroadcastReceiver alarm;
	private SQLiteDatabase db;
	static ToDoDao tododao;
	static LabelNameDao labelnamedao;
	static LabelDao labeldao;
	static List<ToDo> todo_obj;
	// private Long id = null;
	public ViewPager pager;
	public TabPagerAdapter tabPagerAdapter;
	PagerSlidingTabStrip tabs;
	/*
	 * private int[] menuDrawerCollapsingLayouts = { R.id.tasks_inner,
	 * R.id.settings_inner };
	 */
	static QueryBuilder<Label> label1, label2, label3, label4, label5, label6;
	// private ImageView weatherImage;
	private GifAnimationDrawable weatherBackground;
	public static YahooWeather mYahooWeather;
	public static WeatherInfo weatherInfo;
	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();
	private TypedValue mTypedValue = new TypedValue();
	private View mHeader;
	private ImageView mHeaderLogo;
	private AccelerateDecelerateInterpolator mSmoothInterpolator = new AccelerateDecelerateInterpolator();
	private int mActionBarTitleColor;

	public static List<Todos> Today, Tomorrow, Upcoming;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ***** Initializinf Setting shared prefrences**********//
		setting = this.getSharedPreferences("setting", 0);
		Constants.date = setting.getBoolean("date", true);
		Constants.time = setting.getBoolean("time", true);
		Constants.week = setting.getBoolean("week", true);

		db_initialize();
		todo_obj = tododao.loadAll();
		alarm = new AlarmManagerBroadcastReceiver();
		startRepeatingTimer();

		// ***** Left Drawer**********//
		{
			mDrawer = MenuDrawer.attach(this, Type.OVERLAY, Position.LEFT,
					MenuDrawer.MENU_DRAG_WINDOW);
			mDrawer.setContentView(R.layout.activity_main);
			mDrawer.setDropShadowEnabled(false);
			mDrawer.setDrawOverlay(true);
			mDrawer.setMenuView(R.layout.menu_drawer);
		}
		// ***** Right Drawer**********//
		{
			mDrawerr = MenuDrawer.attach(this, Type.OVERLAY, Position.RIGHT,
					MenuDrawer.MENU_DRAG_WINDOW);
			mDrawerr.setContentView(R.layout.activity_main);
			mDrawerr.setMenuView(R.layout.menu_drawer_right);
			mDrawerr.setDropShadowEnabled(false);
			mDrawerr.setDrawOverlay(true);

		}

		// ***** Initializinf Registration shared prefrences**********//
		SharedPreferences pref = this.getSharedPreferences("registration", 0);
		Constants.user_id = pref.getInt("userid", -1);

		// **************************Api Call for Landing data
		if (Constants.user_id != -1) {
			aq.ajax("http://api.heuristix.net/one_todo/v1/tasks/"
					+ Constants.user_id, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {
							Log.v("New ", Constants.user_id + "inside");
							if (json != null) {
								Gson gson = new Gson();
								TaskData obj = new TaskData();
								obj = gson.fromJson(json.toString(),
										TaskData.class);
								TaskData.getInstance().setList(obj);
								Log.v("JSON",
										TaskData.getInstance().todos.get(0).notes
												+ "");
							}
							init();
						}
					});
		} else {
			init();
		}

	}

	private void init() {

		ListView notif_list = (ListView) findViewById(R.id.notif_list);
		Notify_adapter adapter = new Notify_adapter(this);
		notif_list.setAdapter(adapter);

		// ***** LeftMenudrawer Mange Account feld**********//
		if (Constants.user_id == -1) {/*
									 * aq.id(R.id.manage_img).image(R.drawable.
									 * allday_blue);
									 */
			aq.id(R.id.manage_text).text("Verify number");
		} else {
			aq.id(R.id.username).text("Registered User");
			aq.id(R.id.imageView1).text("RU");
		}

		aq.id(R.id.manage_account).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (check1 == 0) {
					check1 = 1;
					aq.id(R.id.manage_account)
							.getTextView()
							.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
									R.drawable.ic_up);
					aq.id(R.id.manage).visibility(View.VISIBLE);
				} else {
					check1 = 0;
					aq.id(R.id.manage_account)
							.getTextView()
							.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
									R.drawable.ic_down);
					aq.id(R.id.manage).visibility(View.GONE);
				}

			}
		});

		// ***** Initializinf Today, tomorrow, upcoming and seprate save for
		// each**********//
		Today = new ArrayList<Todos>();
		Tomorrow = new ArrayList<Todos>();
		Upcoming = new ArrayList<Todos>();

		CurrentDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = null;
		try {
			today = sdf.parse(sdf.format(CurrentDate.getTime()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CurrentDate.add(Calendar.DATE, 1);
		Date tomorrow = null;
		try {
			tomorrow = sdf.parse(sdf.format(CurrentDate.getTime()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Date date = null;
		for (int i = 0; i < TaskData.getInstance().todos.size(); i++) {
			if (TaskData.getInstance().todos.get(i).start_date != null) {
				try {
					date = sdf
							.parse(TaskData.getInstance().todos.get(i).start_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (date.equals(today)) {
					Log.v("Today", date + "   " + today);
					Today.add((TaskData.getInstance().todos.get(i)));
				} else if (date.equals(tomorrow)) {
					Log.v("Tomorrow", date + "   " + tomorrow);
					Tomorrow.add((TaskData.getInstance().todos.get(i)));
				} else if (date.after(tomorrow)) {
					Log.v("upcoming", date + "   " + tomorrow);
					Upcoming.add((TaskData.getInstance().todos.get(i)));
				}
			}
		}

		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		parallaxHeaderInit();

		// ********* Old
		aq.id(R.id.navigation_menu).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu(true);
			}
		});

		// ***** right drawer open close**********//
		aq.id(R.id.notif).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!(mDrawerr.isActivated())) {
					mDrawerr.openMenu();
				} else {
					mDrawerr.closeMenu();
				}
			}
		});

		// ***** left drawer open close**********//
		aq.id(R.id.right_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerr.closeMenu();
			}
		});

		layout_MainMenu = (FrameLayout) findViewById(R.id.container);
		// layout_MainMenu.getForeground().setAlpha(0);
		final View view = getLayoutInflater().inflate(R.layout.landing_menu,
				null, false);
		aq_menu = new AQuery(this, view);
		popupWindowTask = new PopupWindow(view, Utils.getDpValue(200, this),
				WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindowTask.setBackgroundDrawable(getResources().getDrawable(
				android.R.drawable.dialog_holo_light_frame));
		popupWindowTask.setOutsideTouchable(true);
		// popupWindowTask.setAnimationStyle(R.style.Animation);

		popupWindowTask.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
			}
		});

		aq.id(R.id.menu).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (popupWindowTask.isShowing()) {
					popupWindowTask.dismiss();

				} else {
					// layout_MainMenu.getForeground().setAlpha(150);
					popupWindowTask.showAsDropDown(aq.id(R.id.menu).getView(),
							5, 10);
				}
			}
		});

		// DATE Dialog
		View dateTimePickerDialog = getLayoutInflater().inflate(
				R.layout.landing_date_dialog, null, false);
		AlertDialog.Builder builder4 = new AlertDialog.Builder(
				MainActivity.this);
		builder4.setView(dateTimePickerDialog);

		date_time_alert = builder4.create();
		aqd = new AQuery(dateTimePickerDialog);
		aqd.id(R.id.cancel).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				date_time_alert.dismiss();
			}
		});

		aqd.id(R.id.ok).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		DatePicker dialogDatePicker = (DatePicker) dateTimePickerDialog
				.findViewById(R.id.date_picker_dialog);
		Calendar cal = Calendar.getInstance();
		cal.set(Utils.getCurrentYear(1), Utils.getCurrentMonthDigit(1),
				Utils.getCurrentDayDigit(1));
		aqd.id(R.id.title).text(
				cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
						Locale.US)
						+ ", "
						+ Utils.getCurrentDayDigit(1)
						+ " "
						+ cal.getDisplayName(Calendar.MONTH, Calendar.SHORT,
								Locale.US) + " " + Utils.getCurrentYear(1));

		dialogDatePicker.init(Utils.getCurrentYear(1),
				Utils.getCurrentMonthDigit(1), Utils.getCurrentDayDigit(1),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						aqd.id(R.id.title).text(
								cal.getDisplayName(Calendar.DAY_OF_WEEK,
										Calendar.SHORT, Locale.US)
										+ ", "
										+ dayOfMonth
										+ " "
										+ cal.getDisplayName(Calendar.MONTH,
												Calendar.SHORT, Locale.US)
										+ " " + year);

					}

				});

		aq_menu.id(R.id.menu_item1).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				popupWindowTask.dismiss();
				if (menuchange == 1) {

					Fragment fr = new Invitations();
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.container, fr);
					transaction.addToBackStack("invitation");
					transaction.commit();

				}
			}
		});
		aq_menu.id(R.id.menu_item2).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindowTask.dismiss();
			}
		});
		aq_menu.id(R.id.menu_item3).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindowTask.dismiss();
				date_time_alert.show();
			}
		});

		aq.id(R.id.search_button).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (menuchange == 0) {
				inputMethodManager.toggleSoftInput(
						InputMethodManager.SHOW_FORCED, 0);

				aq.id(R.id.search_layout).getView().setVisibility(View.VISIBLE);
				aq.id(R.id.header_layout).getView().setVisibility(View.GONE);
				aq.id(R.id.search_text).getEditText().setFocusable(true);
				// }
			}
		});
		aq.id(R.id.search_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				inputMethodManager.toggleSoftInput(
						InputMethodManager.SHOW_FORCED, 0);
				aq.id(R.id.search_layout).getView().setVisibility(View.GONE);
				aq.id(R.id.header_layout).getView().setVisibility(View.VISIBLE);
			}
		});

		aq.id(R.id.header_logo).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!(mDrawer.isActivated())) {
					mDrawer.openMenu();
				} else {
					mDrawer.closeMenu();
				}
			}
		});

		// Menu Drawer on click change items

		aq.id(R.id.settings).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getSupportFragmentManager().popBackStack("ACCOUNTS",
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				Fragment fr = new Setting();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.container, fr);
				transaction.addToBackStack("SETTING");
				transaction.commit();
				mDrawer.closeMenu();

			}
		});

		aq.id(R.id.manage).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Constants.user_id == -1) {

				} else {
					getSupportFragmentManager().popBackStack("SETTING",
							FragmentManager.POP_BACK_STACK_INCLUSIVE);
					Fragment fr = new Accounts();
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.container, fr);
					transaction.addToBackStack("ACCOUNTS");
					transaction.commit();
					mDrawer.closeMenu();
				}

			}
		});

		aq.id(R.id.todo_layout).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView title = (TextView) findViewById(R.id.weather);
				title.setText("To-do's");
				getSupportFragmentManager().popBackStack();
				mDrawer.closeMenu();
				arg0.setBackgroundColor(Color.parseColor("#F2F2F2"));

				aq.id(R.id.todo_image).image(R.drawable.list_blue);
				aq.id(R.id.todo_text).textColor(Color.parseColor("#33B5E5"));

				aq.id(R.id.calendar_layout).getView()
						.setBackgroundColor(Color.parseColor("#ffffff"));
				aq.id(R.id.calendar_image).image(R.drawable.calendar_black);
				aq.id(R.id.calendar_text)
						.textColor(Color.parseColor("#000000"));

				aq.id(R.id.project_layout).getView()
						.setBackgroundColor(Color.parseColor("#ffffff"));
				aq.id(R.id.project_image).image(R.drawable.progress_black);
				aq.id(R.id.project_text).textColor(Color.parseColor("#000000"));
			}
		});

		aq.id(R.id.calendar_layout).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				aq_menu.id(R.id.menu_item1).text("Invitations");
				aq_menu.id(R.id.menu_item2).text("Visible calenders");
				aq_menu.id(R.id.menu_item3).text("Go to")
						.visibility(View.VISIBLE);

				getSupportFragmentManager().popBackStack();
				mDrawer.closeMenu();
				menuchange = 1;

				arg0.setBackgroundColor(Color.parseColor("#F2F2F2"));

				aq.id(R.id.todo_image).image(R.drawable.list_black);
				aq.id(R.id.todo_text).textColor(Color.parseColor("#000000"));

				aq.id(R.id.todo_layout).getView()
						.setBackgroundColor(Color.parseColor("#ffffff"));
				aq.id(R.id.calendar_image).image(R.drawable.calendar_blue);
				aq.id(R.id.calendar_text)
						.textColor(Color.parseColor("#33B5E5"));

				aq.id(R.id.project_layout).getView()
						.setBackgroundColor(Color.parseColor("#ffffff"));
				aq.id(R.id.project_image).image(R.drawable.progress_black);
				aq.id(R.id.project_text).textColor(Color.parseColor("#000000"));

				Fragment fr = new Calender();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction(); //
				/*
				 * transaction.setCustomAnimations(R.anim.slide_in,
				 * R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
				 */
				transaction.replace(R.id.container_inner, fr);
				transaction.addToBackStack("CALENDAR");
				transaction.commit();

			}
		});

		aq.id(R.id.project_layout).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getSupportFragmentManager().popBackStack();
				TextView title = (TextView) findViewById(R.id.weather);
				title.setText("Projects");

				mDrawer.closeMenu();
				arg0.setBackgroundColor(Color.parseColor("#F2F2F2"));

				aq.id(R.id.todo_image).image(R.drawable.list_black);
				aq.id(R.id.todo_text).textColor(Color.parseColor("#000000"));

				aq.id(R.id.calendar_layout).getView()
						.setBackgroundColor(Color.parseColor("#ffffff"));
				aq.id(R.id.calendar_image).image(R.drawable.calendar_black);
				aq.id(R.id.calendar_text)
						.textColor(Color.parseColor("#000000"));

				aq.id(R.id.todo_layout).getView()
						.setBackgroundColor(Color.parseColor("#Ffffff"));
				aq.id(R.id.project_image).image(R.drawable.progress_blue);
				aq.id(R.id.project_text).textColor(Color.parseColor("#33B5E5"));

				Fragment fr = new Projects();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction(); //
				/*
				 * transaction.setCustomAnimations(R.anim.slide_in,
				 * R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
				 */

				transaction.replace(R.id.container_inner, fr);
				transaction.addToBackStack("PROJECTS");
				transaction.commit();

			}
		});

		final LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		params.gravity = Gravity.CENTER_HORIZONTAL;

		// init database
		ex_database_helper_obj = new DaoMaster.DevOpenHelper(this,
				"onetodo.sqlite", null);
		ex_db = ex_database_helper_obj.getWritableDatabase();
		daoMaster = new DaoMaster(ex_db);
		daoSession = daoMaster.newSession();

		if (pager_number == 0) {
			// getUpdatedTaskList(TODAY);
			updateDate(TODAY);
		}
		if (pager_number == 1) {
			// getUpdatedTaskList(Work);
			updateDate(Work);
		}

		// Initialize the ViewPager and set an adapter
		pager = (ViewPager) findViewById(R.id.pager);
		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		tabPagerAdapter.setTabHolderScrollingContent(this);
		pager.setAdapter(tabPagerAdapter);

		// Bind the tabs to the ViewPager
		tabs = (PagerSlidingTabStrip) aq.id(R.id.tabs).getView();
		tabs.setShouldExpand(false);
		tabs.setDividerColorResource(android.R.color.transparent);
		// tabs.setIndicatorColorResource(R.color.graytab);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(this, 16));
		tabs.setIndicatorHeight(Utils.getPxFromDp(this, 3));
		tabs.setIndicatorColor(Color.parseColor("#ffffff"));
		tabs.setSmoothScrollingEnabled(true);
		tabs.setShouldExpand(false);
		// tabs.setTextColorResource(R.color.graytab);
		tabs.setAllCaps(false);
		tabs.setTypeface(null, Typeface.NORMAL);
		tabs.setOnPageChangeListener(this);

		tabs.setViewPager(pager);
		tabPagerAdapter.notifyDataSetChanged();

		aq.id(R.id.add_task_button)
				.typeface(TypeFaces.get(this, Constants.ICON_FONT))
				.clicked(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(MainActivity.this,
								AddTask.class);
						intent.putExtra("position", pager.getCurrentItem());
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in1,
								R.anim.slide_out1);
					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (tabPagerAdapter != null) {
			tabPagerAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void parallaxHeaderInit() {
		mHeader = (LinearLayout) findViewById(R.id.header);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mMinHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		mActionBarTitleColor = getResources().getColor(android.R.color.white);
		mSpannableString = new SpannableString("One ToDo");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);
	}

	void updateDate(int days) {
		aq.id(R.id.current_date).text(
				String.valueOf(Utils.getCurrentDate(days)));
		aq.id(R.id.day).text(Utils.getCurrentDay(days, Calendar.LONG));
		aq.id(R.id.mon_year).text(
				Utils.getCurrentMonth(days, Calendar.SHORT) + "'"
						+ Utils.getCurrentYear(days));
	}

	/*
	 * private void hideAllShowingLayout(int currentViewId) { for (int id :
	 * menuDrawerCollapsingLayouts) { if (id != currentViewId &&
	 * aq.id(id).getView().getVisibility() == View.VISIBLE) aq.id(id) .getView()
	 * .startAnimation( new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 200,
	 * aq.id(id).getView(), true)); } }
	 */

	public class TabPagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private ScrollTabHolder mListener;

		public TabPagerAdapter(FragmentManager fm) {
			super(fm);
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		}

		public void setTabHolderScrollingContent(ScrollTabHolder listener) {
			mListener = listener;
		}

		@Override
		public int getCount() {
			if (pager_number == 0)
				return pageName.size();
			else if (pager_number == 1)
				return pagename2.size();
			else
				return typeName.size();
			// return 3; // no. of tabs are Today, Tomorrow & Upcoming
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (pager_number == 0)
				return pageName.get(position);
			else if (pager_number == 1)
				return pagename2.get(position);
			else
				return typeName.get(position);
		}

		@Override
		public Fragment getItem(int position) {
			ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) TaskListFragment
					.newInstance(position);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}
			return fragment;
		}

		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

	}

	public void startRepeatingTimer() {
		// Context context = this.getApplicationContext();
		if (alarm != null) {
			// alarm.SetAlarm(this);

		} else {
			Toast.makeText(this, "Alarm is null", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancelRepeatingTimer(View view) {
		Context context = this.getApplicationContext();
		if (alarm != null) {
			alarm.CancelAlarm(context);
		} else {
			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		}
	}

	public void onetimeTimer(View view) {
		Context context = this.getApplicationContext();
		if (alarm != null) {

			alarm.setOnetimeTimer(context);
		} else {
			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		}
	}

	public void db_initialize() {
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
				"OneTodo-db", null);

		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		tododao = daoSession.getToDoDao();
		labeldao = daoSession.getLabelDao();
		labelnamedao = daoSession.getLabelNameDao();

	}

	public static void alrammmm(Context context) {
		NotificationHandler nHandler;
		nHandler = NotificationHandler.getInstance(context);
		nHandler.createSimpleNotification(context);
	}

	private int getWeatherImage(WeatherInfo weatherInfo) {
		if (weatherInfo != null) {
			String weather = weatherInfo.getCurrentText().toLowerCase();
			if (weather.equalsIgnoreCase("haze")) {
				return R.raw.sunny;
			} else if (weather.contains("snow")) {
				return R.raw.snow;
			} else if (weather.contains("rain")) {
				return R.raw.rain;
			} else if (weather.contains("thunder")) {
				return R.raw.thunder;
			} else if (weather.contains("sun")) {
				return R.raw.sunny;
			} else if (weather.contains("cloudy")) {
				return R.raw.cloudy;
			}

		}
		return R.raw.demo;
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		MainActivity.weatherInfo = weatherInfo;

		if (weatherInfo != null) {
			Spanned html = Html.fromHtml(weatherInfo.getCurrentTempC()
					+ " \u00B0C" + "\n" + weatherInfo.getCurrentText());
			// ((TextView) findViewById(R.id.weather_temp)).setText(html);
		}

		/*
		 * weatherImage = (ImageView) findViewById(R.id.weather_background); try
		 * { weatherBackground = new GifAnimationDrawable(getResources()
		 * .openRawResource(getWeatherImage(weatherInfo))); } catch
		 * (NotFoundException e) { e.printStackTrace(); } catch (IOException e)
		 * { e.printStackTrace(); } weatherBackground.setOneShot(false);
		 * weatherImage.setImageDrawable(weatherBackground);
		 * weatherBackground.setVisible(true, true);
		 */
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getTheme().resolveAttribute(android.R.attr.actionBarSize,
					mTypedValue, true);
		} else {
			// getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue,
			// true);
		}

		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());

		return mActionBarHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// getActivity().getActionBar().setTitle(mSpannableString);
		// ((Button) findViewById(R.id.cal)).setAlpha(alpha);
		// if (alpha == 1.0)
		// ((Button) getActivity().findViewById(R.id.cal))
		// .setVisibility(View.VISIBLE);
		// else
		// ((Button) getActivity().findViewById(R.id.cal))
		// .setVisibility(View.INVISIBLE);
		//
		// ((Button) getActivity().findViewById(R.id.cal))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Fragment fr = new Calender();
		// FragmentTransaction transaction =
		// getActivity().getSupportFragmentManager()
		// .beginTransaction();
		// // transaction.setCustomAnimations(R.anim.slide_in,
		// // R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
		//
		// transaction.replace(R.id.container, fr);
		// transaction.addToBackStack("CALENDAR");
		// transaction.commit();
		// }
		// });
	}

	/*
	 * private void setWeatherAlpha(float alpha) { ((TextView)
	 * findViewById(R.id.weather_temp)) .setAlpha(getInverseAlpha(alpha)); if
	 * (getInverseAlpha(alpha) > 0.22f) { ((EditText) findViewById(R.id.search))
	 * .setEms(alphaToEms(getInverseAlpha(alpha))); }
	 */

	// }

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private float getInverseAlpha(float alpha) {
		return 1 - alpha;
	}

	private int alphaToEms(float alpha) {
		return (int) (15 * alpha);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// nothing

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// nothing

	}

	@Override
	public void onPageSelected(int position) {
		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = tabPagerAdapter
				.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

		currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader
				.getTranslationY()));

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// nothing

	}

	public void slideUpDown(final View view) {
		if (!isPanelShown(view)) {
			// Show the panel
			Animation bottomUp = AnimationUtils.loadAnimation(this,
					R.anim.bottom_up);

			view.startAnimation(bottomUp);
			view.setVisibility(View.VISIBLE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					R.color.dim_background));
			((FrameLayout) aq.id(R.id.framedim).getView())
					.setForeground(tintColor);
		} else {
			// Hide the Panel
			Animation bottomDown = AnimationUtils.loadAnimation(this,
					R.anim.bottom_down);

			view.startAnimation(bottomDown);
			view.setVisibility(View.GONE);
			Drawable tintColor = new ColorDrawable(getResources().getColor(
					android.R.color.transparent));
			((FrameLayout) aq.id(R.id.framedim).getView())
					.setForeground(tintColor);
			itemadded.removeAllViews();
		}
	}

	private boolean isPanelShown(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

	// ****************** Notification List Adapter*********

	public class Notify_adapter extends BaseAdapter {

		Context context;

		public Notify_adapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			aq.id(R.id.no_notification).visibility(View.GONE);
			return 10;
		}

		@Override
		public java.lang.Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub

			Holder holder = null;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.menu_drawer_right_list_item,
						arg2, false);
				holder = new Holder();
				holder.message = (TextView) view.findViewById(R.id.message);
				holder.time = (TextView) view.findViewById(R.id.time);
				Utils.RobotoRegular(context, holder.message);
				Utils.RobotoRegular(context, holder.time);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}

			return view;
		}

		class Holder {
			TextView message, time;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		Toast.makeText(MainActivity.this, "asdasdasd", Toast.LENGTH_SHORT)
				.show();
	}

}

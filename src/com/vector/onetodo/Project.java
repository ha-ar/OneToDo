/*package com.vector.onetodo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.vector.one_todo.R;
import com.vector.onetodo.Project.AddTaskPagerFragment;
import com.vector.onetodo.Project.ListClickListenerEvent;
import com.vector.onetodo.Project.ListClickListenerTask;
import com.vector.onetodo.db.gen.AssignDao;
import com.vector.onetodo.db.gen.CheckListDao;
import com.vector.onetodo.db.gen.DaoMaster;
import com.vector.onetodo.db.gen.DaoSession;
import com.vector.onetodo.db.gen.FriendsDao;
import com.vector.onetodo.db.gen.LabelDao;
import com.vector.onetodo.db.gen.LabelNameDao;
import com.vector.onetodo.db.gen.ReminderDao;
import com.vector.onetodo.db.gen.ShareDao;
import com.vector.onetodo.db.gen.ToDo;
import com.vector.onetodo.db.gen.ToDoDao;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class Project extends FragmentActivity {

	AQuery aq;
	static String label_name = null;
	int dayPosition;
	int position = 0;
	Cursor cursor;
	String[] array;
	

	private PopupWindow popupWindowTask, popupWindowEvent;
	DragSortListView listViewTask;
	ArrayAdapter<String> adapterTask;
	SQLiteDatabase db;

	public static FrameLayout layout_MainMenu;

	private DragSortListView.DropListener onDropTask = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			int mFrom = from + 1, mTo = to + 1;
			if (from != to) {
				String item = adapterTask.getItem(from);
				adapterTask.remove(item);
				adapterTask.insert(item, to);
				AddTaskFragment.swapInflatedLayouts(mFrom, mTo);
				inflateLayoutsTasks();
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.project);
		aq = new AQuery(this);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {

		
		
		// ddd.get(2).getId();

		// getActionBar().hide();
		dayPosition = getIntent().getExtras().getInt("position");

		// Initialize the ViewPager and set an adapter
		ViewPager pager = (ViewPager) aq.id(R.id.add_project_pager).getView();

		pager.setAdapter(new AddTaskPagerFragment(getSupportFragmentManager()));
		PageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator_project);
        mIndicator.setViewPager(pager);
		// Bind the tabs to the ViewPager
//		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) aq.id(
//				R.id.add_task_tabs).getView();
//		tabs.setDividerColorResource(android.R.color.transparent);
//		tabs.setIndicatorColorResource(R.color.dark_blue_color);
//		tabs.setUnderlineColorResource(android.R.color.transparent);
//		tabs.setTextSize(Utils.getPxFromDp(this, 16));
//		tabs.setIndicatorHeight(2);
//		tabs.setTextColorResource(R.color.dark_grey_color);
//		tabs.setAllCaps(false);
//		tabs.setTypeface(null, Typeface.NORMAL);
//		tabs.setShouldExpand(true);
//		tabs.setViewPager(pager);
//		tabs.setOnPageChangeListener(new PageChangeListener());

		layout_MainMenu = (FrameLayout) findViewById(R.id.main_container_project);
		layout_MainMenu.getForeground().setAlpha(0);

		final LayoutInflater inflater = (LayoutInflater) Project.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.popup_menu, null, false);

		final LayoutInflater inflater2 = (LayoutInflater) Project.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view2 = inflater2.inflate(R.layout.popup_menu_event, null,
				false);

		TextView tx = (TextView) view.findViewById(R.id.show_hid_text);
		tx.setTypeface(TypeFaces.get(this, Constants.MED_TYPEFACE));

		final String[] array = { "Priority & Label", "Location",
				"Reminder & Repeat", "Notes", "Image", "Assign & Share" };

		String[] arrayEvent = { "Priority & Label", "Location",
				"Reminder & Repeat", "Notes", "Image", "Assign & Share" };

		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(array));

		listViewTask = (DragSortListView) view.findViewById(R.id.list);
		adapterTask = new ArrayAdapter<String>(Project.this,
				R.layout.popup_menu_items, R.id.text, arrayList);

		listViewTask.setAdapter(adapterTask);
		listViewTask.setDropListener(onDropTask);
		for (int i = 0; i < array.length; i++)
			listViewTask.setItemChecked(i, true);

		popupWindowTask = new PopupWindow(view, Utils.getDpValue(250, this),
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		popupWindowTask.setBackgroundDrawable(new BitmapDrawable());
		popupWindowTask.setOutsideTouchable(true);
		popupWindowTask.setAnimationStyle(R.style.MainPopupAnimation);
		popupWindowTask.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				layout_MainMenu.getForeground().setAlpha(0);
			}
		});
		popupWindowEvent = new PopupWindow(view2, Utils.getDpValue(250, this),
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		popupWindowEvent.setBackgroundDrawable(new BitmapDrawable());
		popupWindowEvent.setOutsideTouchable(true);
		popupWindowEvent.setAnimationStyle(R.style.MainPopupAnimation);
		popupWindowEvent.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				layout_MainMenu.getForeground().setAlpha(0);
			}
		});

		DragSortController controllerTask = new DragSortController(listViewTask);
		controllerTask.setDragHandleId(R.id.drag_handle);

		listViewTask.setOnTouchListener(controllerTask);
		listViewTask.setOnItemClickListener(new ListClickListenerTask());
		aq.id(R.id.menu_dots_project).clicked(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utils.hidKeyboard(Project.this);
				if (popupWindowTask.isShowing())
					popupWindowTask.dismiss();
				else {
					layout_MainMenu.getForeground().setAlpha(150);
					popupWindowTask.showAsDropDown(aq.id(R.id.menu_dots_project)
							.getView(), 0, 10);
				}
			}
		});

		

//		aq.id(R.id.add_new).clicked(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				/*
//				 * for (int i = 0; i < array.length; i++)
//				 * Toast.makeText(Project.this, listViewEvent.isItemChecked(i) +
//				 * "", Toast.LENGTH_SHORT).show();
//				 
//				tod = tododao.loadAll();
//				if (position == 0) {
//					Add();
//				} else if (position == 1) {
//					ADD_Event();
//				}/*
//				 * for (int i = 0; i < array.length; i++) {
//				 * listViewEvent.getCheckedItemIds();
//				 * Toast.makeText(Project.this, "CLICKED " +
//				 * listViewEvent.getCheckedItemIds(),
//				 * Toast.LENGTH_SHORT).show(); }
//				 
//			}
//		});

		aq.id(R.id.save_label).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});
		
		//DONOT Show location at the moment
		aq.id(R.layout.add_task_location1).gone();
	}

	public void inflateLayoutsTasks() {
		GridLayout gridLayout = (GridLayout) findViewById(R.id.inner_container_project);
		for (int key : Project2.inflatingLayoutsSelector.keySet()) {
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.WRAP_CONTENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rowSpec = GridLayout.spec(key);
			View child = gridLayout
					.findViewById(Project2.inflatingLayoutsSelector.get(key));
			child.setLayoutParams(param);
		}
		gridLayout.invalidate();
	}


	public class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				Project.this.position = position;
				aq.id(R.id.menu_dots_event).gone();
				aq.id(R.id.menu_dots_project).visible();
			} else if (position == 1) {
				Project.this.position = position;
				aq.id(R.id.menu_dots_event).visible();
				aq.id(R.id.menu_dots_project).gone();
			}

		}

	}

	public class ListClickListenerTask implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int layoutId = Project2.inflatingLayoutsSelector.get(position + 1);
			CheckedTextView checkedTextView = (CheckedTextView) view
					.findViewById(R.id.checkbox);
			if (checkedTextView.isChecked()) {
				aq.id(layoutId).visible();
			} else {
				aq.id(layoutId).gone();
			}
		}

	}

	public class ListClickListenerEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int layoutId = AddEventFragment.inflatingLayoutsEvents
					.get(position + 1);
			CheckedTextView checkedTextView = (CheckedTextView) view
					.findViewById(R.id.checkbox);
			if (checkedTextView.isChecked()) {
				aq.id(layoutId).visible();
			} else {
				aq.id(layoutId).gone();
			}
		}

	}

	public class AddTaskPagerFragment extends FragmentPagerAdapter {

		public AddTaskPagerFragment(FragmentManager fm) {
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
				return "Add Task";
			case 1:
				return "Add Event";
			default:
				return "";// not the case

			}
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return Project1.newInstance(position, dayPosition);
			else if (position == 1)
				return Project2.newInstance(position, dayPosition);
			else
				return null;
		}
	}
	
	
	
	AQuery aq;
	int dayPosition, position = 0;

	ArrayAdapter<String> adapterTask, adapterEvent;
	SQLiteDatabase db;

	public static FrameLayout layout_MainMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		aq = new AQuery(this);
		Init();

	}

	public void Init() {

		dayPosition = getIntent().getExtras().getInt("position");

		// Initialize the ViewPager and set an adapter
		ViewPager pager = (ViewPager) aq.id(R.id.add_project_pager).getView();

		pager.setAdapter(new AddTaskPagerFragment(getSupportFragmentManager()));
		PageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator_project);
		mIndicator.setViewPager(pager);

		layout_MainMenu = (FrameLayout) findViewById(R.id.main_container_project);
		layout_MainMenu.getForeground().setAlpha(0);

		final LayoutInflater inflater = (LayoutInflater) Project.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.popup_menu, null, false);

		final LayoutInflater inflater2 = (LayoutInflater) Project.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view2 = inflater2.inflate(R.layout.popup_menu_event, null,
				false);

		TextView tx = (TextView) view.findViewById(R.id.show_hid_text);
		tx.setTypeface(TypeFaces.get(this, Constants.MED_TYPEFACE));

	}

	public void inflateLayoutsTasks() {
		GridLayout gridLayout = (GridLayout) findViewById(R.id.inner_container_project);
		for (int key : Project1.inflatingLayouts.keySet()) {
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.WRAP_CONTENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rowSpec = GridLayout.spec(key);
			View child = gridLayout.findViewById(Project1.inflatingLayouts
					.get(key));
			child.setLayoutParams(param);
		}
		gridLayout.invalidate();
	}

	public void inflateLayoutsEvents() {
		GridLayout gridLayout = (GridLayout) findViewById(R.id.inner_event_container);
		for (int key : Project2.inflatingLayoutsSelector.keySet()) {
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.height = LayoutParams.WRAP_CONTENT;
			param.width = LayoutParams.MATCH_PARENT;
			param.rowSpec = GridLayout.spec(key);
			View child = gridLayout
					.findViewById(Project2.inflatingLayoutsSelector.get(key));
			child.setLayoutParams(param);
		}
		gridLayout.invalidate();
	}

	public class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				Project.this.position = position;
				aq.id(R.id.menu_dots_event).gone();
				aq.id(R.id.menu_dots_task).visible();
			} else if (position == 1) {
				Project.this.position = position;
				aq.id(R.id.menu_dots_event).visible();
				aq.id(R.id.menu_dots_task).gone();
			}

		}

	}

	public class AddTaskPagerFragment extends FragmentPagerAdapter {

		public AddTaskPagerFragment(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 3; // just Add Task & Add Event
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Add Task";
			case 1:
				return "Add Event";
			default:
				return "";// not the case

			}
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return Project1.newInstance(position, dayPosition);
			else if (position == 1)
				return Project2.newInstance(position, dayPosition);
			else if (position == 2)
				return Project3.newInstance(position, dayPosition);
			else
				return null;
		}
	}

}
*/
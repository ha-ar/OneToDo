package com.vector.onetodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vector.model.TaskData;
import com.vector.model.TaskData.Todos;
import com.vector.onetodo.db.gen.ToDo;
import com.vector.onetodo.db.gen.ToDoDao.Properties;
import com.vector.onetodo.utils.Utils;

import de.greenrobot.dao.query.QueryBuilder;

public class TaskListFragment extends ScrollTabHolderFragment implements
		OnScrollListener {

	TextView tx;
	private ListView listView;
	public static QueryBuilder<ToDo> todayQuery, tomorrowQuery, upcommingQuery;
	// private List<Label> label;
	public static LandingAdapter todayAdapter, tomorrowAdapter,
			upComingAdapter;

	private int position;
	private View mFakeHeader;
	private static long[] Currentdate;
	private String[] ToDoName = { "Task", "Event", "Schedule", "Appoinment",
			"Project" };

	// public static ItemLazyListAdapter todaysAdapter, tomorrowsAdapter,
	// upcomingAdapter;

	public static TaskListFragment newInstance(int position) {
		TaskListFragment myFragment = new TaskListFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tasks_list, container, false);
		listView = (ListView) view.findViewById(R.id.task_list_view);
		mFakeHeader = getActivity().getLayoutInflater().inflate(
				R.layout.fake_header, listView, false);
		listView.addHeaderView(mFakeHeader);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int id = -1;
				if (position == 0) {
					id = Integer.parseInt(MainActivity.Today.get((arg2 - 1)).id);
				} else if (position == 1) {
					id = Integer.parseInt(MainActivity.Tomorrow.get((arg2 - 1)).id);
				} else if (position == 2) {
					id = Integer.parseInt(MainActivity.Upcoming.get((arg2 - 1)).id);
				}

				if (id != -1) {

					for (int i = 0; i < TaskData.getInstance().todos.size(); i++) {
						if (Integer.parseInt(TaskData.getInstance().todos
								.get(i).id) == id) {
							id = i;
							i = TaskData.getInstance().todos.size();
						}
					}

					Bundle bundle = new Bundle();
					Fragment fr = new TaskView();
					FragmentTransaction transaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					bundle.putInt("id", id);
					fr.setArguments(bundle);
					transaction.replace(R.id.container, fr);
					transaction.addToBackStack("TASKVIEW");
					transaction.commit();
				}
			}

		});

		Currentdate = new long[3];
		String date_string = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i <= 2; i++) {
			date_string = Utils.getCurrentYear(i) + "-"
					+ (Utils.getCurrentMonthDigit(i) + 1) + "-"
					+ Utils.getCurrentDayDigit(i);
			try {
				Date mDate = sdf.parse(date_string);
				Currentdate[i] = mDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		position = getArguments().getInt("position");
		setadapter(getActivity(), position);

		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getActivity(), "" + arg2, Toast.LENGTH_LONG)
		// .show();
		// }
		// });

	}

	/*
	 * public class Holder { TextView title, location, reminderDate; }
	 */

	public class LandingAdapter extends BaseAdapter {

		Context context;
		int posit;
		List<Todos> listToShow;

		public LandingAdapter(Context context, List<Todos> whichList,
				int poisiton) {

			tx = (TextView) getActivity().findViewById(R.id.pagertext);
			this.context = context;
			this.listToShow = whichList;
			this.posit = position;
			if (listToShow.size() > 0) {
				tx.setVisibility(View.GONE);
			} else {

				tx.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public int getCount() {
			return listToShow.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int ID) {
			return ID;
		}

		@Override
		public View getView(int position, View view1, ViewGroup parent) {
			View view = view1;
			Holder holder = null;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_item, parent, false);
				holder = new Holder();
				holder.icon = (TextView) view.findViewById(R.id.list_icon);
				holder.title = (TextView) view.findViewById(R.id.list_title);
				holder.location = (TextView) view
						.findViewById(R.id.list_location);
				holder.time = (TextView) view.findViewById(R.id.list_time);
				holder.time1 = (TextView) view.findViewById(R.id.list_time1);
				holder.type = (TextView) view.findViewById(R.id.type);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}
			// Iconify.setIcon(holder.icon, IconValue.fa_exclamation);
			// holder.type.setText(ToDoName[Integer.parseInt(TaskData.getInstance().todos.get(position).todo_type_id)-1]);
			// holder.icon.setTextSize(16);
			holder.title.setText(listToShow.get(position).title);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MM-yyyy hh:mm");
			Calendar calendar = Calendar.getInstance();
			// calendar.setTimeInMillis(listToShow.get(position).getStart_date());
			// holder.time.setText(formatter.format(calendar.getTime()));

			/*
			 * holder.time.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK,
			 * Calendar.SHORT,
			 * Locale.US)+" "+calendar.get(Calendar.DAY_OF_MONTH)
			 * +" "+calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
			 * Locale.US)+" "+calendar.get(Calendar.YEAR));
			 * holder.time1.setText(
			 * String.format("%02d",calendar.get(Calendar.HOUR))+":"+
			 * String.format("%02d",calendar.get(Calendar.MINUTE))+" "+
			 * calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT,
			 * Locale.US));
			 */
			String strDate = listToShow.get(position).start_date;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = dateFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.setTime(date);
			if (posit == 2) {
				holder.time.setText(calendar.getDisplayName(
						Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)
						+ " "
						+ calendar.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ calendar.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US)
						+ " "
						+ calendar.get(Calendar.YEAR));
			}
			holder.time1.setText(String.format("%02d",
					calendar.get(Calendar.HOUR))
					+ ":"
					+ String.format("%02d", calendar.get(Calendar.MINUTE))
					+ " "
					+ calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT,
							Locale.US));
			if (listToShow.get(position).todo_attachment != null) {
				holder.location.setText(listToShow.get(position).location);
			}
			return view;
		}

	}

	class Holder {
		TextView title, location, time, icon, time1, type;
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && listView.getFirstVisiblePosition() >= 1) {
			return;
		}

		listView.setSelectionFromTop(1, scrollHeight);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount, position);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// nothing

	}

	public static void todayQuery() {
		todayQuery = MainActivity.tododao.queryBuilder().where(
				Properties.Start_date.between(Currentdate[0], Currentdate[1]));

	}

	public static void tomorrowQuery() {
		tomorrowQuery = MainActivity.tododao.queryBuilder().where(
				Properties.Start_date.between(Currentdate[1], Currentdate[2]));

	}

	public static void upComingQuery() {
		upcommingQuery = MainActivity.tododao.queryBuilder().where(
				Properties.Start_date.gt(Currentdate[2]));
	}

	private void setadapter(Context context, int position) {

		switch (position) {
		case 0:

			/*
			 * //todayQuery(); All=MainActivity.Today;
			 */

			todayAdapter = new LandingAdapter(getActivity(),
					MainActivity.Today, 0);
			todayAdapter.notifyDataSetInvalidated();
			listView.setAdapter(todayAdapter);

			break;
		case 1:
			// tomorrowQuery();

			tomorrowAdapter = new LandingAdapter(getActivity(),
					MainActivity.Tomorrow, 1);
			tomorrowAdapter.notifyDataSetInvalidated();
			listView.setAdapter(tomorrowAdapter);
			break;
		case 2:
			// upComingQuery();

			upComingAdapter = new LandingAdapter(getActivity(),
					MainActivity.Upcoming, 2);
			upComingAdapter.notifyDataSetInvalidated();
			listView.setAdapter(upComingAdapter);
			break;
		default:
			// nothing
			break;
		}

	}
}
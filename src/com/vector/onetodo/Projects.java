package com.vector.onetodo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;
import com.vector.onetodo.utils.Utils;

public class Projects extends Fragment implements ProjectsScrollHolder {

	AQuery aq;
	TextView title;
	private ViewPager pager;
	private TabPagerAdapterpro tabPagerAdapter;

	private ActionBar actionBar;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		actionBar = ((ActionBarActivity) activity).getSupportActionBar();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.projects, container, false);
		aq = new AQuery(getActivity(), view);
		actionBar.setTitle("Projects");
		setHasOptionsMenu(true);
		return view;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.clear();
		inflater.inflate(R.menu.main, menu);

		SearchManager manager = (SearchManager) getActivity().getSystemService(
				Context.SEARCH_SERVICE);
		SearchView search = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		search.setSearchableInfo(manager.getSearchableInfo(getActivity()
				.getComponentName()));
		search.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String query) {
				// loadHistory(query);
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}

		});

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		aq.id(R.id.organizer).text(
				Html.fromHtml("<i><small><font color=\"#c5c5c5\">"
						+ "Competitor ID: " + "</font></small></i>"
						+ "<font color=\"#47a842\">" + "compID" + "</font>"));

		pager = (ViewPager) getActivity().findViewById(R.id.pager_projects);

		tabPagerAdapter = new TabPagerAdapterpro(getChildFragmentManager());
		tabPagerAdapter
				.setTabHolderScrollingContent(new ProjectsScrollHolder() {

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount, int pagePosition) {
						// TODO Auto-generated method stub

					}

					@Override
					public void adjustScroll(int scrollHeight) {
						// TODO Auto-generated method stub

					}
				});

		pager.setAdapter(tabPagerAdapter);

		// Bind the tabs to the ViewPager
		final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) aq.id(
				R.id.tabs_projects).getView();
		/*
		 * android.widget.LinearLayout.LayoutParams defaultTabLayoutParams = new
		 * LinearLayout.LayoutParams(size.x/2, LayoutParams.MATCH_PARENT);
		 * tabs.setLayoutParams(defaultTabLayoutParams);
		 */
		tabs.setShouldExpand(true);
		tabs.setDividerColorResource(android.R.color.transparent);
		// tabs.setIndicatorColorResource(R.color.graytab);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(getActivity(), 14));
		tabs.setIndicatorHeight(Utils.getPxFromDp(getActivity(), 3));

		tabs.setMinimumWidth(Utils.getPxFromDp(getActivity(), 200));

		tabs.setIndicatorColor(Color.parseColor("#ffffff"));
		tabs.setTextColor(Color.parseColor("#ffffff"));
		tabs.setSmoothScrollingEnabled(true);
		// tabs.setTextColorResource(R.color.graytab);
		tabs.setAllCaps(true);
		tabs.setTypeface(null, Typeface.NORMAL);
		// tabs.setOnPageChangeListener(new
		// OnPageChangeListener(getActivity()));

		tabs.setViewPager(pager);
		tabPagerAdapter.notifyDataSetChanged();
	}

	public class TabPagerAdapterpro extends FragmentPagerAdapter {

		private SparseArrayCompat<ProjectsScrollHolder> mScrollTabHolders;
		private ProjectsScrollHolder mListener;

		public TabPagerAdapterpro(FragmentManager fm) {
			super(fm);
			mScrollTabHolders = new SparseArrayCompat<ProjectsScrollHolder>();
		}

		public void setTabHolderScrollingContent(ProjectsScrollHolder listener) {
			mListener = listener;
		}

		@Override
		public int getCount() {
			return 2;
			// return 3; // no. of tabs are Today, Tomorrow & Upcoming
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "MY PROJECTS";
			case 1:
				return "SHARED PROJECTS";
			default:
				return "";// not the case

			}

		}

		public SparseArrayCompat<ProjectsScrollHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

		@Override
		public Fragment getItem(int position) {
			ProjectsTabHolder fragment = (ProjectsTabHolder) ProjectsListFragment
					.newInstance(position);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}
			return fragment;
		}

	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		// TODO Auto-generated method stub

	}

}

package com.vector.onetodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;
import com.vector.onetodo.utils.Utils;

public class AddTaskAssign extends Fragment {

	AlertDialog dialog,Invite_selection, Invite;
	AQuery aqemail, aq, aq_menu,aq_selection,aq_invite;
	int check = 0, position = 0;
	private TabPagerAdapter tabPagerAdapter;
	LinearLayout last = null;
	private ViewPager pager;
	private PopupWindow popupWindowTask;

	/*
	 * List<String> name; contact list_adapter;
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setRetainInstance(true);
		View view = inflater
				.inflate(R.layout.add_task_assign, container, false);
		// name = new ArrayList<String>();
		aq = new AQuery(getActivity(), view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		final View viewMenu = getActivity().getLayoutInflater().inflate(
				R.layout.landing_menu, null, false);
		aq_menu = new AQuery(getActivity(), viewMenu);
		aq_menu.id(R.id.menu_item1).text("Invite contacts");
		aq_menu.id(R.id.menu_item2).text("Search");

		
		//******************** Dialogs
		View selection = getActivity().getLayoutInflater().inflate(
				R.layout.add_task_assign_email, null, false);
		aq_selection = new AQuery(selection);
		AlertDialog.Builder builderLabel = new AlertDialog.Builder(
				getActivity());
		builderLabel.setView(selection);
		Invite_selection = builderLabel.create();
		
		View invite = getActivity().getLayoutInflater().inflate(
				R.layout.add_task_invite, null, false);
		aq_invite = new AQuery(invite);
		AlertDialog.Builder builderLabel1 = new AlertDialog.Builder(
				getActivity());
		builderLabel1.setView(invite);
		Invite = builderLabel1.create();
		aq_invite.id(R.id.cancel).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Invite.dismiss();
			}
		});
		
		aq_selection.id(R.id.email).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Invite.show();
				Invite_selection.dismiss();
			}
		});
		
		//******************** POPUP WINDOW
		popupWindowTask = new PopupWindow(viewMenu, Utils.getDpValue(200,
				getActivity()), WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindowTask.setBackgroundDrawable(getResources().getDrawable(
				android.R.drawable.dialog_holo_light_frame));
		popupWindowTask.setOutsideTouchable(true);
		// popupWindowTask.setAnimationStyle(R.style.Animation);

		aq.id(R.id.assign_menu).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (popupWindowTask.isShowing()) {
					popupWindowTask.dismiss();

				} else {
					// layout_MainMenu.getForeground().setAlpha(150);
					popupWindowTask.showAsDropDown(aq.id(R.id.assign_menu)
							.getView(), 5, 10);
					aq.id(R.id.assign_menu).image(R.drawable.ic_show_black);
				}
			}
		});

		aq_menu.id(R.id.menu_item1).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindowTask.dismiss();
				Invite_selection.show();

			}
		});
		
		popupWindowTask.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				aq.id(R.id.assign_menu).image(R.drawable.ic_show_white);
			}
		});
		

		aq.id(R.id.assign_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) { // TODO Auto-generated
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		
		//****************** PAGER BINDE WITH TAB
		pager = (ViewPager) getActivity().findViewById(R.id.assign_pager);
		tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
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
				R.id.assing_tabs).getView();

		tabs.setShouldExpand(false);
		tabs.setDividerColorResource(android.R.color.transparent);
		tabs.setUnderlineColorResource(android.R.color.transparent);
		tabs.setTextSize(Utils.getPxFromDp(getActivity(), (int) 17.78));
		tabs.setIndicatorHeight(Utils.getPxFromDp(getActivity(), 3));
		tabs.setMinimumWidth(Utils.getPxFromDp(getActivity(), 100));
		tabs.setIndicatorColor(Color.parseColor("#ffffff"));
		tabs.setTextColor(Color.parseColor("#ffffff"));
		tabs.setSmoothScrollingEnabled(true);
		tabs.setShouldExpand(false);
		tabs.setAllCaps(false);
		tabs.setTypeface(null, Typeface.NORMAL);
		tabs.setViewPager(pager);
		tabPagerAdapter.notifyDataSetChanged();

		/*
		 * // TODO Auto-generated method stub super.onViewCreated(view,
		 * savedInstanceState);
		 * 
		 * list_adapter = new contact(getActivity());
		 * 
		 * name.add("Sulman"); name.add("Ali Shujat"); name.add("Qasim sandhu");
		 * name.add("Markel seni");
		 * 
		 * if (check == 0) { aq.id(R.id.contact_layout).visibility(View.GONE);
		 * aq.id(R.id.imageView1).getImageView()
		 * .setImageResource(R.drawable.acceptfinal);
		 * aq.id(R.id.contact_list_layout).visibility(View.VISIBLE);
		 * aq.id(R.id.imageView1).getImageView().setAlpha((float) 0.3); contact
		 * adapter = new contact(getActivity());
		 * aq.id(R.id.listView1).getListView().setAdapter(adapter); }
		 * 
		 * adapter = new ArrayAdapter(getActivity(),
		 * R.layout.add_task_assign_spinner_item, item);
		 * 
		 * aq.id(R.id.textView1).text(AddTaskFragment.title);
		 * 
		 * LayoutInflater inflater = getActivity().getLayoutInflater(); View
		 * attachment = inflater.inflate(R.layout.add_task_assign_email, null,
		 * false);
		 * 
		 * if(myEditText.requestFocus()) {
		 * getActivity().getWindow().setSoftInputMode
		 * (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); }
		 * 
		 * 
		 * aqemail = new AQuery(attachment); AlertDialog.Builder attach_builder
		 * = new AlertDialog.Builder( getActivity());
		 * attach_builder.setView(attachment); dialog = attach_builder.create();
		 * 
		 * EditText edit = (EditText) aqemail.id(R.id.add_assign_task_email)
		 * .getView(); InputMethodManager imm = (InputMethodManager)
		 * getActivity() .getSystemService(Context.INPUT_METHOD_SERVICE);
		 * imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
		 * aq.id(R.id.add_contact).clicked(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub dialog.show(); } }); aq.id(R.id.imageView1).clicked(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub float f = View.ALPHA.get(arg0); if (f == 1) {
		 * 
		 * LayoutInflater inflate = getActivity().getLayoutInflater(); View vie
		 * = inflate.inflate(R.layout.add_task_title, null, false);
		 * AddTaskFragment
		 * .aqa.id(R.id.assign_task_button_task).getImageView().setImageResource
		 * (R.drawable.assign_blue); //TextView txt=(TextView)
		 * vie.findViewById(R.id.task_assign);
		 * 
		 * //AddTaskFragment.img.setImageResource(R.drawable.assign_blue);
		 * AddTaskFragment.aqa.id(R.id.task_assign).text(name.get(position));
		 * //AddTaskFragment
		 * .aqa.id(R.id.imageView12).getImageView().setImageResource
		 * (R.drawable.next_item_blue);
		 * ///txt.setTextColor(Color.parseColor("#33B5E5"));
		 * 
		 * getFragmentManager().popBackStack();
		 * 
		 * } else {
		 * 
		 * dialog.show(); } } });
		 * 
		 * aq.id(R.id.imageView3).clicked(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub getFragmentManager().popBackStack(); } });
		 * 
		 * aqemail.id(R.id.edit_ok).clicked(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub dialog.dismiss();
		 * aq.id(R.id.contact_layout).visibility(View.GONE);
		 * aq.id(R.id.imageView1).getImageView()
		 * .setImageResource(R.drawable.acceptfinal);
		 * aq.id(R.id.contact_list_layout).visibility(View.VISIBLE);
		 * aq.id(R.id.imageView1).getImageView().setAlpha((float) 0.3);
		 * aq.id(R.id.listView1).getListView().setAdapter(list_adapter);
		 * 
		 * } }); aqemail.id(R.id.edit_cencel).clicked(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub dialog.dismiss();
		 * aqemail.id(R.id.add_assign_task_email).text(""); } });
		 * aq.id(R.id.listView1).getListView() .setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View
		 * container, int arg2, long arg3) { // TODO Auto-generated method stub
		 * position = arg2; LinearLayout linearLayoutParent = (LinearLayout)
		 * container;
		 * 
		 * // Getting the inner Linear Layout // LinearLayout linearLayoutChild
		 * = (LinearLayout ) // linearLayoutParent.getChildAt(1); if (last !=
		 * null) { // Getting the Country TextView ImageView tvCount1 =
		 * (ImageView) last.getChildAt(2); tvCount1.setVisibility(View.GONE); }
		 * 
		 * last = linearLayoutParent;
		 * 
		 * // Getting the Country TextView ImageView tvCountry = (ImageView)
		 * linearLayoutParent .getChildAt(2);
		 * 
		 * // ImageView img=(ImageView) // arg1.findViewById(R.id.assign_image);
		 * tvCountry.setVisibility(View.VISIBLE);
		 * 
		 * aq.id(R.id.imageView1).getImageView() .setAlpha((float) 1); } });
		 */}

	/*
	 * public class contact extends BaseAdapter {
	 * 
	 * Context context;
	 * 
	 * public contact(Context context) { this.context = context; }
	 * 
	 * @Override public int getCount() { // TODO Auto-generated method stub
	 * return name.size(); }
	 * 
	 * @Override public java.lang.Object getItem(int arg0) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public long getItemId(int position) { // TODO Auto-generated
	 * method stub return position; }
	 * 
	 * public class Holder { ImageView image; Spinner icon; TextView text; }
	 * 
	 * @Override public View getView(int Position, View arg1, ViewGroup arg2) {
	 * // TODO Auto-generated method stub View row = arg1; Holder holder = null;
	 * if (row == null) { LayoutInflater inflater = ((Activity) context)
	 * .getLayoutInflater(); row =
	 * inflater.inflate(R.layout.add_task_assign_item, arg2, false); holder =
	 * new Holder(); holder.image = (ImageView)
	 * row.findViewById(R.id.assign_image); holder.icon = (Spinner)
	 * row.findViewById(R.id.item_spinner); holder.text = (TextView)
	 * row.findViewById(R.id.textView1); row.setTag(holder); } else { holder =
	 * (Holder) row.getTag(); } //
	 * holder.image.setBackgroundResource(R.drawable.calendar_blue);
	 * holder.text.setText(aqemail.id(R.id.add_assign_task_email)
	 * .getText().toString()); holder.text.setText(name.get(Position));
	 * holder.icon.setAdapter(adapter); holder.icon.setSelection(1);
	 * holder.icon.setOnItemSelectedListener(new OnItemSelectedListener() {
	 * 
	 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) {
	 * 
	 * // TODO Auto-generated method stub if (((TextView)
	 * arg1).getText().toString() .equals("Resend invite")) {
	 * aqemail.id(R.id.add_assign_task_email).text(""); dialog.show(); }
	 * ((TextView) arg1).setText(""); ((TextView) arg1).setBackgroundColor(Color
	 * .parseColor("#00000000")); }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> arg0) { // TODO
	 * Auto-generated method stub
	 * 
	 * } }); return row; } }
	 */

	public class TabPagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ProjectsScrollHolder> mScrollTabHolders;
		private ProjectsScrollHolder mListener;

		public TabPagerAdapter(FragmentManager fm) {
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
				return "RECENT";
			case 1:
				return "CONTACTS";
			default:
				return "";// not the case
			}

		}

		public SparseArrayCompat<ProjectsScrollHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

		@Override
		public Fragment getItem(int position) {
			ProjectsTabHolder fragment = (ProjectsTabHolder) AssignListFragment
					.newInstance(position);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}
			return fragment;
		}
	}
}
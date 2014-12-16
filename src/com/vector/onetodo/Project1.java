/*package com.vector.onetodo;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.vector.one_todo.R;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;

public class Project1 extends Fragment {

	public static AQuery aq, popupAQ, aqloc;

	static List<java.lang.Object> names;

	static String checkedId2 = null;

	static LinearLayout lll;

	static ContactsCompletionView completionAssignView, completionShareView;
	static int currentHours, currentMin, currentDayDigit, currentYear,
			currentMonDigit;

	public static HashMap<Integer, Integer> inflatingLayouts = new HashMap<Integer, Integer>();

	EditText label_field = null;

	protected static final int RESULT_CODE = 123;

	public static final int RESULT_GALLERY = 0;

	public static final int PICK_CONTACT = 2;

	public static View allView;

	public static Activity act;

	public static Project1 newInstance(int position, int dayPosition) {
		Project1 myFragment = new Project1();
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
		inflatingLayouts.put(0, R.layout.project_title);
		inflatingLayouts.put(1, R.layout.project_duration);
		inflatingLayouts.put(2, R.layout.project_task);
		inflatingLayouts.put(3, R.layout.project_note);

		inflateLayouts();

		aq.id(R.id.contacts_name).typeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE));

		aq.id(R.id.name).typeface(
				TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE));

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
	
}*/
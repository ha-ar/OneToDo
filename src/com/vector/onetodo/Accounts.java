package com.vector.onetodo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.vector.onetodo.utils.Utils;

public class Accounts extends Fragment {

	AQuery aq;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.account, container, false);

		aq = new AQuery(getActivity(), view);
		setFont();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		aq.id(R.id.account_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
	}

	public void setFont() {
		Utils.RobotoRegular(getActivity(), aq.id(R.id.account).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.accounts).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.email).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.facebook).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.phonenumber)
				.getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.mynumber).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.personal).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.name).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.display).getTextView());

		Utils.RobotoMedium(getActivity(), aq.id(R.id.email1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.facebook1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.mynumber1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.name1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.display1).getTextView());
	}

}

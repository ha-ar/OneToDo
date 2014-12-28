package com.vector.onetodo;

import uk.me.lewisdeane.ldialogs.BaseDialog.Alignment;
import uk.me.lewisdeane.ldialogs.CustomDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.vector.onetodo.utils.Utils;

public class Accounts extends Fragment implements OnClickListener {

	AQuery aq;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.account, container, false);
		aq = new AQuery(getActivity(), view);
		setFont();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		aq.id(R.id.account_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getFragmentManager().popBackStack();
			}
		});

		aq.id(R.id.go_pro).clicked(this);
		aq.id(R.id.verify_number).clicked(this);
		aq.id(R.id.email_account).clicked(this);
		aq.id(R.id.name_layout).clicked(this);
		aq.id(R.id.display_pic).clicked(this);
		aq.id(R.id.info_gopro).clicked(this);
		aq.id(R.id.info_number).clicked(this);
		aq.id(R.id.info_email).clicked(this);

	}

	public void setFont() {
		Utils.RobotoRegular(getActivity(), aq.id(R.id.account).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.accounts).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.email).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.mynumber).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.personal).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.name).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.display).getTextView());

		Utils.RobotoMedium(getActivity(), aq.id(R.id.email1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.mynumber1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.name1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.display1).getTextView());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_pro:
			showGoProDialog();
			break;
		case R.id.verify_number:
			showVerifyNumberDialog();
			break;
		case R.id.email_account:
			showEmailAccountDialog();
			break;
		case R.id.name_layout:
			showNameDialog();
			break;
		case R.id.display_pic:
			break;
		case R.id.info_gopro:
			showInfoGoProDialog();
			break;
		case R.id.info_email:
			showInfoEmailDialog();
			break;
		case R.id.info_number:
			showInfoNumberDialog();
			break;
		default:
			break;
		}

	}

	private void showInfoNumberDialog() {
		CustomDialog.Builder builder = new Builder(getActivity(),
				"Phone number", "OK");
		builder.rightToLeft(false);
		builder.titleAlignment(Alignment.LEFT);
		builder.contentAlignment(Alignment.LEFT);
		builder.positiveColorRes(R.color.active);
		builder.content("This number has been register with your account. If you change you number, all your will be available at new number.");
		builder.negativeText("CHANGE NUMBER");
		CustomDialog dialog = builder.build();
		dialog.show();

	}

	private void showInfoEmailDialog() {
		CustomDialog.Builder builder = new Builder(getActivity(), "Email", "OK");
		builder.rightToLeft(false);
		builder.titleAlignment(Alignment.LEFT);
		builder.contentAlignment(Alignment.LEFT);
		builder.positiveColorRes(R.color.active);
		builder.content("You can get reminder alerts on this email. You can change this email anytime.");
		CustomDialog dialog = builder.build();
		dialog.show();
	}

	private void showInfoGoProDialog() {
		CustomDialog.Builder builder = new Builder(getActivity(), "ONEtodo",
				"Buy PRO");
		builder.rightToLeft(false);
		builder.titleAlignment(Alignment.LEFT);
		builder.contentAlignment(Alignment.LEFT);
		builder.positiveColorRes(R.color.active);
		builder.content("You are using a trail version with all the pro-features. You can carry on using basic feature free of cost when trial ends. You can buy pro version anytime.");
		builder.negativeText("OK");
		CustomDialog dialog = builder.build();
		dialog.show();
	}

	private void showNameDialog() {
		// TODO Auto-generated method stub

	}

	private void showEmailAccountDialog() {
		// TODO Auto-generated method stub

	}

	private void showVerifyNumberDialog() {
		// TODO Auto-generated method stub

	}

	private void showGoProDialog() {

	}

}

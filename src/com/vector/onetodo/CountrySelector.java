package com.vector.onetodo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.Utils;

public class CountrySelector extends Fragment {

	// http://api.heuristix.net/one_todo/v1/user/addContacts
	/*
	 * $data = array( 'user_id' => '6', 'contacts' => array('+447589567876',
	 * '+447589567897', '+447589517897') );
	 */
	// private Button loginButton;
	AQuery aq;
	TextView skip;
	HttpClient client;
	HttpPost post;
	List<NameValuePair> pairs, pair;
	HttpResponse response = null;
	Boolean message;
	AlertDialog alert;
	TextView confirm, save;
	int position = 0;
	public static View view;
	InputMethodManager imm;

	// ************** Phone COntacts

	String phoneNumber = null;
	Cursor cursor;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (SplashScreen.country != null) {
			aq.id(R.id.country).getEditText().setText(SplashScreen.code);
			aq.id(R.id.country).getEditText()
					.setSelection(SplashScreen.code.length());
			aq.id(R.id.spinner1).text(SplashScreen.country);
			aq.id(R.id.country).getEditText().setEnabled(true);
			aq.id(R.id.country).getEditText().requestFocus();
			if (imm != null) {
				imm.showSoftInput(aq.id(R.id.country).getEditText(),
						InputMethodManager.SHOW_IMPLICIT);
			}
			position = 1;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.about, container, false);
		aq = new AQuery(getActivity(), view);
		imm = (InputMethodManager) getActivity().getSystemService(
				getActivity().INPUT_METHOD_SERVICE);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// ******* Phone contact , name list

		Constants.Name = new ArrayList<String>();
		Constants.Contact = new ArrayList<String>();
		new Phone_contact().execute();

		String html = "ONE" + "<br />" + "todo";
		aq.id(R.id.title).text(Html.fromHtml(html));

		skip = (TextView) getActivity().findViewById(R.id.loginSkip);

		View vie = getActivity().getLayoutInflater().inflate(R.layout.skip,
				null, false);
		AlertDialog.Builder builderLabel = new AlertDialog.Builder(
				getActivity());
		builderLabel.setView(vie);
		alert = builderLabel.create();
		confirm = (TextView) vie.findViewById(R.id.skip_confirm);
		save = (TextView) vie.findViewById(R.id.skip_save);

		aq.id(R.id.spinner1).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Fragment fr = new Country();
				FragmentTransaction trans = getFragmentManager()
						.beginTransaction();
				trans.replace(R.id.container, fr);
				trans.addToBackStack("COUNTRY");
				trans.commit();
			}
		});

		aq.id(R.id.countryok).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!(aq.id(R.id.country).getText().length() < 4
						|| aq.id(R.id.country).getText().equals("") || position == 0)) {

					if (Constants.RegId != null) {
						AddRegister();
					} else {
						Toast.makeText(getActivity(), "Try Again...",
								Toast.LENGTH_LONG).show();
						SplashScreen.registerInBackground(getActivity());
					}
				} else {
					Toast.makeText(getActivity(),
							"Enter correct number and select country",
							Toast.LENGTH_LONG).show();
					aq.id(R.id.country).getEditText().requestFocus();
				}
			}
		});
		skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alert.show();
			}
		});
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.dismiss();
				showUserDetailsActivity();
			}
		});

	}

	private void showUserDetailsActivity() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), MainActivity.class);
		getActivity().startActivity(intent);
		getActivity().finish();
		getActivity()
				.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
	}

	public void AddRegister() {
		pairs = new ArrayList<NameValuePair>();
		Random r = new Random();
		int i1 = r.nextInt(100000 - 1 + 1) + 1;
		pairs.add(new BasicNameValuePair("email", "example" + i1 + "@gmail.com"));
		pairs.add(new BasicNameValuePair("password", "eyspasd"));
		pairs.add(new BasicNameValuePair("registration_type", Constants.RegId));

		pairs.add(new BasicNameValuePair("registration_type_id", "3"));

		pairs.add(new BasicNameValuePair("device_type_id", Secure.getString(
				getActivity().getContentResolver(), Secure.ANDROID_ID) + ""));

		pairs.add(new BasicNameValuePair("device_type", Build.MODEL + ""));
		pairs.add(new BasicNameValuePair("mobile_no", aq.id(R.id.country)
				.getText().toString()
				+ ""));
		pairs.add(new BasicNameValuePair("country", SplashScreen.country + ""));
		pairs.add(new BasicNameValuePair("date_created", Utils
				.getCurrentYear(1)
				+ "-"
				+ Utils.getCurrentMonthDigit(1)
				+ "-"
				+ Utils.getCurrentDayDigit(1)
				+ " "
				+ Utils.getCurrentHours()
				+ ":" + Utils.getCurrentMins() + ":00"));

		HttpEntity entity = null;

		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Map<String, HttpEntity> param = new HashMap<String, HttpEntity>();
		param.put(AQuery.POST_ENTITY, entity);

		aq.ajax("http://api.heuristix.net/one_todo/v1/user/register", param,
				JSONObject.class, new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						// dismis();

						int id = -1;
						try {

							JSONObject obj1 = new JSONObject(json.toString());
							message = obj1.getBoolean("error");
							id = obj1.getInt("result");

						} catch (Exception e) {
						}
						if (id != -1) {
							Constants.user_id = id;
							SplashScreen.editor.putInt("userid", id);
							SplashScreen.editor.commit();
						}
						Log.v("Response", json.toString() + message + "");
						if (message == false) {
							showUserDetailsActivity();
						}
					}
				});

		client = new DefaultHttpClient();
		post = new HttpPost(
				"http://api.heuristix.net/one_todo/v1/user/addContacts");
		pair = new ArrayList<NameValuePair>();
		pair.add(new BasicNameValuePair("user_id", "3223"));
		for (int i = 0; i < Constants.Contact.size(); i++) {
			pair.add(new BasicNameValuePair("contacts[" + i + "]",
					Constants.Contact.get(i)));
		}

		try {
			post.setEntity(new UrlEncodedFormEntity(pair));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			response = client.execute(post);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String temp = null;
		try {
			temp = EntityUtils.toString(response.getEntity());
		} catch (org.apache.http.ParseException | IOException e) {
			e.printStackTrace();
		}
		Log.v("Response post ", temp + " new");

	}

	public class Phone_contact extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (Constants.Name.size() > 0)
				Toast.makeText(getActivity(), Constants.Name.get(0),
						Toast.LENGTH_LONG);
			else
				Toast.makeText(getActivity(), "Not", Toast.LENGTH_LONG);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			cursor = getActivity()
					.getContentResolver()
					.query(ContactsContract.Contacts.CONTENT_URI,
							null,
							ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1",
							null,
							"UPPER(" + ContactsContract.Contacts.DISPLAY_NAME
									+ ") ASC");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					int hasPhoneNumber = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
					if (hasPhoneNumber > 0) {
						Constants.Name
								.add(cursor.getString(cursor
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
						// Query and loop for every phone number of the contact
						Cursor phoneCursor = getActivity()
								.getContentResolver()

								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?",
										new String[] { cursor.getString(cursor
												.getColumnIndex(ContactsContract.Contacts._ID)) },
										null);

						phoneCursor.moveToNext();
						phoneNumber = phoneCursor
								.getString(phoneCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Constants.Contact.add(phoneNumber);

						phoneCursor.close();
					}
				}
			}

			return null;
		}
	}

}

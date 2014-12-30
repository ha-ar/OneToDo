package com.vector.onetodo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

	// private Button loginButton;
	AQuery aq;
	TextView skip;
	HttpClient client;
	HttpPost post;
	List<NameValuePair> pairs;
	HttpResponse response = null;
	Boolean message;
	private final int SPLASH_DISPLAY_TIME = 200;
	private Dialog progressDialog;
	AlertDialog alert;
	TextView confirm, save;
	int position = 0;
	public static View view;
	InputMethodManager imm;

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
				imm.showSoftInput(aq.id(R.id.country).getEditText(), InputMethodManager.SHOW_IMPLICIT);
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
		imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		/*InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}*/

		String html = "ONE" + "<br />" + "todo";
		aq.id(R.id.title).text(Html.fromHtml(html));

		/*
		 * about.this.position = position;
		 * aq.id(R.id.country).getEditText().requestFocus(); if
		 * (code.get(position).toString().equals("Select country")) {
		 * aq.id(R.id.item).getTextView()
		 * .setTextColor(Color.parseColor("#CECECE"));
		 * 
		 * } else { aq.id(R.id.item).getTextView()
		 * .setTextColor(Color.parseColor("#FFFFFF")); } }
		 */

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
				//imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
				// deviceid();
				if (!(aq.id(R.id.country).getText().length() < 4
						|| aq.id(R.id.country).getText().equals("") || position == 0)) {
					/*
					 * asyn = new add(); asyn.execute();
					 */
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
		/*
		 * loginButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { onLoginButtonClicked(); } });
		 */

		// Check if there");    code.add("+a currently logged in user
		// and they are linked to a Facebook account.
		/*
		 * ParseUser currentUser = ParseUser.getCurrentUser(); if ((currentUser
		 * != null) && ParseFacebookUtils.isLinked(currentUser)) { // Go to the
		 * user info activity new Handler().postDelayed(new Runnable() { public
		 * void run() { showUserDetailsActivity(); } }, SPLASH_DISPLAY_TIME);
		 * 
		 * }
		 */
	}

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { super.onActivityResult(requestCode, resultCode, data);
	 * ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data); }
	 */

	/*
	 * private void onLoginButtonClicked() { SplashScreen.this.progressDialog =
	 * ProgressDialog.show( SplashScreen.this, "", "Logging in...", true);
	 * List<String> permissions1 = Arrays.asList("public_profile");
	 * ParseFacebookUtils.logIn(permissions1, this, new LogInCallback() {
	 * 
	 * @Override public void done(ParseUser user, ParseException err) {
	 * SplashScreen.this.progressDialog.dismiss(); if (user == null) { } else if
	 * (user.isNew()) { showUserDetailsActivity(); } else {
	 * showUserDetailsActivity(); } } }); }
	 */

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

	/*
	 * public void deviceid() {
	 * 
	 * String android_id = Secure.getString( getActivity().getContentResolver(),
	 * Secure.ANDROID_ID);
	 * 
	 * String manufacturer = Build.MANUFACTURER; String brand = Build.BRAND;
	 * String product = Build.PRODUCT; String model = Build.MODEL;
	 * Log.v("manufacturer ///brand///product///model   ", manufacturer + "///"
	 * + brand + " /// " + product + "///" + model + "" + android_id);
	 * 
	 * Log.v("Hello ", Utils.getCurrentYear(1) + "-" +
	 * Utils.getCurrentMonthDigit(1) + "-" + Utils.getCurrentDayDigit(1) + " " +
	 * Utils.getCurrentHours() + ":" + Utils.getCurrentMins() + ":00"); }
	 */

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
		// UrlEncodedFormEntity
		// entity=null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			// entity.setContentType("application/json");
		} catch (UnsupportedEncodingException e) {
			// TODO
			// Auto-generated
			// catch block
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

		/*
		 * try { post.setEntity(new UrlEncodedFormEntity(pairs)); } catch
		 * (UnsupportedEncodingException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); }
		 * 
		 * try { response = client.execute(post); } catch
		 * (ClientProtocolException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); asyn.cancel(true); } catch (IOException e1) {
		 * // TODO Auto-generated catch block e1.printStackTrace();
		 * asyn.cancel(true); }
		 */

	}
	/*
	 * public class add extends AsyncTask<String, Integer, Void> {
	 * 
	 * 
	 * @Override protected Void doInBackground(String... params) { // TODO
	 * Auto-generated method stub
	 * 
	 * HttpEntity entity = null; // UrlEncodedFormEntity // entity=null; try {
	 * entity = new UrlEncodedFormEntity(pairs, "UTF-8"); //
	 * entity.setContentType("application/json"); } catch
	 * (UnsupportedEncodingException e) { // TODO // Auto-generated // catch
	 * block e.printStackTrace(); }
	 * 
	 * Map<String, HttpEntity> param = new HashMap<String, HttpEntity>();
	 * param.put(AQuery.POST_ENTITY, entity);
	 * 
	 * aq.ajax("http://api.heuristix.net/one_todo/v1/user/register", param,
	 * JSONObject.class, new AjaxCallback<JSONObject>() {
	 * 
	 * @Override public void callback(String url, JSONObject json, AjaxStatus
	 * status) { // dismis();
	 * 
	 * int id = -1; try {
	 * 
	 * JSONObject obj1 = new JSONObject(json .toString()); message =
	 * obj1.getBoolean("error"); id = obj1.getInt("result");
	 * 
	 * } catch (Exception e) { } Constants.user_id=id; Log.v("Response",
	 * json.toString() + message+""); if(message==false){
	 * showUserDetailsActivity(); } } });
	 * 
	 * 
	 * try { post.setEntity(new UrlEncodedFormEntity(pairs)); } catch
	 * (UnsupportedEncodingException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); }
	 * 
	 * try { response = client.execute(post); } catch (ClientProtocolException
	 * e1) { // TODO Auto-generated catch block e1.printStackTrace();
	 * asyn.cancel(true); } catch (IOException e1) { // TODO Auto-generated
	 * catch block e1.printStackTrace(); asyn.cancel(true); }
	 * 
	 * return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) {
	 * super.onPostExecute(result);
	 * 
	 * // TODO Auto-generated method stub super.onPostExecute(result); String
	 * temp = null;
	 * 
	 * try { temp = EntityUtils.toString(response.getEntity()); } catch
	 * (org.apache.http.ParseException | IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); asyn.cancel(true); } // get all headers
	 * Header[] headers = response.getAllHeaders(); for (Header header :
	 * headers) { if (header.getName().equals("result")) { Constants.user_id =
	 * Integer.valueOf(header.getValue()); }
	 * 
	 * Log.v("user id ", Constants.user_id + ""); } Log.v("Response ", temp);
	 * showUserDetailsActivity();
	 * 
	 * 
	 * 10-29 08:31:54.079: V/Response(1109):
	 * {"message":"User created successfully" ,"error":false,"result":18}User
	 * created successfully
	 * 
	 * 
	 * }
	 * 
	 * @Override protected void onPreExecute() { // TODO Auto-generated method
	 * stub super.onPreExecute();
	 * 
	 * 
	 * client = new DefaultHttpClient(); post = new HttpPost(
	 * "http://api.heuristix.net/one_todo/v1/user/register");
	 * 
	 * pairs = new ArrayList<NameValuePair>(); Random r = new Random(); int i1 =
	 * r.nextInt(100000 - 1 + 1) + 1; pairs.add(new BasicNameValuePair("email",
	 * "example" + i1 + "@gmail.com")); pairs.add(new
	 * BasicNameValuePair("password", "eyspasd")); pairs.add(new
	 * BasicNameValuePair("registration_type", "none")); pairs.add(new
	 * BasicNameValuePair("registration_type_id", "3")); pairs.add(new
	 * BasicNameValuePair("device_type_id", Secure
	 * .getString(getActivity().getContentResolver(), Secure.ANDROID_ID) + ""));
	 * pairs.add(new BasicNameValuePair("device_type", Build.MODEL + ""));
	 * pairs.add(new BasicNameValuePair("mobile_no", code.get(position) +
	 * aq.id(R.id.country).getText().toString() + "")); pairs.add(new
	 * BasicNameValuePair("country", country.get(position) .toString() + ""));
	 * pairs.add(new BasicNameValuePair("date_created", Utils .getCurrentYear(1)
	 * + "-" + Utils.getCurrentMonthDigit(1) + "-" + Utils.getCurrentDayDigit(1)
	 * + " " + Utils.getCurrentHours() + ":" + Utils.getCurrentMins() + ":00"));
	 * } }
	 */

	/*
	 * @Override public void onAttach(Activity activity) { // TODO
	 * Auto-generated method stub super.onAttach(activity); //---register parent
	 * activity for events--- try{ fragmentListener = (FragmentListener)
	 * activity; }catch (ClassCastException e) { throw new ClassCastException(
	 * "Parent activity must implement interface FragmentListener."); } }
	 */

	/*
	 * public void onAttach(Activity activity) { super.onAttach(activity);
	 * 
	 * //---register parent activity for events--- try{ fragmentListener =
	 * (FragmentListener) activity; }catch (ClassCastException e) { throw new
	 * ClassCastException
	 * ("Parent activity must implement interface FragmentListener."); } }
	 */
}

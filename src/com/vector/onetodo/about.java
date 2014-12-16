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
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.Utils;

public class about extends Fragment {

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
	List<String> country;
	List<String> code;
	TextView confirm, save;
	int position = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.about,
				container, false);
		aq = new AQuery(getActivity(), view);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}

		String html = "ONE" + "<br />" + "todo";
		aq.id(R.id.title).text(Html.fromHtml(html));
		country = new ArrayList<String>();
		code = new ArrayList<String>();
		country();
		

		
		
				/*about.this.position = position;
				aq.id(R.id.country).getEditText().requestFocus();
				if (code.get(position).toString().equals("Select country")) {
					aq.id(R.id.item).getTextView()
							.setTextColor(Color.parseColor("#CECECE"));

				} else {
					aq.id(R.id.item).getTextView()
							.setTextColor(Color.parseColor("#FFFFFF"));
				}
			}*/

			

	
		skip = (TextView) getActivity().findViewById(R.id.loginSkip);

		View vie = getActivity().getLayoutInflater().inflate(R.layout.skip,
				null, false);
		AlertDialog.Builder builderLabel = new AlertDialog.Builder(
				getActivity());
		builderLabel.setView(vie);
		alert = builderLabel.create();
		confirm = (TextView) vie.findViewById(R.id.skip_confirm);
		save = (TextView) vie.findViewById(R.id.skip_save);

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
					if(Constants.RegId!=null){

						AddRegister();
					}
					else{
						Toast.makeText(getActivity(),
								"Try Again...",
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

	public void country() {

		code.add("+   ");

		country.add("Select country");
		country.add("Canada");
		code.add("+1");
		country.add("East Timor");
		code.add("+670");
		country.add("Sao Tome and Principe");
		code.add("+239");
		country.add("Turkmenistan");
		code.add("+993");
		country.add("Saint Helena");
		code.add("+290");
		country.add("Northern Marianas");
		code.add("+1");
		country.add("United States of America");
		code.add("+1");
		country.add("Lithuania");
		code.add("+370");
		country.add("Cambodia");
		code.add("+855");
		country.add("Switzerland");
		code.add("+41");
		country.add("Ethiopia");
		code.add("+251");
		country.add("Aruba");
		code.add("+297");
		country.add("Swaziland");
		code.add("+268");
		country.add("Wallis and Futuna");
		code.add("+681");
		country.add("Argentina");
		code.add("+54");
		country.add("Bolivia");
		code.add("+591");
		country.add("Cameroon");
		code.add("+237");
		country.add("Burkina Faso");
		code.add("+226");
		country.add("Ghana");
		code.add("+233");
		country.add("Saudi Arabia");
		code.add("+966");
		country.add("Japan");
		code.add("+81");
		country.add("Cape Verde");
		code.add("+238");
		country.add("Slovenia");
		code.add("+386");
		country.add("Guatemala");
		code.add("+502");
		country.add("Bosnia and Herzegovina");
		code.add("+387");
		country.add("Kuwait");
		code.add("+965");
		country.add("Russian Federation");
		code.add("+7");
		country.add("Jordan");
		code.add("+962");
		country.add("UAE");
		code.add("+971");
		country.add("Dominica");
		code.add("+1");
		country.add("Liberia");
		code.add("+231");
		country.add("Maldives");
		code.add("+960");
		country.add("Jamaica");
		code.add("+1");
		country.add("Oman");
		code.add("+968");
		country.add("Tanzania");
		code.add("+255");
		country.add("Martinique");
		code.add("+596");
		country.add("Ivory Coast");
		code.add("+225");
		country.add("Albania");
		code.add("+355");
		country.add("Gabon");
		code.add("+241");
		country.add("Saint Pierre and Miquelon");
		code.add("+508");
		country.add("Monaco");
		code.add("+377");
		country.add("England");
		code.add("+44");
		country.add("New Zealand");
		code.add("+64");
		country.add("Yemen");
		code.add("+967");
		country.add("Falkland (Malvinas) Islands");
		code.add("+500");
		country.add("Denmark");
		code.add("+45");
		country.add("Andorra");
		code.add("+376");
		country.add("Greenland");
		code.add("+299");
		country.add("Samoa");
		code.add("+685");
		country.add("Chinese Taipei");
		code.add("+886");
		country.add("Macau");
		code.add("+853");
		country.add("United Arab Emirates");
		code.add("+971");
		country.add("Guam");
		code.add("+1");
		country.add("India");
		code.add("+91");
		country.add("Azerbaijan");
		code.add("+994");
		country.add("Lesotho");
		code.add("+266");
		country.add("Saint Vincent and the Grenadines");
		code.add("+1");
		country.add("Cyprus");
		code.add("+357");
		country.add("South Korea");
		code.add("+82");
		country.add("Tajikistan");
		code.add("+992");
		country.add("Turkey");
		code.add("+90");
		country.add("Afghanistan");
		code.add("+93");
		country.add("Bangladesh");
		code.add("+880");
		country.add("Mauritania");
		code.add("+222");
		country.add("Solomon Islands");
		code.add("+677");
		country.add("Turks and Caicos Islands");
		code.add("+1");
		country.add("Saint Lucia");
		code.add("+1");
		country.add("San Marino");
		code.add("+378");
		country.add("French Polynesia");
		code.add("+689");
		country.add("France");
		code.add("+33");
		country.add("Bermuda");
		code.add("+1");
		country.add("Slovakia");
		code.add("+421");
		country.add("Somalia");
		code.add("+252");
		country.add("Peru");
		code.add("+51");
		country.add("Laos");
		code.add("+856");
		country.add("Nauru");
		code.add("+674");
		country.add("Seychelles");
		code.add("+248");
		country.add("Norway");
		code.add("+47");
		country.add("Malawi");
		code.add("+265");
		country.add("Cook Islands");
		code.add("+682");
		country.add("Benin");
		code.add("+229");
		country.add("Korea");
		code.add("+82");
		country.add("Cuba");
		code.add("+53");
		country.add("Niue Island");
		code.add("+683");
		country.add("Montenegro");
		code.add("+382");
		country.add("Saint Kitts and Nevis");
		code.add("+1");
		country.add("Togo");
		code.add("+228");
		country.add("China");
		code.add("+86");
		country.add("Armenia");
		code.add("+374");
		country.add("Dominican Republic");
		code.add("+1");
		country.add("America");
		code.add("+1");
		country.add("Mongolia");
		code.add("+976");
		country.add("Ukraine");
		code.add("+380");
		country.add("Bahrain");
		code.add("+973");
		country.add("Tonga");
		code.add("+676");
		country.add("Finland");
		code.add("+358");
		country.add("Libya");
		code.add("+218");
		country.add("Cayman Islands");
		code.add("+1");
		country.add("Central African Republic");
		code.add("+236");
		country.add("Mauritius");
		code.add("+230");
		country.add("Ascension");
		code.add("+247");
		country.add("Liechtenstein");
		code.add("+423");
		country.add("Australia");
		code.add("+61");
		country.add("British Virgin Islands");
		code.add("+1");
		country.add("Kenya");
		code.add("+254");
		country.add("Vatican City");
		code.add("+379");
		country.add("Bulgaria");
		code.add("+359");
		country.add("United States");
		code.add("+1");
		country.add("Romania");
		code.add("+40");
		country.add("Angola");
		code.add("+244");
		country.add("Chad");
		code.add("+235");
		country.add("South Africa");
		code.add("+27");
		country.add("Tokelau");
		code.add("+690");
		country.add("Democratic Republic of Congo");
		code.add("+243");
		country.add("Sweden");
		code.add("+46");
		country.add("Qatar");
		code.add("+974");
		country.add("Malaysia");
		code.add("+60");
		country.add("Austria");
		code.add("+43");
		country.add("Vietnam");
		code.add("+84");
		country.add("Mozambique");
		code.add("+258");
		country.add("Uganda");
		code.add("+256");
		country.add("Hungary");
		code.add("+36");
		country.add("Niger");
		code.add("+227");
		country.add("Brazil");
		code.add("+55");
		country.add("Faroe Islands");
		code.add("+298");
		country.add("Guinea");
		code.add("+224");
		country.add("Panama");
		code.add("+507");
		country.add("Mali");
		code.add("+223");
		country.add("Costa Rica");
		code.add("+506");
		country.add("Luxembourg");
		code.add("+352");
		country.add("American Samoa");
		code.add("+1");
		country.add("Bahamas");
		code.add("+1");
		country.add("Great Britain");
		code.add("+44");
		country.add("Gibraltar");
		code.add("+350");
		country.add("Ireland");
		code.add("+353");
		country.add("Pakistan");
		code.add("+92");
		country.add("Palau");
		code.add("+680");
		country.add("Nigeria");
		code.add("+234");
		country.add("Ecuador");
		code.add("+593");
		country.add("Czech Republic");
		code.add("+420");
		country.add("Brunei");
		code.add("+673");
		country.add("Belarus");
		code.add("+375");
		country.add("Iran");
		code.add("+98");
		country.add("USA");
		code.add("+1");
		country.add("Algeria");
		code.add("+213");
		country.add("El Salvador");
		code.add("+503");
		country.add("Tuvalu");
		code.add("+688");
		country.add("Marshall Islands");
		code.add("+692");
		country.add("Chile");
		code.add("+56");
		country.add("Puerto Rico");
		code.add("+1");
		country.add("Belgium");
		code.add("+32");
		country.add("Kiribati");
		code.add("+686");
		country.add("Haiti");
		code.add("+509");
		country.add("Belize");
		code.add("+501");
		country.add("Hong Kong");
		code.add("+852");
		country.add("Sierra Leone");
		code.add("+232");
		country.add("Georgia");
		code.add("+995");
		country.add("Iridium Satellite");
		code.add("+8816/8817");
		country.add("Gambia");
		code.add("+220");
		country.add("Philippines");
		code.add("+63");
		country.add("Thuraya Satellite");
		code.add("+882 16");
		country.add("Moldova");
		code.add("+373");
		country.add("Morocco");
		code.add("+212");
		country.add("Croatia");
		code.add("+385");
		country.add("Micronesia");
		code.add("+691");
		country.add("Guinea-Bissau");
		code.add("+245");
		country.add("Thailand");
		code.add("+66");
		country.add("Namibia");
		code.add("+264");
		country.add("Grenada");
		code.add("+1");
		country.add("Britain");
		code.add("+44");
		country.add("U.S. Virgin Islands");
		code.add("+1");
		country.add("Iraq");
		code.add("+964");
		country.add("Portugal");
		code.add("+351");
		country.add("Estonia");
		code.add("+372");
		country.add("Uruguay");
		code.add("+598");
		country.add("Mexico");
		code.add("+52");
		country.add("Lebanon");
		code.add("+961");
		country.add("Uzbekistan");
		code.add("+998");
		country.add("Tunisia");
		code.add("+216");
		country.add("Djibouti");
		code.add("+253");
		country.add("Rwanda");
		code.add("+250");
		country.add("Antigua and Barbuda");
		code.add("+1");
		country.add("Spain");
		code.add("+34");
		country.add("Colombia");
		code.add("+57");
		country.add("Reunion");
		code.add("+262");
		country.add("Burundi");
		code.add("+257");
		country.add("GBR");
		code.add("+44");
		country.add("Taiwan");
		code.add("+886");
		country.add("Fiji");
		code.add("+679");
		country.add("Barbados");
		code.add("+1");
		country.add("Madagascar");
		code.add("+261");
		country.add("Italy");
		code.add("+39");
		country.add("Bhutan");
		code.add("+975");
		country.add("Sudan");
		code.add("+249");
		country.add("Nepal");
		code.add("+977");
		country.add("Malta");
		code.add("+356");
		country.add("Netherlands");
		code.add("+31");
		country.add("Suriname");
		code.add("+597");
		country.add("Anguilla");
		code.add("+1");
		country.add("Venezuela");
		code.add("+58");
		country.add("Israel");
		code.add("+972");
		country.add("Indonesia");
		code.add("+62");
		country.add("Iceland");
		code.add("+354");
		country.add("Zambia");
		code.add("+260");
		country.add("Senegal");
		code.add("+221");
		country.add("Papua New Guinea");
		code.add("+675");
		country.add("Trinidad and Tobago");
		code.add("+1");
		country.add("Germany");
		code.add("+49");
		country.add("Vanuatu");
		code.add("+678");
		country.add("Diego Garcia");
		code.add("+246");
		country.add("Kazakhstan");
		code.add("+7");
		country.add("Poland");
		code.add("+48");
		country.add("Eritrea");
		code.add("+291");
		country.add("Kyrgyzstan");
		code.add("+996");
		country.add("UK");
		code.add("+44");
		country.add("Mayotte");
		code.add("+262");
		country.add("Montserrat");
		code.add("+1");
		country.add("New Caledonia");
		code.add("+687");
		country.add("Macedonia");
		code.add("+389");
		country.add("North Korea");
		code.add("+850");
		country.add("Sri Lanka");
		code.add("+94");
		country.add("Latvia");
		code.add("+371");
		country.add("Guyana");
		code.add("+592");
		country.add("Syria");
		code.add("+963");
		country.add("Guadeloupe");
		code.add("+590");
		country.add("Inmarsat Satellite");
		code.add("+870");
		country.add("Burma");
		code.add("+95");
		country.add("Honduras");
		code.add("+504");
		country.add("Myanmar");
		code.add("+95");
		country.add("Equatorial Guinea");
		code.add("+240");
		country.add("Egypt");
		code.add("+20");
		country.add("Nicaragua");
		code.add("+505");
		country.add("Singapore");
		code.add("+65");
		country.add("Serbia");
		code.add("+381");
		country.add("Botswana");
		code.add("+267");
		country.add("United Kingdom");
		code.add("+44");
		country.add("Congo");
		code.add("+242");
		country.add("Greece");
		code.add("+30");
		country.add("Paraguay");
		code.add("+595");
		country.add("French Guiana");
		code.add("+594");
		country.add("Comoros");
		code.add("+269");

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
		pairs.add(new BasicNameValuePair("mobile_no", code.get(position)
				+ aq.id(R.id.country).getText().toString() + ""));
		pairs.add(new BasicNameValuePair("country", country.get(position)
				.toString() + ""));
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
						if(id!=-1){
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
}

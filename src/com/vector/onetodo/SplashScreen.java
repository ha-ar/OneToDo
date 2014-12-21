package com.vector.onetodo;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.androidquery.AQuery;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vector.onetodo.utils.Constants;

public class SplashScreen extends BaseActivity {

	AQuery aq;
	public static Editor editor,Regeditor;
	static SharedPreferences pref;
	static SharedPreferences regid;
	static GoogleCloudMessaging gcm=null;
	static String regId;
	public static String country=null,code=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		regid=this.getSharedPreferences("Registration", 0);
		pref = this.getSharedPreferences("registration", 0);
		
		Regeditor = regid.edit();
		aq = new AQuery(this);
		editor = pref.edit();
		//***************CHeck device registeration for GCM
		
		if(regid.getString("reg", null)==null){
			registerInBackground(this);
		}
		else{

        	Constants.RegId=regid.getString("reg", null);
		}
		
		
		if(pref.getInt("userid", -1)!=-1){
			Constants.user_id=pref.getInt("userid", -1);
			showUserLandingActivity();
		}
		
		String html = "ONE" + "<br />" + "todo";
		aq.id(R.id.title).text(Html.fromHtml(html));

		aq.id(R.id.intro_1)
				.text(Html
						.fromHtml("<b>"
								+ "ONE-todo"
								+ "</b>"
								+ " is a cross-palteform organiser app for both individuals and groups."));
		aq.id(R.id.intro_2)
				.text(Html
						.fromHtml("<b>"
								+ "ONE-todo"
								+ "</b>"
								+ " provides real time collaboration for group activities."));
		aq.id(R.id.intro_3)
				.text(Html
						.fromHtml("<b>"
								+ "ONE-todo"
								+ "</b>"
								+ " works with phone number so you  don’t need any log-in or password."));
		aq.id(R.id.intro_4).text(
				Html.fromHtml("<b>" + "ONE-todo" + "</b>"
						+ " keeps everything at one place."));

		aq.id(R.id.intro_5).text(Html.fromHtml("To-do's"));
		aq.id(R.id.intro_6).text(Html.fromHtml("Events"));
		aq.id(R.id.intro_7).text(Html.fromHtml("Appoinments"));
		aq.id(R.id.intro_8).text(Html.fromHtml("Schedules"));
		aq.id(R.id.intro_9).text(Html.fromHtml("Projects"));

		aq.id(R.id.button1).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showUserDetailsActivity();
			}
		});

	}

	private void showUserDetailsActivity() {
		
		
		Fragment fr = new about();
		FragmentManager manager=getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.replace(R.id.container, fr);
		trans.addToBackStack("");
		trans.commit();
	}
	
	private void showUserLandingActivity() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(this.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();/*
		this
				.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);*/
	}
	
	 static void registerInBackground(final Context context)  {

		 
	        new AsyncTask<Void, Void, String>() {
	            @Override
	            protected String doInBackground(Void... params) {
	                String msg = "";
	                try {
	                    if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(context);
	                    }
	                    regId = gcm.register(Constants.SENDER_ID);
	                    Log.d("RegisterActivity", "registerInBackground - regId: "
	                            + regId);
	                    msg = "Device registered, registration ID=" + regId;

	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                    Log.d("RegisterActivity", "Error: " + msg);
	                }
	                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(String msg) {
	            	Constants.RegId=regId;
	                Log.v("REGISTERID","this is my reg id**********"+regId);
	                Regeditor.putString("reg", regId);
	            }
	        }.execute(null, null, null);
	    }
}

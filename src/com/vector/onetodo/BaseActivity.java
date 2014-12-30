package com.vector.onetodo;

import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.vector.onetodo.db.gen.DaoMaster;
import com.vector.onetodo.db.gen.DaoSession;/*
import com.vector.onetodo.db.gen.LabelName;
import com.vector.onetodo.db.gen.LabelNameDao;*/
import com.vector.onetodo.utils.Constants;

public abstract class BaseActivity extends ActionBarActivity {

	public static AQuery aq, aqd, aq_menu;
	public static HashMap<Integer, String> pageName = new HashMap<Integer, String>(),
			pagename2 = new HashMap<Integer, String>(),
			typeName = new HashMap<Integer, String>();
	public static int ONE_DAY = 1000 * 60 * 60 * 24;
	public static int TWO_DAYS = 2 * ONE_DAY;
	public static final int TODAY = 0, TOMORROW = 1, UPCOMING = 2, Work = 0,
			Home = 1, Personal = 2, Studies = 3, Meetups = 4, Games = 5;

	/*static LabelNameDao labelnamedao;*/
	public DaoSession daoSession;
	public DaoMaster daoMaster;
	SQLiteDatabase db;
	Long id = null;
	static int check = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aq = new AQuery(this);
		db_initialize();
	/*	Constants.label_name = labelnamedao.loadAll();

		if (Constants.label_name.size() == 0) {
			LabelName ln = new LabelName();
			ln.setId(id);
			ln.setName("Work");
			labelnamedao.insert(ln);
			ln.setId(id);
			ln.setName("Home");
			labelnamedao.insert(ln);
			ln.setId(id);
			ln.setName("Personal");
			labelnamedao.insert(ln);
		}

		for (int i = 0; i < Constants.label_name.size(); i++) {
			pagename2.put(i, Constants.label_name.get(i).getName());
		}*/

		pageName.put(TODAY, "TODAY");
		pageName.put(TOMORROW, "TOMORROW");
		pageName.put(UPCOMING, "UPCOMING");

		typeName.put(0, "My Todo's");
		typeName.put(1, "Assigned Tasks");
		typeName.put(2, "Shared Tasks");
		typeName.put(3, "Delayed Tasks");
	}

	public void db_initialize() {

		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
				"OneTodo-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();/*
		labelnamedao = daoSession.getLabelNameDao();*/

	}
}
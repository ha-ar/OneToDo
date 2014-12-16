package com.vector.onetodo.utils;

import java.util.List;

import com.vector.onetodo.db.gen.LabelName;

public class Constants {

	public final static String ICON_FONT = "onetodo.ttf";

	public static int user_id=-1;
	public static boolean date,time,week;
	public static List<LabelName> label_name;
	public final static String MORE = "";
	public static String SENDER_ID="184279149655";
	public static String RegId="";
	public final static String CLOCK = "";
	public final static String ADD = "";
	public final static String SETTINGS = "";
	public final static String SHARE = "";
	public final static String RESCHEDULE = "";
	public final static String DELETE = "";
	public final static String[] CONTACTS_EVOKING_WORDS = { "call", "message",
			"sms", "text", "ring", "dial", "phone", "telephone", "mobile",
			"ping", "buzz", "email", "mail" };
	public static String NORMAL_TYPEFACE = "HelveticaNeue.otf";
	public static String LIGHT_TYPEFACE = "HelveticaNeue-Light.otf";
	public static String MED_TYPEFACE = "HelveticaNeue-Medium.otf";
	public static String BOLD_TYPEFACE = "HelveticaNeueLTStd-Bd.otf";
	public static String ROMAN_TYPEFACE = "HelveticaNeueLTStd-Roman.otf";
	public static int AlaramIndex = -1;
	public static int AlaramSize = 0;
	public static float density = 0, dp = 0;

	// public static String[] state = { "View Tasks", "Settings", "Help",
	// "About" };
	// public static String[][] parent = { { "aa", "bb", "cc", "dd", "ee" },
	// { "ff", "gg", "hh", "ii", "jj" }, { "kk", "ll", "mm", "nn", "oo" },{ } };
	//
	// public static String[][][] child = {
	// { { "aaa", "aab", "aac", "aad", "aae" },
	// { "bba", "bbb", "bbc", "bbd", "bbe" },
	// { "cca", "ccb", "ccc", "ccd", "cce", "ccf", "ccg" },
	// { "dda", "ddb", "dddc", "ddd", "dde", "ddf" },
	// { "eea", "eeb", "eec" } },
	// { { "ffa", "ffb", "ffc", "ffd", "ffe" },
	// { "gga", "ggb", "ggc", "ggd", "gge" },
	// { "hha", "hhb", "hhc", "hhd", "hhe", "hhf", "hhg" },
	// { "iia", "iib", "iic", "iid", "iie", "ii" },
	// { "jja", "jjb", "jjc", "jjd" } },
	// { { "kka", "kkb", "kkc", "kkd", "kke" },
	// { "lla", "llb", "llc", "lld", "lle" },
	// { "mma", "mmb", "mmc", "mmd", "mme", "mmf", "mmg" },
	// { "nna", "nnb", "nnc", "nnd", "nne", "nnf" },
	// { "ooa", "oob" } } };
	public static String[] state = { "Quick Access Tasks", "Settings", "Help",
			"About" };
	public static String[][] parent = {
			{ "Delayed", "Assigned", "Shared", "Completed", "Deleted", "None" },
			{ "Date Format", "Time Format", "Set Start of Week",
					"Unscheduled Tasks" }, {}, {} };

	public static String[][][] child = {
			{ {}, {}, {}, {}, {}, {} },
			{ { "DD/MM/YY", "MM/DD/YY" }, { "12H", "24H" },
					{ "Monday", "Sunday" }, {}, {} }, { {}, {}, {}, {}, {} },
			{ {}, {}, {}, {}, {} } };
}

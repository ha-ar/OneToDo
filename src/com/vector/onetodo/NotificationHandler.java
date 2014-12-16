package com.vector.onetodo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.vector.onetodo.utils.Constants;

public class NotificationHandler {
	// Notification handler singleton
	private static NotificationHandler nHandler;
	private static NotificationManager mNotificationManager;

	private NotificationHandler () {}
	/**
	 * Singleton pattern implementation
	 * @return
	 */
	public static  NotificationHandler getInstance(Context context) {
		if(nHandler == null) {
			nHandler = new NotificationHandler();
			mNotificationManager =
					(NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		}

		return nHandler;
	}


	/**
	 * Shows a simple notification
	 * @param context aplication context
	 */
	public void createSimpleNotification(Context context) {
		// Creates an explicit intent for an Activity
	
		Intent resultIntent = new Intent(context, TestActivity.class);

		// Creating a artifical activity stack for the notification activity
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(TestActivity.class);
		stackBuilder.addNextIntent(resultIntent);

		// Pending intent to the notification manager
		PendingIntent resultPending = stackBuilder
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		// Building the notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher) // notification icon
				.setContentTitle(AlarmManagerBroadcastReceiver.todo_Qlist.get(Constants.AlaramIndex).getTitle())
				.setContentText(AlarmManagerBroadcastReceiver.todo_Qlist.get(Constants.AlaramIndex).getLocation()) // notification text
				.setContentIntent(resultPending); // notification intent

		// mId allows you to update the notification later on.
		mNotificationManager.notify(10, mBuilder.build());
	}
	public void createSimpleNotification2(Context context,String title,String message) {
		// Creates an explicit intent for an Activity
	
		Intent resultIntent = new Intent(context, MainActivity.class);

		// Creating a artifical activity stack for the notification activity
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);

		// Pending intent to the notification manager
		PendingIntent resultPending = stackBuilder
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
/*	[{message=just checking, title=Title, 
			smallIcon=small_icon, tickerText=Ticker text here...Ticker text
			here...Ticker text here, 
			collapse_key=do_not_collapse, sound=1, subtitle=Subtitle, 
			vibrate=1, android.support.content.wakelockid=2, 
			largeIcon=large_icon, from=184279149655}]*/

		// Building the notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher) // notification icon
				.setContentTitle(title)
				.setContentText(message) // notification text
				.setContentIntent(resultPending); // notification intent

		
		mBuilder.setContentTitle(title).setContentText(message);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
		
		/*int notifyID = 1;
		mNotifyBuilder = new NotificationCompat.Builder(this)
		    .setContentTitle("New Message")
		    .setContentText("You've received new messages.")
		    .setSmallIcon(R.drawable.ic_notify_status)
		numMessages = 0;
		// Start of a loop that processes data and then notifies the user

		
		    // Because the ID remains unchanged, the existing notification is
		    // updated.
		    mNotificationManager.notify(
		            notifyID,
		            mNotifyBuilder.build());*/

	}
}
